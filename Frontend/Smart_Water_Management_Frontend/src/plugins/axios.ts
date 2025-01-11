import axios from 'axios';
import type { App } from 'vue';


// Create axios instance
const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json'
  }
});

// Add request interceptor to add auth token
// Add Basic Auth credentials
const username = 'amm@gmail'; // Replace with your actual username
const password = 'amna123'; // Replace with your actual password
const basicAuth = btoa(`${username}:${password}`);
axiosInstance.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${basicAuth}`
  }
  return config
});

// Add response interceptor to handle errors
axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error('API Error:', error);
    return Promise.reject(error);
  }
);

export default {
  install: (app: App) => {
    app.config.globalProperties.$axios = axiosInstance;
  }
};

export { axiosInstance }; 