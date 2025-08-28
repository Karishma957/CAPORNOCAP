export interface Profile {
    username: string;
    xp: number;
    createdAt: Date;
    avatarUrl: string;
    currentAchievement: {
        title: string;
        description: string;
    }
}