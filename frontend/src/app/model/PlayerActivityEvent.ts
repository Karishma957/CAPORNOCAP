export interface PlayerActivityEvent {
    playerId: number;
    userActivityType: `LOGIN` | `LOGOUT` | `QUIZ_STARTED` | `QUIZ_ENDED` | `RECOMMENDATION_ACCEPTED`;
    achievement: string;
}