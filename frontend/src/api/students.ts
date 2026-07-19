import api from './axios';
import type { Student } from '../types';

export const studentsApi = {
  getAllStudents: async (): Promise<Student[]> => {
    const response = await api.get('/api/admin/students');
    return response.data;
  },

  getStudentById: async (id: number): Promise<Student> => {
    const response = await api.get(`/api/admin/students/${id}`);
    return response.data;
  },

  createStudent: async (data: Omit<Student, 'id' | 'isActive' | 'firstLogin' | 'createdAt'> & { password: string }): Promise<Student> => {
    const response = await api.post('/api/admin/students', data);
    return response.data;
  },

  updateStudent: async (id: number, data: Partial<Student>): Promise<Student> => {
    const response = await api.put(`/api/admin/students/${id}`, data);
    return response.data;
  },

  resetPassword: async (id: number): Promise<void> => {
    await api.post(`/api/admin/students/${id}/reset-password`);
  },

  deleteStudent: async (id: number): Promise<void> => {
    await api.delete(`/api/admin/students/${id}`);
  },
};
