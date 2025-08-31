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
  OnInit,
  ChangeDetectorRef
} from '@angular/core';
import { Router } from '@angular/router';
import { GenreItem } from '../model/GenreItem';
import { RecommendedItem } from '../model/RecommendedItem';
import { Leaderboard } from '../model/Leaderboard';
import { Profile } from '../model/Profile';
import { ApiService } from '../services/api.service';
import { AuthenticationService } from '../services/authentication-service';

@Component({
  selector: 'app-game-screen',
  imports: [NgFor, NgIf, DatePipe],
  templateUrl: './game-screen-component.html',
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
    "ENTERTAINMENT", "FASHION", "GAMING", "GENERAL_KNOWLEDGE", "GEOGRAPHY", "HISTORY", "LITERATURE", "MATHEMATICS", "MEMES", "MUSIC", "SCIENCE", "SPORTS", "TECHNOLOGY"
  ];

  recommended: RecommendedItem[] = [];

  genres: GenreItem[] = [];

  difficulties = ['EASY', 'MEDIUM', 'HARD'];
  selectedGenre: GenreItem | null = null;

  constructor(private router: Router, private apiService: ApiService, private authenticationService: AuthenticationService, private changeDetectorRef: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.loadRecommended();
    this.loadGenres();
  }

  private loadGenres(): void {
    this.genres = this.genresNames.map((g) => this.mapGenreRecommended(g));
  }

  private loadRecommended(): void {
    const playerId = this.authenticationService.getPlayerId();
    if (playerId == null) {
      this.authenticationService.clearAuth();
      return;
    }
    this.apiService.getRecommendations(playerId).subscribe({
      next: (success) => {
        this.recommended = success.recommendations.map(
          (item: { genreName: string, difficulty: string, confidenceScore: number }, i: number) => this.mapRecommended(item, i));
        if (this.recommended.length > 0) this.showRecommended = true;
        this.changeDetectorRef.markForCheck();
      }
    });
  }

  private mapRecommended(item: { genreName: string, difficulty: string, confidenceScore: number }, index: number): RecommendedItem {
    const accent = this.accentPalette[Math.floor(Math.random() * this.accentPalette.length)];
    return {
      id: `r${index + 1}`,
      genre: item.genreName,
      difficulty: item.difficulty as 'EASY' | 'MEDIUM' | 'HARD',
      icon: `/quiz-icons/${item.genreName.toUpperCase()}.png`,
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
    this.apiService.getLeaderboard().subscribe({
      next: (success) => {
        this.leaderboard = success;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  closeLeaderboard() {
    this.showLeaderboard = false;
    this.leaderboard = [];
  }

  openProfile() {
    this.showProfile = true;
    this.loading = true;
    const playerId = this.authenticationService.getPlayerId();
    if (playerId == null) return;
    this.apiService.getProfile(playerId).subscribe({
      next: (success) => {
        console.log(success);
        this.profile = {
          username: success.username,
          xp: success.xp,
          currentAchievement: {
            title: success.achievement.title,
            description: success.achievement.description
          },
          createdAt: success.createdAt,
          avatarUrl: success.avatarUrl
        };
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  closeProfile() {
    this.showProfile = false;
    this.profile;
  }

  logout() {
    this.authenticationService.clearAuth();
    this.router.navigate(['/']);
  }
}
