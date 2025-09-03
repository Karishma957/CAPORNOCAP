import { Component, OnInit } from '@angular/core';
import { DevConsoleService } from '../services/DevConsoleService';
import { DeveloperConsoleState } from '../model/DeveloperConsoleState';
import { ChartConfiguration } from 'chart.js';
import { NgChartsModule } from 'ng2-charts';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-developers-console-component',
  imports: [NgChartsModule, CommonModule],
  templateUrl: './developers-console-component.html',
  styleUrl: './developers-console-component.css'
})
export class DevelopersConsoleComponent implements OnInit {
  data: DeveloperConsoleState | null = null;

  lineData: ChartConfiguration<'line'>['data'] = { labels: [], datasets: [] };
  radarData: ChartConfiguration<'radar'>['data'] = { labels: [], datasets: [] };
  pieData: ChartConfiguration<'doughnut'>['data'] = { labels: [], datasets: [] };
  barData: ChartConfiguration<'bar'>['data'] = { labels: [], datasets: [] };

  constructor(private devConsoleService: DevConsoleService) { }

  ngOnInit(): void {
    console.log('[AnalyticsState] ngOnInit called');
    this.devConsoleService.start();
    this.devConsoleService.data$.subscribe(data => {
      this.data = data;
      if (data) this.updateChart(data);
    })
  }

  ngOnDestroy(): void {
    this.devConsoleService.stop();
  }

  private updateChart(data: DeveloperConsoleState) {
    const labels = data.timeSeriesByMinute.map(x => new Date(x.ts).toLocaleTimeString());
    const logins = data.timeSeriesByMinute.map(x => x.logins);
    const logouts = data.timeSeriesByMinute.map(x => x.logouts);
    const quizStarted = data.timeSeriesByMinute.map(x => x.quizStarted);

    this.lineData = {
      labels: labels,
      datasets: [
        {
          label: 'Logins', data: logins
        },
        {
          label: 'Logouts', data: logouts
        },
        {
          label: 'Quiz Started', data: quizStarted
        }
      ]
    }

    const scoreLabels = data.scoresByCombo.map(c => `${c.genre}-${c.difficulty}`);
    this.radarData = {
      labels: scoreLabels,
      datasets: [
        {
          label: 'Average Score',
          data: data.scoresByCombo.map(c => c.averageScore)
        }
      ]
    }

    if (data.achievementCount) {
      this.pieData = {
        labels: Object.keys(data.achievementCount),
        datasets: [{
          data: Object.values(data.achievementCount)
        }]
      }
    }

    this.barData = {
      labels: data.mostPlayed.map(m => `${m.genre}-${m.difficulty}`),
      datasets: [
        {
          label: 'Play Count',
          data: data.mostPlayed.map(m => m.plays)
        }
      ]
    }
  }

}
