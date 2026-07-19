import api from './axios';
import type { AuthResponse, UserMe } from '../types';

export const authApi = {
  login: async (username: string, password: string): Promise<AuthResponse> => {
    const response = await api.post('/api/auth/login', { username, password });
    return response.data;
  },

  refreshToken: async (refreshToken: string): Promise<AuthResponse> => {
    const response = await api.post('/api/auth/refresh', { refreshToken });
    return response.data;
  },

  changePassword: async (currentPassword: string, newPassword: string): Promise<void> => {
    await api.post('/api/auth/change-password', { currentPassword, newPassword });
  },

  getMe: async (): Promise<UserMe> => {
    const response = await api.get('/api/auth/me');
    return response.data;
  },

  logout: async (): Promise<void> => {
    await api.post('/api/auth/logout');
  },
};
