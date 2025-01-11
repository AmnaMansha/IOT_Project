import { defineStore } from 'pinia';
import axiosInstance from '@/utils/axios';
import { router } from '@/router';

interface User {
  id: number;
  name: string;
  email: string;
  role: 'ADMIN' | 'USER';
}

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: null as User | null,
    token: localStorage.getItem('token'),
    email: localStorage.getItem('email'),
    password: localStorage.getItem('password'),
  }),

  getters: {
    isAuthenticated: (state) => !!state.token,
    isAdmin: (state) => state.user?.role === 'ADMIN',
  },

  actions: {
    async login(credentials: { email: string; password: string }) {
      try {
        const response = await axiosInstance.post('/auth/login', credentials);
        
        if (response.data.status === 'success') {
          this.token = response.data.token || new Date().getTime().toString();
          this.user = response.data.data;
          
          if (this.token) {
            localStorage.setItem('token', this.token);
            localStorage.setItem('email', credentials.email);
            localStorage.setItem('password', credentials.password);
            // Set the token in axios default headers
            axiosInstance.defaults.headers.common['Authorization'] = `Bearer ${this.token}`;
          }
          
          if (this.user && this.user.role === 'ADMIN') {
            router.push('/main/dashboard/default');
          } else if (this.user) {
            router.push('/main/user-dashboard');
          } 
          
          return { success: true };
        }
        return { success: false, message: 'Login failed' };
      } catch (error) {
        console.error('Login error:', error);
        return { message: error.response?.data?.message || 'Login failed' };
      }
    },

    // Add register action
    async register(credentials: { name: string; email: string; password: string; role: string }) {
      try {
        const response = await axiosInstance.post('/auth/register', credentials);

        if (response.data.status === 'success') {
          return { success: true };
        } else {
          return { success: false, message: response.data.message || 'Registration failed' };
        }
      } catch (error) {
        console.error('Registration error:', error);
        return { success: false, message: error.response?.data?.message || 'Registration failed' };
      }
    },

    logout() {
      this.user = null;
      this.token = null;
      this.email = null;
      this.password = null;

      // Remove stored data from localStorage
      localStorage.removeItem('email');
      localStorage.removeItem('password');
      localStorage.removeItem('token');
      delete axiosInstance.defaults.headers.common['Authorization'];
      router.push('/auth/login');
    }
  },
});
