export interface ScoreEvent {
    playerId: number;
    quizSessionId: number;
    score: number;
    totalQuestions: Date;
    correctAnswers: number;
    difficulty: string;
    genreName: string;
}