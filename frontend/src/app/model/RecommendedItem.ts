export interface RecommendedItem {
    id: string;
    genre: string;
    difficulty: 'EASY' | 'MEDIUM' | 'HARD';
    icon: string;
    accent: string;
};