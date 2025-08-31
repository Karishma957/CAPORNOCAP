import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "../../environments";
import { HttpClient } from "@angular/common/http";

@Injectable({
    providedIn: 'root'
})
export class ApiService {

    private baseUrl = environment.backendBaseUrl;

    constructor(private http: HttpClient) { }

    register(username: string, password: string): Observable<any> {
        return this.http.post(`${this.baseUrl}/register`, { username, password });
    }

    getRecommendations(playerId: number): Observable<any> {
        return this.http.get(`${this.baseUrl}/recommend/${playerId}`);
    }

    getProfile(playerId: number): Observable<any> {
        return this.http.get(`${this.baseUrl}/player/${playerId}`);
    }

    getLeaderboard(): Observable<any> {
        return this.http.get(`${this.baseUrl}/leaderboard`);
    }

    getQuiz(genre: string, difficulty: string): Observable<any> {
        return this.http.get(`${this.baseUrl}/quiz`, {
            params: { genre, difficulty }
        });
    }

    submitQuiz(payload: any): Observable<any> {
        return this.http.post(`${this.baseUrl}/submitQuiz`, payload);
    }
}