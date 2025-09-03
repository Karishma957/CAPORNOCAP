import { MostPlayed } from "./MostPlayed";
import { ScoresCombo } from "./ScoresCombo";
import { TimeSeriesByMinute } from "./TimeSeriesByMinute";

export interface DeveloperConsoleState {
    onlinePlayers: number;
    activeQuizzes: number;
    totalPlayers: number;
    totalQuizzesPlayed: number;
    totalRecommendationsAccepted: number;

    timeSeriesByMinute: TimeSeriesByMinute[];
    achievementCount: { [key: string]: number } | null;
    mostPlayed: MostPlayed[];
    scoresByCombo: ScoresCombo[];
}