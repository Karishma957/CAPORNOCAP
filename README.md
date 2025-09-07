# CapOrNoCap – Multiplayer Quiz

---

## 🎥 Live Demo

Watch the full demo on YouTube:  
👉 [CapOrNoCap – Accessibility in Action](https://youtu.be/DeBlgr1dnYI?feature=shared)

---

## Overview

**CapOrNoCap** is a multiplayer quiz platform where players choose genres and difficulty levels to play quiz rounds.
It uses **Spring Boot microservices, Angular frontend, MySQL, Kafka, and an AI module (RandomForestRegressor)** to provide:

* Real-time score tracking
* Leaderboards
* Achievements & profiles
* AI-powered personalized quiz recommendations
* Developers console to tract users interest and overall stats

---

## Architecture

### Frontend (Angular)

* Handles user actions: registration, login, quiz play, achievements, leaderboard
* Displays AI-based recommendations & developer metrics

### Backend (Spring Boot Microservices)

| Microservice                | Role                                                                      |
| --------------------------- | --------------------------------------------------------------------------|
| **AuthService**             | Handles authentication & JWT-based authorization                          |
| **PlayerService**           | Manages player registration, profile, achievements                        |
| **QuestionService**         | Stores & serves quiz questions (genre + difficulty)                       |
| **QuizService**             | Orchestrates quiz, validates answers, emits score events                  |
| **DeveloperConsoleService** | Aggregate player interests, recommendation accptance and game statistics  |

---

## AI Module (Python – RandomForestRegressor)

* Input: `user-activity-events`, `score-events`
* Model: **RandomForestRegressor** for predicting best-fit genres/difficulty
* Output: personalized quiz recommendations

---

## 📡 Event Streaming (Kafka)

**Consumer Groups (Java class `KafkaConsumerGroups`):**

* `capornocap-ai-group` → AI service
* `capornocap-analytics-group` → DeveloperConsoleService

**Topics:**

| Kafka Topic                   | Purpose                                | Producer                   | Consumer(s)                        |
| ----------------------------- | -------------------------------------- | -------------------------- | ---------------------------------- |
| `user-activity-events`        | Player activity (quiz start/finish)    | PlayerService, QuizService | DeveloperConsoleService            |
| `score-events`                | Score submission & achievements        | QuizService                | DeveloperConsoleService, AIService |

---

## Features

* ✅ Authentication & JWT authorization
* ✅ Quiz creation & play (genre + difficulty)
* ✅ Player achievements & profiles
* ✅ Developer Console with analytics dashboard created using SSE
* ✅ AI recommendations with RandomForestRegressor
* ✅ Kafka-based real-time events

---

## Metrics & Dashboard

**Developer Console**

* Online players
* Active quiz sessions
* Achievements unlocked
* Recommendations accepted

**Charts**

* Time series → logins/logouts/quizzes per minute
* Bar → scores grouped by (genre × difficulty)
* Top N → most played genres/difficulties
* Pie → achievement & recommendation distribution

---

## 👩‍💻 Author

* **Karishma Nair** – Developer

---
