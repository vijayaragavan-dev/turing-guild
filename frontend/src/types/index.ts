export interface User {
  id: number;
  batchNumber?: string;
  email?: string;
  fullName: string;
  role: 'STUDENT' | 'ADMIN';
  isActive: boolean;
  firstLogin: boolean;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  requiresPasswordChange?: boolean;
  role?: string;
  username?: string;
}

export interface UserMe {
  id: number;
  fullName: string;
  email: string;
  batchNumber?: string;
  role: string;
  isActive: boolean;
  firstLogin: boolean;
}

export interface Event {
  id: number;
  title: string;
  description?: string;
  category: 'PROGRAMMING' | 'APTITUDE' | 'VERBAL' | 'CORE_CS';
  status: 'DRAFT' | 'PUBLISHED' | 'CLOSED' | 'DELETED';
  durationMinutes: number;
  totalMarks: number;
  startTime?: string;
  endTime?: string;
  createdBy?: string;
  createdAt?: string;
  questions?: Question[];
}

export interface Question {
  id: number;
  questionType: QuestionType;
  questionText: string;
  marks: number;
  orderIndex: number;
  codingTemplate?: string;
  expectedOutput?: string;
  sampleInput?: string;
  sampleOutput?: string;
  options?: Option[];
}

export type QuestionType =
  | 'MCQ' | 'CODING' | 'DEBUGGING' | 'OUTPUT_PREDICTION'
  | 'FILL_MISSING_CODE' | 'LOGICAL' | 'NUMERICAL' | 'FILL_BLANKS' | 'MATCH';

export interface Option {
  id: number;
  optionText: string;
  orderIndex: number;
}

export interface Submission {
  id: number;
  userId: number;
  userBatchNumber?: string;
  eventId: number;
  eventTitle?: string;
  status: 'IN_PROGRESS' | 'SUBMITTED' | 'EVALUATED';
  totalScore: number;
  submittedAt?: string;
  createdAt?: string;
  answers?: Answer[];
}

export interface Answer {
  id: number;
  questionId: number;
  answerText?: string;
  isCorrect?: boolean;
  scoreAwarded: number;
  feedback?: string;
}

export interface LeaderboardEntry {
  rank: number;
  userId: number;
  userBatchNumber: string;
  userFullName: string;
  eventId: number;
  eventTitle: string;
  totalScore: number;
}

export interface Student {
  id: number;
  batchNumber: string;
  email: string;
  fullName: string;
  isActive: boolean;
  firstLogin: boolean;
  createdAt?: string;
}

export interface Admin {
  id: number;
  fullName: string;
  email: string;
  isActive: boolean;
  firstLogin: boolean;
  createdAt?: string;
  updatedAt?: string;
}
