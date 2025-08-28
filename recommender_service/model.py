from dataclasses import dataclass, field
from typing import List, Dict
import pandas as pd
from sklearn.ensemble import RandomForestRegressor
from sklearn.preprocessing import LabelEncoder
import numpy as np
import random

@dataclass
class ScoreEvent:
    player_id: int
    quiz_session_id: int
    score: int
    total_questions: int
    correct_answers: int
    difficulty: str
    genre_name: str

@dataclass
class RecommendationItem:
    genreName: str
    difficulty: str
    confidenceScore: float

@dataclass
class AIRecommendationResponseEvent:
    playerId: int
    recommendations: List[RecommendationItem]

    def to_dict(self):
        return {
            "playerId": self.playerId,
            "recommendations": [
                {
                    "genreName": r.genreName,
                    "difficulty": r.difficulty,
                    "confidenceScore": r.confidenceScore
                } for r in self.recommendations
            ],
        }
     

class RecommenderModel:
    def __init__(self):
        self.score_events: List[ScoreEvent] = []
        self.model = None
        self.le_player = LabelEncoder()
        self.le_genre = LabelEncoder()
        self.le_diff = LabelEncoder()
        self.trained = False

    def add_score_event(self, event: ScoreEvent):
        self.score_events.append(event)
        self._train_model()
        
    def _compute_global_popular_combos(self):
        data = pd.DataFrame([{
            'genreName': e.genre_name,
            'difficulty': e.difficulty
        } for e in self.score_events])

        agg = data.groupby(['genreName', 'difficulty']).size().reset_index(name='playCount')
        agg = agg.sort_values(by='playCount', ascending=False)
        return agg

    def _train_model(self):
        if not self.score_events:
            return

        data = pd.DataFrame([{
            'playerId': e.player_id,
            'genreName': e.genre_name,
            'difficulty': e.difficulty,
            'correctAnswers': e.correct_answers,
            'totalQuestions': e.total_questions
        } for e in self.score_events])

        agg = data.groupby(['playerId', 'genreName', 'difficulty']).agg(
            avg_accuracy=('correctAnswers', lambda x: x.sum() / data.loc[x.index, 'totalQuestions'].sum())
        ).reset_index()

        self.le_player.fit(agg['playerId'].astype(str))
        self.le_genre.fit(agg['genreName'])
        self.le_diff.fit(agg['difficulty'])

        agg['player_enc'] = self.le_player.transform(agg['playerId'].astype(str))
        agg['genre_enc'] = self.le_genre.transform(agg['genreName'])
        agg['diff_enc'] = self.le_diff.transform(agg['difficulty'])

        X = agg[['player_enc', 'genre_enc', 'diff_enc']]
        y = agg['avg_accuracy']
        self.model = RandomForestRegressor(n_estimators=100, random_state=42)
        
        self.model.fit(X, y)
        self.trained = True
        
        agg_play_counts = self._compute_global_popular_combos()
        self.most_played_combos = agg_play_counts


    def recommend(self, player_id: int, limit: int = 3) -> AIRecommendationResponseEvent:
        if not self.trained:
            return AIRecommendationResponseEvent(player_id, [])

        player_str = str(player_id)
        if player_str not in self.le_player.classes_:
            recommendations = []
            if hasattr(self, 'most_played_combos'):
                for _, row in self.most_played_combos.head(limit).iterrows():
                    recommendations.append(RecommendationItem(
                        genreName=row['genreName'],
                        difficulty=row['difficulty'],
                        confidenceScore=1.0
                    ))
            return AIRecommendationResponseEvent(
                playerId=player_id,
                recommendations=recommendations,
            )

        player_enc = self.le_player.transform([player_str])[0]
        genres = self.le_genre.classes_
        diffs = self.le_diff.classes_

        candidates = []

        for genre in genres:
            for diff in diffs:
                genre_enc = self.le_genre.transform([genre])[0]
                diff_enc = self.le_diff.transform([diff])[0]

                X_test = pd.DataFrame([[player_enc, genre_enc, diff_enc]],
                      columns=['player_enc', 'genre_enc', 'diff_enc'])
                pred = self.model.predict(X_test)[0]

                candidates.append(RecommendationItem(
                    genreName=genre,
                    difficulty=diff,
                    confidenceScore=pred
                ))

        candidates.sort(key=lambda x: x.confidenceScore, reverse=True)

        return AIRecommendationResponseEvent(
            playerId=player_id,
            recommendations=candidates[:limit],
        )


def load_training_data():
    dummy_data = []
    for player_id in range(-200, 0):  # Negative IDs for dummy players
        for _ in range(random.randint(-100, -1)):  # multiple quiz sessions
            genre = random.choice(GENRES)
            difficulty = random.choice(DIFFICULTY)
            total_questions = random.randint(5, 15)
            correct_answers = random.randint(0, total_questions)
            dummy_data.append(
                ScoreEvent(
                    player_id=player_id,
                    quiz_session_id=random.randint(1000, 2000),
                    score=correct_answers,
                    total_questions=total_questions,
                    correct_answers=correct_answers,
                    difficulty=difficulty,
                    genre_name=genre,
                    )
                )
    return dummy_data
