import api from './axios';
import type { Admin } from '../types';

export const adminsApi = {
  getAllAdmins: async (): Promise<Admin[]> => {
    const response = await api.get('/api/admin/users');
    return response.data;
  },

  getAdminById: async (id: number): Promise<Admin> => {
    const response = await api.get(`/api/admin/users/${id}`);
    return response.data;
  },

  createAdmin: async (data: { fullName: string; email: string; password: string; isActive?: boolean }): Promise<Admin> => {
    const response = await api.post('/api/admin/users', data);
    return response.data;
  },

  updateAdmin: async (id: number, data: Partial<Admin>): Promise<Admin> => {
    const response = await api.put(`/api/admin/users/${id}`, data);
    return response.data;
  },

  updateAdminStatus: async (id: number, isActive: boolean): Promise<Admin> => {
    const response = await api.patch(`/api/admin/users/${id}/status`, { isActive });
    return response.data;
  },

  resetPassword: async (id: number, newPassword: string): Promise<void> => {
    await api.patch(`/api/admin/users/${id}/reset-password`, { newPassword });
  },

  deleteAdmin: async (id: number): Promise<void> => {
    await api.delete(`/api/admin/users/${id}`);
  },
};
