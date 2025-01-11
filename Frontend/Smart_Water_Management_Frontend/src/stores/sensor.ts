import { defineStore } from 'pinia';
import axios from 'axios';

export const useSensorStore = defineStore('sensor', {
  state: () => ({
    waterLevel: 0,
    temperature: 0,
    humidity: 0,
    pumpStatus: false,
    alerts: []
  }),

  actions: {
    async fetchCurrentData() {
      try {
        const [waterLevel, temp, humidity, pump] = await Promise.all([
          axios.get('http://localhost:8080/api/waterlevel/current'),
          axios.get('http://localhost:8080/api/temperature/current'),
          axios.get('http://localhost:8080/api/humidity/current'),
          axios.get('http://localhost:8080/api/pump/status')
        ]);

        this.waterLevel = waterLevel.data.level;
        this.temperature = temp.data.value;
        this.humidity = humidity.data.value;
        this.pumpStatus = pump.data.status;
      } catch (error) {
        console.error('Error fetching sensor data:', error);
      }
    },

    async togglePump() {
      try {
        const response = await axios.post('http://localhost:8080/api/pump/control', {
          status: !this.pumpStatus
        });
        this.pumpStatus = response.data.status;
        return { success: true };
      } catch (error) {
        return { success: false, message: 'Failed to toggle pump' };
      }
    }
  }
}); 