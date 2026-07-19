import api from './axios';
import type { Event, Question } from '../types';

export const eventsApi = {
  getPublishedEvents: async (): Promise<Event[]> => {
    const response = await api.get('/api/events');
    return response.data;
  },

  getEventById: async (id: number): Promise<Event> => {
    const response = await api.get(`/api/events/${id}`);
    return response.data;
  },

  getAllEvents: async (): Promise<Event[]> => {
    const response = await api.get('/api/admin/events');
    return response.data;
  },

  createEvent: async (data: Partial<Event>): Promise<Event> => {
    const response = await api.post('/api/admin/events', data);
    return response.data;
  },

  updateEvent: async (id: number, data: Partial<Event>): Promise<Event> => {
    const response = await api.put(`/api/admin/events/${id}`, data);
    return response.data;
  },

  publishEvent: async (id: number): Promise<Event> => {
    const response = await api.put(`/api/admin/events/${id}/publish`);
    return response.data;
  },

  closeEvent: async (id: number): Promise<Event> => {
    const response = await api.put(`/api/admin/events/${id}/close`);
    return response.data;
  },

  deleteEvent: async (id: number): Promise<void> => {
    await api.delete(`/api/admin/events/${id}`);
  },

  getEventQuestions: async (eventId: number): Promise<Question[]> => {
    const response = await api.get(`/api/admin/events/${eventId}/questions`);
    return response.data;
  },

  addQuestion: async (eventId: number, data: {
    questionType: string;
    questionText: string;
    marks?: number;
    orderIndex?: number;
    codingTemplate?: string;
    expectedOutput?: string;
    sampleInput?: string;
    sampleOutput?: string;
    options?: { optionText: string; isCorrect: boolean; orderIndex?: number }[];
  }): Promise<Question> => {
    const response = await api.post(`/api/admin/events/${eventId}/questions`, data);
    return response.data;
  },

  deleteQuestion: async (questionId: number): Promise<void> => {
    await api.delete(`/api/admin/questions/${questionId}`);
  },
};
