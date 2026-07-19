import { useState, useEffect, useCallback } from 'react';
import type { AuthResponse } from '../types';
import { authApi } from '../api/auth';

interface AuthState {
  isAuthenticated: boolean;
  role: string | null;
  username: string | null;
  requiresPasswordChange: boolean;
  loading: boolean;
}

export function useAuth() {
  const [authState, setAuthState] = useState<AuthState>({
    isAuthenticated: false,
    role: null,
    username: null,
    requiresPasswordChange: false,
    loading: true,
  });

  useEffect(() => {
    const token = localStorage.getItem('accessToken');
    const role = localStorage.getItem('userRole');
    const username = localStorage.getItem('username');

    setAuthState({
      isAuthenticated: !!token,
      role,
      username,
      requiresPasswordChange: false,
      loading: false,
    });
  }, []);

  const login = useCallback(async (username: string, password: string): Promise<AuthResponse> => {
    const response = await authApi.login(username, password);
    
    localStorage.setItem('accessToken', response.accessToken);
    localStorage.setItem('refreshToken', response.refreshToken);
    if (response.role) localStorage.setItem('userRole', response.role);
    if (response.username) localStorage.setItem('username', response.username);

    setAuthState({
      isAuthenticated: true,
      role: response.role || null,
      username: response.username || null,
      requiresPasswordChange: response.requiresPasswordChange ?? false,
      loading: false,
    });

    return response;
  }, []);

  const logout = useCallback(() => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('userRole');
    localStorage.removeItem('username');

    setAuthState({
      isAuthenticated: false,
      role: null,
      username: null,
      requiresPasswordChange: false,
      loading: false,
    });
  }, []);

  const changePassword = useCallback(async (currentPassword: string, newPassword: string) => {
    await authApi.changePassword(currentPassword, newPassword);
  }, []);

  return { ...authState, login, logout, changePassword };
}
