import { LoginRequest, LoginResponse, SignupRequest } from '@/types/auth';
import { apiClient } from './apiClient';

export const AuthAPI = {
  signup: async (formData: SignupRequest) => {
    const { data } = await apiClient.post('/users/signup', formData);
    return data;
  },

  login: async (formData: LoginRequest) => {
    const { data } = await apiClient.post<LoginResponse>('/users/login', formData);
    return data;
  },
};
