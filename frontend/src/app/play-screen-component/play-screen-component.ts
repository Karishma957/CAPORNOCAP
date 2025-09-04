import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Question } from '../model/Question';
import { Answer } from '../model/Answer';
import { AuthenticationService } from '../services/authentication-service';
import { NgFor, NgIf } from '@angular/common';
import { ApiService } from '../services/api.service';
import { ReviewAnswer } from '../model/ReviewAnswer';
import { PlayerActivityEvent } from '../model/PlayerActivityEvent';


@Component({
  selector: 'app-play-screen-component',
  standalone: true,
  imports: [NgIf, NgFor],
  templateUrl: './play-screen-component.html',
  styleUrls: ['./play-screen-component.css']
})
export class PlayScreenComponent implements OnInit {
  genre: string = '';
  difficulty: string = '';
  playerId: number | null = null;
  questions: Question[] = [];
  answers: Answer[] = [];
  currentQuestionIndex: number = 0;
  currentQuestion!: Question;
  loading: boolean = true;
  timer: number = 10;
  timerInterval: any;
  quizCompleted = false;
  score: number = 0;
  totalQuestions: number = 0;
  reviewAnswer: ReviewAnswer[] = [];
  achievement: { title: string; description: string } | null = null;


  backgrounds = [
    "#88C999", // soft mint green
    "#A3C4BC", // muted teal
    "#B5A7D3", // soft lavender
    "#8FA9C4", // cool steel blue
    "#C2B280", // olive beige
    "#7DAEA3", // seafoam grey
    "#A7B5D1", // periwinkle
    "#9EB27C"  // moss green
  ];

  startTime!: string;
  endTime!: string;

  validGenres = [
    "ENTERTAINMENT", "FASHION", "GAMING", "GENERAL_KNOWLEDGE", "GEOGRAPHY",
    "HISTORY", "LITERATURE", "MATHEMATICS", "MEMES", "MUSIC",
    "SCIENCE", "SPORTS", "TECHNOLOGY"
  ];
  validDifficulties = ["EASY", "MEDIUM", "HARD"];
  invalidSelection = false;

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private router: Router,
    private authenticationService: AuthenticationService,
    private apiService: ApiService
  ) { }

  ngOnInit() {
    this.genre = this.route.snapshot.queryParamMap.get('genre') || '';
    this.difficulty = this.route.snapshot.queryParamMap.get('difficulty') || '';
    this.startTime = new Date().toISOString();
    this.playerId = this.authenticationService.getPlayerId();

    if (!this.validGenres.includes(this.genre) || !this.validDifficulties.includes(this.difficulty)) {
      this.invalidSelection = true;
      return;
    }

    this.fetchQuestions();
  }


  fetchQuestions() {
    this.apiService.getQuiz(this.genre, this.difficulty)
      .subscribe(data => {
        this.questions = data;
        this.loading = false;
        this.currentQuestion = this.questions[this.currentQuestionIndex];
        this.startTimer();
      });

    const activityEvent: PlayerActivityEvent = { playerId: this.playerId!, userActivityType: 'QUIZ_STARTED', achievement: '' };
    this.apiService.sendPlayerActivityEvent(activityEvent).subscribe();

    this.questions = [
      { id: 1, questionText: 'The Earth is flat?' },
      { id: 2, questionText: 'The Sun rises in the East?' },
      { id: 3, questionText: 'Is JavaScript the same as Java?' }
    ];

    this.loading = false;
    this.currentQuestion = this.questions[this.currentQuestionIndex];
    this.startTimer();
  }

  startTimer() {
    this.timer = 30;
    this.timerInterval = setInterval(() => {
      this.timer--;
      if (this.timer === 0) {
        this.saveAnswer(null);
      }
    }, 1000);
  }

  saveAnswer(choice: boolean | null) {
    clearInterval(this.timerInterval);

    this.answers.push({ questionId: this.questions[this.currentQuestionIndex].id, answer: choice });
    if (this.currentQuestionIndex < this.questions.length) {
      this.currentQuestionIndex++;
      this.currentQuestion = this.questions[this.currentQuestionIndex];
      this.startTimer();
    }
    if (this.currentQuestionIndex == this.questions.length) {
      this.submitQuiz();
    }
  }

  submitQuiz() {
    this.endTime = new Date().toISOString();
    const payload = {
      playerId: this.playerId,
      genre: this.genre,
      difficulty: this.difficulty,
      startTime: this.startTime,
      endTime: this.endTime,
      answers: this.answers
    };

    this.apiService.submitQuiz(payload)
      .subscribe({
        next: (success) => {
          console.log(success);
          this.score = success.score;
          this.totalQuestions = success.totalQuestions;
          this.achievement = success.achievement;
          this.reviewAnswer = success.answers.map((ans: any) => {
            const qt = this.questions.find(q => q.id == ans.questionId);
            return {
              questionText: qt?.questionText,
              answer: ans.answer,
              isCorrect: ans.isCorrect
            };
          });
          console.log(this.reviewAnswer);
          this.quizCompleted = true;
        }
      });

    const activityEvent: PlayerActivityEvent = { playerId: this.playerId!, userActivityType: 'QUIZ_ENDED', achievement: '' };
    this.apiService.sendPlayerActivityEvent(activityEvent).subscribe();
  }

  getRandomBackground(): string {
    return this.backgrounds[this.currentQuestionIndex % this.backgrounds.length];
  }

  doneButton() {
    this.router.navigate(['/main']);
  }

}