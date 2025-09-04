import { Injectable } from '@angular/core';
import { PlayerActivityEvent } from '../model/PlayerActivityEvent';
import { ApiService } from './api.service';

const TOKEN_KEY = 'capnocap_auth_token';
const PLAYER_ID = 'capnocap_playerid';
const USERNAME = 'capnocap_username';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  username: string | null = null;
  token: string | null = null;
  playerId: number | null = null;

  loggedIn: boolean = false;
  loginErrorMessage: string = "";

  constructor(private apiService: ApiService) {
    this.token = localStorage.getItem(TOKEN_KEY);
    this.username = localStorage.getItem(USERNAME);
    this.playerId = Number(localStorage.getItem(PLAYER_ID));
    this.loggedIn = !!this.token && !!this.username && !!this.playerId;
  }

  isUserLoggedIn(): boolean {
    return this.loggedIn;
  }


  getToken(): string | null {
    return this.token;
  }

  getPlayerId(): number | null {
    return this.playerId;
  }

  getUsername(): string | null {
    return this.username;
  }

  setAuth(playerId: number, username: string, token: string): void {
    localStorage.setItem(TOKEN_KEY, token);
    localStorage.setItem(PLAYER_ID, playerId.toString());
    localStorage.setItem(USERNAME, username);
    this.token = token
    this.playerId = playerId;
    this.username = username;
    this.loggedIn = true;
  }

  clearAuth(): void {
    const activityEvent: PlayerActivityEvent = { playerId: this.playerId!, userActivityType: 'LOGOUT', achievement: '' };
    this.apiService.sendPlayerActivityEvent(activityEvent).subscribe();
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(PLAYER_ID);
    localStorage.removeItem(USERNAME);
    this.token = null;
    this.playerId = null;
    this.username = null;
    this.loggedIn = false;
  }
}
