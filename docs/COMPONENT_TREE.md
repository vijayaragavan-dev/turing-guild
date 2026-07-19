# Frontend Component Tree

```
src/
├── main.tsx
├── App.tsx
├── index.css
├── api/
│   ├── axios.ts              # Axios instance with interceptors
│   ├── auth.ts               # Auth API calls
│   ├── students.ts           # Student management API
│   ├── events.ts             # Event API
│   ├── submissions.ts        # Submission API
│   └── leaderboard.ts        # Leaderboard API
├── components/
│   ├── ui/                   # shadcn/ui components
│   │   ├── button.tsx
│   │   ├── input.tsx
│   │   ├── card.tsx
│   │   ├── dialog.tsx
│   │   ├── table.tsx
│   │   ├── badge.tsx
│   │   ├── select.tsx
│   │   ├── tabs.tsx
│   │   └── toast.tsx
│   ├── layout/
│   │   ├── Navbar.tsx
│   │   ├── Sidebar.tsx
│   │   └── DashboardLayout.tsx
│   └── shared/
│       ├── ProtectedRoute.tsx
│       ├── LoadingSpinner.tsx
│       └── DataTable.tsx
├── pages/
│   ├── auth/
│   │   ├── StudentLogin.tsx
│   │   ├── AdminLogin.tsx
│   │   └── ChangePassword.tsx
│   ├── student/
│   │   ├── Dashboard.tsx
│   │   ├── EventList.tsx
│   │   ├── EventDetail.tsx
│   │   ├── SubmissionPage.tsx
│   │   ├── Results.tsx
│   │   └── Leaderboard.tsx
│   └── admin/
│       ├── Dashboard.tsx
│       ├── StudentManagement.tsx
│       ├── EventManagement.tsx
│       ├── EventBuilder.tsx
│       ├── SubmissionReview.tsx
│       ├── ResultsExport.tsx
│       └── LeaderboardAdmin.tsx
├── hooks/
│   ├── useAuth.ts
│   └── useToast.ts
├── lib/
│   └── utils.ts              # cn() helper, formatters
├── schemas/
│   ├── auth.ts               # Zod schemas
│   ├── student.ts
│   ├── event.ts
│   └── submission.ts
└── types/
    └── index.ts              # TypeScript interfaces
```

# Backend Package Structure

```
src/main/java/com/turingguild/tgms/
├── config/
│   ├── SecurityConfig.java
│   ├── CorsConfig.java
│   ├── OpenApiConfig.java
│   └── FlywayConfig.java
├── security/
│   ├── JwtTokenProvider.java
│   ├── JwtAuthenticationFilter.java
│   ├── CustomUserDetailsService.java
│   └── RateLimiterFilter.java
├── controller/
│   ├── AuthController.java
│   ├── StudentController.java
│   ├── EventController.java
│   ├── StudentEventController.java
│   ├── SubmissionController.java
│   ├── AdminSubmissionController.java
│   ├── LeaderboardController.java
│   └── ResultController.java
├── service/
│   ├── AuthService.java
│   ├── StudentService.java
│   ├── EventService.java
│   ├── SubmissionService.java
│   ├── EvaluationService.java
│   ├── LeaderboardService.java
│   └── ResultService.java
├── repository/
│   ├── UserRepository.java
│   ├── EventRepository.java
│   ├── QuestionRepository.java
│   ├── OptionRepository.java
│   ├── SubmissionRepository.java
│   ├── AnswerRepository.java
│   └── LeaderboardRepository.java
├── entity/
│   ├── User.java
│   ├── Event.java
│   ├── Question.java
│   ├── Option.java
│   ├── Submission.java
│   ├── Answer.java
│   ├── Leaderboard.java
│   └── enums/
│       ├── Role.java
│       ├── EventCategory.java
│       ├── EventStatus.java
│       ├── QuestionType.java
│       └── SubmissionStatus.java
├── dto/
│   ├── request/
│   │   ├── LoginRequest.java
│   │   ├── ChangePasswordRequest.java
│   │   ├── CreateStudentRequest.java
│   │   ├── UpdateStudentRequest.java
│   │   ├── CreateEventRequest.java
│   │   ├── UpdateEventRequest.java
│   │   ├── CreateQuestionRequest.java
│   │   ├── SubmitAnswersRequest.java
│   │   └── EvaluateSubmissionRequest.java
│   └── response/
│       ├── AuthResponse.java
│       ├── StudentResponse.java
│       ├── EventResponse.java
│       ├── QuestionResponse.java
│       ├── SubmissionResponse.java
│       ├── LeaderboardResponse.java
│       └── MessageResponse.java
├── exception/
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   ├── UnauthorizedException.java
│   └── BadRequestException.java
├── handler/
│   ├── QuestionHandler.java       # Strategy interface
│   ├── McqHandler.java
│   ├── CodingHandler.java
│   ├── DebuggingHandler.java
│   ├── OutputPredictionHandler.java
│   ├── FillMissingCodeHandler.java
│   ├── LogicalHandler.java
│   ├── NumericalHandler.java
│   ├── FillBlanksHandler.java
│   └── MatchHandler.java
└── util/
    └── PaginationUtil.java
```
