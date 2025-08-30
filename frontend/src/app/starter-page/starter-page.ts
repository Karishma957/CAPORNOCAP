import { NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { AuthenticationService } from '../services/authentication-service';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from '../services/api.service';

@Component({
  selector: 'app-starter-page',
  standalone: true,
  imports: [NgIf, FormsModule],
  templateUrl: './starter-page.html',
  styleUrls: ['./starter-page.css']
})
export class StarterPage {
  showAbout: boolean = false;
  showContact: boolean = false;
  showLogin: boolean = false;
  username: string = "";
  password: string = "";
  loginError: string = "";
  loginErrorPresent: boolean = false;

  constructor(private router: Router, private authenticationService: AuthenticationService, private apiService: ApiService) { }

  startGameButtonClick() {
    const isUserLoggedIn = false;//this.authenticationService.isUserLoggedIn();
    if (isUserLoggedIn) {
      console.log('User logged in, proceed to game');
    } else {
      this.showLogin = true;
    }
  }

  loginSubmit() {

    // this.authenticationService.setAuth(1, "me", "qwdcfasg");
    // this.router.navigate(['/main']);

    this.apiService.register(this.username, this.password).subscribe({
      next: (success) => {
        console.log(success);
        this.loginErrorPresent = false;
        this.authenticationService.setAuth(success.playerId, success.username, success.token);
        this.router.navigate(['/main']);
      },
      error: (error) => {
        console.log("yaaaaar");
        console.log(error);
        this.loginError = error.error?.error || error.message || "Idk man";
        this.loginErrorPresent = true;
      }
    });
  }

  showAboutButtonClick(toOpen: boolean): void {
    this.showAbout = toOpen;
  }

  showContactButtonClick(toOpen: boolean): void {
    this.showContact = toOpen;
  }

  closeLoginPopup() {
    this.showLogin = false;
    this.loginErrorPresent = false;
    this.username = "";
    this.password = "";
  }
}
