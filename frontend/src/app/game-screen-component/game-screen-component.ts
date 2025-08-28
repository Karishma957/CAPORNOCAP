import { DatePipe, NgFor, NgIf } from '@angular/common';
import {
  Component,
  ElementRef,
  ViewChild,
  ViewChildren,
  QueryList,
  AfterViewInit,
  HostListener,
  ChangeDetectionStrategy,
  OnInit
} from '@angular/core';
import { Router } from '@angular/router';
import { GenreItem } from '../model/GenreItem';
import { RecommendedItem } from '../model/RecommendedItem';
import { Leaderboard } from '../model/Leaderboard';
import { Profile } from '../model/Profile';

@Component({
  selector: 'app-game-screen',
  imports: [NgFor, NgIf, DatePipe],
  templateUrl: './game-screen-component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  styleUrls: ['./game-screen-component.css']
})
export class GameScreenComponent implements OnInit {
  showRecommended: boolean = false;
  showLeaderboard: boolean = false;
  showProfile: boolean = false;
  loading: boolean = false;
  leaderboard: Leaderboard[] = [];
  profile!: Profile;

  private accentPalette: string[] = [
    "#88C999", // soft mint green
    "#A3C4BC", // muted teal
    "#B5A7D3", // soft lavender
    "#8FA9C4", // cool steel blue
    "#C2B280", // olive beige
    "#7DAEA3", // seafoam grey
    "#A7B5D1", // periwinkle
    "#9EB27C"  // moss green
  ];

  private genresNames: string[] = [
    "Entertainment", "Fashion", "Gaming", "General Knowledge", "Geography", "History", "Literature", "Mathematics", "Memes", "Music", "Science", "Sports", "Technology"
  ];

  recommended: RecommendedItem[] = [];

  genres: GenreItem[] = [];

  difficulties = ['Easy', 'Medium', 'Hard'];
  selectedGenre: GenreItem | null = null;

  constructor(private router: Router) { }

  ngOnInit(): void {
    this.loadRecommended();
    this.loadGenres();
  }

  private loadGenres(): void {
    this.genres = this.genresNames.map((g) => this.mapGenreRecommended(g));
  }

  private loadRecommended(): void {
    // this.http.get<{ genre: string; difficulty: string }[]>(`/recommended/${playerId}`)
    //   .subscribe(data => {
    //     this.recommended = data.slice(0, 3).map((item, index) => this.mapRecommended(item, index));
    //   });

    // --- Dummy data for now ---
    const dummy = [
      { genre: 'History', difficulty: 'Hard' },
      { genre: 'Science', difficulty: 'Medium' },
      { genre: 'Memes', difficulty: 'Easy' }
    ];

    this.recommended = dummy.map((item, i) => this.mapRecommended(item, i));
    if (this.recommended.length > 0) this.showRecommended = true;
  }

  private mapRecommended(item: { genre: string; difficulty: string }, index: number): RecommendedItem {
    const accent = this.accentPalette[Math.floor(Math.random() * this.accentPalette.length)];
    return {
      id: `r${index + 1}`,
      genre: item.genre,
      difficulty: item.difficulty as 'Easy' | 'Medium' | 'Hard',
      icon: `/quiz-icons/${item.genre.toUpperCase()}.png`,
      accent
    };
  }

  private mapGenreRecommended(genre: string): GenreItem {
    const accent = this.accentPalette[Math.floor(Math.random() * this.accentPalette.length)];
    return {
      name: genre,
      icon: `/quiz-icons/${genre.toUpperCase()}.png`,
      accent
    };
  }

  openDifficultyPopup(genre: GenreItem) {
    this.selectedGenre = genre
  }

  closePopup() {
    this.selectedGenre = null;
  }

  goToPlayScreen(genreName: string, difficulty: string) {
    this.router.navigate(['/play'], {
      queryParams: {
        genre: genreName,
        difficulty: difficulty
      }
    });
  }

  openLeaderboard() {
    this.showLeaderboard = true;
    this.loading = true;
    //apiCall
    this.loading = false;
    this.leaderboard = [
      { username: 'Alice', xp: 2500, avatarUrl: '/avatars/a.png' },
      { username: 'Bob', xp: 2100, avatarUrl: '/avatars/b.png' },
      { username: 'Charlie', xp: 1800, avatarUrl: '/avatars/c.png' },
      { username: 'Bob', xp: 2100, avatarUrl: '/avatars/b.png' },
      { username: 'Charlie', xp: 1800, avatarUrl: '/avatars/c.png' },
      { username: 'Bob', xp: 2100, avatarUrl: '/avatars/b.png' },
      { username: 'Charlie', xp: 1800, avatarUrl: '/avatars/c.png' },
      { username: 'Bob', xp: 2100, avatarUrl: '/avatars/b.png' },
      { username: 'Charlie', xp: 1800, avatarUrl: '/avatars/c.png' }
    ];
  }

  closeLeaderboard() {
    this.showLeaderboard = false;
    this.leaderboard = [];
  }

  openProfile() {
    this.showProfile = true;
    this.loading = true;
    // API call
    this.loading = false;
    this.profile = {
      username: 'Player1',
      xp: 1200,
      currentAchievement: {
        title: 'Sharp Shooter',
        description: 'Answered 10 questions correctly in a row!'
      },
      createdAt: new Date(),
      avatarUrl: '/avatars/player.png'
    };
  }

  closeProfile() {
    this.showProfile = false;
    this.profile;
  }

}
