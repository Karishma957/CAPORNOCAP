export interface RecommendedItem {
    id: string;
    genre: string;
    difficulty: 'Easy' | 'Medium' | 'Hard';
    icon: string;
    accent: string;
};