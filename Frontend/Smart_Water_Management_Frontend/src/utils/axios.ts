import axios from 'axios';

const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080/api',
    timeout: 10000
});

// Add a request interceptor
axiosInstance.interceptors.request.use(
    (config) => {
        const user = JSON.parse(localStorage.getItem('user') || '{}');
        if (user.email && localStorage.getItem('token')) {
            const token = localStorage.getItem('token') || '';
            // For Bearer token auth
            config.headers.Authorization = `Bearer ${token}`;
            // For Basic auth
            config.auth = {
                username: user.email,
                password: token
            };
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// Add a response interceptor
axiosInstance.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401) {
            // Handle unauthorized error (e.g., redirect to login)
            localStorage.removeItem('token');
            window.location.href = '/auth/login';
        }
        return Promise.reject(error);
    }
);

export default axiosInstance;
