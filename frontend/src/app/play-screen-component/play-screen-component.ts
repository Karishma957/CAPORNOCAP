import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Question } from '../model/Question';
import { Answer } from '../model/Answer';
import { AuthenticationService } from '../services/authentication-service';
import { NgIf } from '@angular/common';
import { ApiService } from '../services/api.service';


@Component({
  selector: 'app-play-screen-component',
  standalone: true,
  imports: [NgIf],
  templateUrl: './play-screen-component.html',
  styleUrls: ['./play-screen-component.css']
})
export class PlayScreenComponent implements OnInit {
  genre: string = '';
  difficulty: string = '';
  playerName: string | null = null;
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
    this.playerName = this.authenticationService.getUsername();

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
    this.timer = 10;
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
      playerId: this.playerName,
      genre: this.genre,
      difficulty: this.difficulty,
      startTime: this.startTime,
      endTime: this.endTime,
      answers: this.answers
    };

    // this.quizService.submitQuiz(payload)
    //   .subscribe(result => {
    //     this.score = result.score;
    //     this.totalQuestions = result.totalQuestions;
    //     this.achievement = result.achievement || null;
    //     this.quizCompleted = true;
    //   });

    this.score = 5;
    this.totalQuestions = 10;
    this.quizCompleted = true;
    this.achievement = { title: "yoyoyooo", description: "asjfesdjgbsdjb jasbfjkabjbfjc jasbfjkasb sjabsfkbsb" };
    console.log('Dummy submit payload:', payload);
    // this.router.navigate(['/results'], { state: { payload } });
  }

  getRandomBackground(): string {
    return this.backgrounds[this.currentQuestionIndex % this.backgrounds.length];
  }

  doneButton() {
    this.router.navigate(['/main']);
  }

}