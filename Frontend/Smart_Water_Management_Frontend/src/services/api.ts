import axiosInstance from '@/utils/axios';

export const waterLevelApi = {
  getCurrentLevel: () => axiosInstance.get('/waterlevel/current'),
  getHistoricalData: (startDate: string, endDate: string) => 
    axiosInstance.get(`/waterlevel?startDate=${startDate}&endDate=${endDate}`)
};

export const environmentApi = {
  getCurrentTemperature: () => axiosInstance.get('/temperature/current'),
  getCurrentHumidity: () => axiosInstance.get('/humidity/current'),
  getHistoricalData: (type: 'temperature' | 'humidity', startDate: string, endDate: string) =>
    axiosInstance.get(`/${type}?startDate=${startDate}&endDate=${endDate}`)
};

export const pumpApi = {
  getStatus: () => axiosInstance.get('/pump/status'),
  control: (status: boolean) => axiosInstance.post(`/pump/control?status=${status}`)
};

export const alertApi = {
  getActive: () => axiosInstance.get('/alerts/active'),
  create: (alert: {
    type: string;
    severity: string;
    message: string;
  }) => axiosInstance.post('/alerts', alert)
};

export const userApi = {
  getUsers: () => axiosInstance.get('/auth/users'),
  deleteUser: (userId: string) => axiosInstance.delete(`/auth/users/${userId}`),
  updateUserRole: (userId: string, role: string) => 
    axiosInstance.put(`/auth/users/${userId}/role`, { role })
};
