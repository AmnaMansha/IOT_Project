<script setup lang="ts">
import { ref, onMounted } from 'vue';
import axios from 'axios';

// Environment data
const temperature = ref(0);
const humidity = ref(0);

// Your Basic Auth credentials
const basicAuthCredentials = {
  username: localStorage.getItem('email') || '' ,
  password: localStorage.getItem('password') || ''
};

// Fetch temperature and humidity data
const fetchEnvironmentData = async () => {
  try {
    const [tempResponse, humidResponse] = await Promise.all([
      axios.get('http://localhost:8080/api/temperature/current', {
        auth: {
          username: basicAuthCredentials.username,
          password: basicAuthCredentials.password
        }
      }),
      axios.get('http://localhost:8080/api/humidity/current', {
        auth: {
          username: basicAuthCredentials.username,
          password: basicAuthCredentials.password
        }
      })
    ]);

    if (tempResponse.data) {
      temperature.value = tempResponse.data.data.temperature;
      console.log('Temperature:', temperature.value);
    }
    if (humidResponse.data) {
      humidity.value = humidResponse.data.data.humidity;
    }
  } catch (error) {
    console.error('Failed to fetch environment data:', error);
  }
};

// Call the API when the component is mounted
onMounted(() => {
  fetchEnvironmentData();
});

// Function to get color based on temperature level
const getTemperatureColor = () => {
  if (temperature.value <= 15) {
    return '#00E396';  // Green for Low Temperature
  } else if (temperature.value <= 30) {
    return '#FFB74D';  // Orange for Medium Temperature
  } else {
    return '#FF4560';  // Red for High Temperature
  }
};
</script>

<template>
  <v-card elevation="0" class="bg-primary overflow-hidden bubble-shape bubble-primary-shape">
    <v-card-text>
      <div class="d-flex align-start mb-3">
        <v-row>
          <!-- Temperature Gauge -->
          <v-col cols="12" sm="6">
            <v-card>
              <v-card-title>Temperature</v-card-title>
              <v-card-text class="text-center">
                <div class="text-h3 mb-4">{{ temperature }}°C</div>
                <apexchart
                  type="radialBar"
                  height="200"
                  
                  :options="{
                    plotOptions: {
                      radialBar: {
                        startAngle: -135,
                        endAngle: 135,
                      }
                    },
                    labels: ['Temp'],
                    title: {
                      text: 'Temperature Level',
                      align: 'center',
                    },
                    colors: [getTemperatureColor()] // Apply color based on temperature
                  }"
                  :series="[temperature]"
                />
              </v-card-text>
            </v-card>
          </v-col>

          <!-- Humidity Gauge -->
          <v-col cols="12" sm="6">
            <v-card>
              <v-card-title>Humidity</v-card-title>
              <v-card-text class="text-center">
                <div class="text-h3 mb-4">{{ humidity }}%</div>
                <apexchart
                  type="radialBar"
                  height="200"
                  :options="{
                    plotOptions: {
                      radialBar: {
                        startAngle: -135,
                        endAngle: 135,
                      }
                    },
                    labels: ['Humidity'],
                    title: {
                      text: 'Humidity Level',
                      align: 'center',
                    },
                  }"
                  :series="[humidity]"
                />
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>
      </div>
    </v-card-text>
  </v-card>
</template>
