package com.capornocap.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.capornocap.dto.DeveloperConsoleState;
import com.capornocap.model.MostPlayed;
import com.capornocap.model.ScoresCombo;
import com.capornocap.model.TimeSeriesByMinute;
import com.capornocap.utils.Difficulty;
import com.capornocap.utils.Genre;

@Service
public class DeveloperConsoleService {
    private final Set<Long> onlinePlayers = ConcurrentHashMap.newKeySet();
    private Set<Long> activeQuizzes = ConcurrentHashMap.newKeySet();

    private final AtomicLong totalPlayers = new AtomicLong(0);
    private final AtomicLong totalQuizzesPlayed = new AtomicLong(0);
    private final AtomicLong recommendationsAccepted = new AtomicLong(0);

    private static final int WINDOW = 60;
    private final RollingCounter logins = new RollingCounter(WINDOW);
    private final RollingCounter logouts = new RollingCounter(WINDOW);
    private final RollingCounter quizStarted = new RollingCounter(WINDOW);

    private final Map<String, AtomicLong> genreDifficultyCount = new ConcurrentHashMap<>();
    private final Map<String, Score> genreDifficultyTotalScore = new ConcurrentHashMap<>();

    // used by controller to push updates
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter registerEmitter() {
        SseEmitter emitter = new SseEmitter(0L);
        this.emitters.add(emitter);
        emitter.onCompletion(() -> this.emitters.remove(emitter));
        emitter.onTimeout(() -> this.emitters.remove(emitter));
        return emitter;
    }

    private void pushToEmitters() {
        DeveloperConsoleState state = build();
        for (var emitter : this.emitters) {
            try {
                emitter.send(state, MediaType.APPLICATION_JSON);
            } catch (Exception e) {
                this.emitters.remove(emitter);
            }
        }
    }

    public DeveloperConsoleState snapshot() {
        return build();
    }

    private DeveloperConsoleState build() {
        List<TimeSeriesByMinute> ts = new ArrayList<>();
        var li = logins.series();
        var lo = logouts.series();
        var qs = quizStarted.series();
        int maxSize = Math.max(li.size(), Math.max(lo.size(), qs.size()));
        for (int i = 0; i < maxSize; i++) {
            int loginCount = i < li.size() ? li.get(i).count : 0;
            int logoutCount = i < lo.size() ? lo.get(i).count : 0;
            int quizCount = i < qs.size() ? qs.get(i).count : 0;
            long minute = i < li.size() ? li.get(i).minute
                    : i < lo.size() ? lo.get(i).minute
                            : qs.get(i).minute;
            ts.add(new TimeSeriesByMinute(minute, loginCount, logoutCount, quizCount));
        }
        List<MostPlayed> mostPlayeds = new ArrayList<>();

        genreDifficultyCount.forEach((k, v) -> {
            String[] parts = k.split("-");
            mostPlayeds.add(MostPlayed.builder()
                    .genre(Genre.fromString(parts[0]))
                    .difficulty(Difficulty.fromString(parts[1]))
                    .plays(v.get())
                    .build());
        });

        List<ScoresCombo> scoresCombos = new ArrayList<>();
        genreDifficultyTotalScore.forEach((k, v) -> {
            String[] parts = k.split("-");
            scoresCombos.add(ScoresCombo.builder()
                    .genre(Genre.fromString(parts[0]))
                    .difficulty(Difficulty.fromString(parts[1]))
                    .plays(v.count)
                    .averageScore(v.average())
                    .build());
        });
        return DeveloperConsoleState.builder()
                .onlinePlayers((long) onlinePlayers.size())
                .activeQuizzes((long) activeQuizzes.size())
                .totalPlayers(totalPlayers.get())
                .totalQuizzesPlayed(totalQuizzesPlayed.get())
                .totalRecommendationsAccepted(recommendationsAccepted.get())
                .timeSeriesByMinute(ts)
                .mostPlayed(mostPlayeds)
                .scoresByCombo(scoresCombos)
                .build();
    }

    private long currentMinute() {
        return Instant.now().truncatedTo(ChronoUnit.MINUTES).toEpochMilli();
    }

    public void onLogin(Long playerId) {
        onlinePlayers.add(playerId);
        logins.inc(currentMinute());
        pushToEmitters();
    }

    public void onLogout(Long playerId) {
        onlinePlayers.remove(playerId);
        logouts.inc(currentMinute());
        pushToEmitters();
    }

    public void onQuizStarted(Long playerId) {
        activeQuizzes.add(playerId);
        quizStarted.inc(currentMinute());
        pushToEmitters();
    }

    public void onQuizEnded(Long playerId) {
        activeQuizzes.remove(playerId);
        totalQuizzesPlayed.incrementAndGet();
        pushToEmitters();
    }

    public void onScore(Genre genre, Difficulty difficulty, int score) {
        String key = genre.name() + "-" + difficulty.name();
        genreDifficultyCount.computeIfAbsent(key, k -> new AtomicLong(0)).incrementAndGet();
        genreDifficultyTotalScore.computeIfAbsent(key, k -> new Score()).add(score);

        pushToEmitters();
    }

    public void onRecommendationAccepted() {
        recommendationsAccepted.incrementAndGet();
        pushToEmitters();
    }

    public void incrementTotalPlayer() {
        totalPlayers.incrementAndGet();
        pushToEmitters();
    }

    public void setTotalPlayers(long count) {
        totalPlayers.set(count);
        pushToEmitters();
    }

    private static class Score {
        long sum = 0;
        long count = 0;

        synchronized void add(int score) {
            sum += score;
            count++;
        }

        synchronized double average() {
            return count == 0 ? 0.0 : (double) sum / count;
        }
    }

    private static class RollingCounter {
        private final int size;
        private final Deque<Bucket> buckets = new ArrayDeque<>();

        public RollingCounter(int size) {
            this.size = size;
        }

        public synchronized void inc(long bucketMinute) {
            if (!buckets.isEmpty() && buckets.getLast().minute == bucketMinute) {
                buckets.getLast().count++;
            } else {
                buckets.addLast(new Bucket(bucketMinute, 1));
                if (buckets.size() > size) {
                    buckets.removeFirst();
                }
            }
        }

        public synchronized List<Bucket> series() {
            return new ArrayList<>(buckets);
        }
    }

    static class Bucket {
        long minute;
        int count;

        Bucket(long minute, int count) {
            this.minute = minute;
            this.count = count;
        }
    }
}