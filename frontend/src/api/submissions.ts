import api from './axios';
import type { Submission, LeaderboardEntry } from '../types';

export const submissionsApi = {
  joinEvent: async (eventId: number): Promise<Submission> => {
    const response = await api.post(`/api/events/${eventId}/join`);
    return response.data;
  },

  getSubmission: async (id: number): Promise<Submission> => {
    const response = await api.get(`/api/submissions/${id}`);
    return response.data;
  },

  submitAnswers: async (submissionId: number, answers: { questionId: number; answerText: string }[]): Promise<Submission> => {
    const response = await api.post(`/api/submissions/${submissionId}/submit`, { answers });
    return response.data;
  },

  getMyResults: async (): Promise<Submission[]> => {
    const response = await api.get('/api/results/me');
    return response.data;
  },

  getSubmissionsByEvent: async (eventId: number): Promise<Submission[]> => {
    const response = await api.get('/api/admin/submissions', { params: { eventId } });
    return response.data;
  },

  getSubmissionById: async (id: number): Promise<Submission> => {
    const response = await api.get(`/api/admin/submissions/${id}`);
    return response.data;
  },

  evaluateSubmission: async (submissionId: number, answers: { answerId: number; scoreAwarded: number; feedback?: string }[]): Promise<Submission> => {
    const response = await api.put(`/api/admin/submissions/${submissionId}/evaluate`, { answers });
    return response.data;
  },

  getLeaderboard: async (eventId: number): Promise<LeaderboardEntry[]> => {
    const response = await api.get(`/api/leaderboard/${eventId}`);
    return response.data;
  },

  recomputeLeaderboard: async (eventId: number): Promise<void> => {
    await api.post(`/api/admin/leaderboard/${eventId}/recompute`);
  },
};
