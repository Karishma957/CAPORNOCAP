import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "../../environments";
import { HttpClient } from "@angular/common/http";
import { PlayerActivityEvent } from "../model/PlayerActivityEvent";

@Injectable({
    providedIn: 'root'
})
export class ApiService {

    private baseUrl = environment.production ? '/api' : environment.backendBaseUrl;

    constructor(private http: HttpClient) { }

    register(username: string, password: string): Observable<any> {
        return this.http.post(`${this.baseUrl}/auth/register`, { username, password });
    }

    getRecommendations(playerId: number): Observable<any> {
        return this.http.get(`${this.baseUrl}/recommend/${playerId}`);
    }

    getProfile(playerId: number): Observable<any> {
        return this.http.get(`${this.baseUrl}/player/${playerId}`);
    }

    getLeaderboard(page: number, size: number): Observable<any> {
        return this.http.get(`${this.baseUrl}/leaderboard`,
            { params: { page, size } }
        );
    }

    getQuiz(genre: string, difficulty: string): Observable<any> {
        return this.http.get(`${this.baseUrl}/quiz`, {
            params: { genre, difficulty }
        });
    }

    submitQuiz(payload: any): Observable<any> {
        return this.http.post(`${this.baseUrl}/submitQuiz`, payload);
    }

    sendPlayerActivityEvent(payload: PlayerActivityEvent) {
        console.log("sending player activity event: {}", payload);
        return this.http.post(`${this.baseUrl}/playerActivity`, payload);
    }
}