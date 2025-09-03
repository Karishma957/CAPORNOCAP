import { Routes } from '@angular/router';
import { StarterPage } from './starter-page/starter-page';
import { GameScreenComponent } from './game-screen-component/game-screen-component';
import { PlayScreenComponent } from './play-screen-component/play-screen-component';
import { AuthGuard } from './guards/auth-guard';
import { DevelopersConsoleComponent } from './developers-console-component/developers-console-component';

export const routes: Routes = [
    { path: '', component: StarterPage },
    { path: 'main', component: GameScreenComponent, canActivate: [AuthGuard] },
    { path: 'play', component: PlayScreenComponent, canActivate: [AuthGuard] },
    { path: 'dev-console', component: DevelopersConsoleComponent },
    { path: '**', redirectTo: '' }
];
