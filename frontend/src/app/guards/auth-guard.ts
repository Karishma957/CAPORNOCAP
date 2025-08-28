import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { AuthenticationService } from '../services/authentication-service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private authService: AuthenticationService, private router: Router) { }

  canActivate(): boolean | UrlTree {
    if (this.authService.isUserLoggedIn()) {
      return true;
    }
    return this.router.createUrlTree(['/']);
  }
}
