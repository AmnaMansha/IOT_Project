<script setup lang="ts">
import { ref, onMounted } from 'vue';
import axios from 'axios';

// Tabs
const tab = ref('1');

// API Data
const temperature = ref<number | null>(null);
const humidity = ref<number | null>(null);

// Your Basic Auth credentials
const basicAuthCredentials = {
  username: localStorage.getItem('email') || '',
  password: localStorage.getItem('password') || ''
};

// Fetch Temperature and Humidity Data
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
      temperature.value = tempResponse.data.data.temperature; // Adjust based on your API response
    }

    if (humidResponse.data) {
      humidity.value = humidResponse.data.data.humidity; // Adjust based on your API response
    }
  } catch (error) {
    console.error('Failed to fetch environment data:', error);
  }
};

// Call API when the component is mounted
onMounted(() => {
  fetchEnvironmentData();
});
</script>

<template>
  <v-card elevation="0" class="bg-primary overflow-hidden bubble-shape bubble-primary-shape mb-5">
    <v-card-text>
      <div class="d-flex align-start mb-3">
        <div class="ml-auto z-1">
          <v-tabs v-model="tab" class="theme-tab" density="compact" align-tabs="end">
            <v-tab value="1" hide-slider color="transparent">Temperature</v-tab>
            <v-tab value="2" hide-slider color="transparent">Humidity</v-tab>
          </v-tabs>
        </div>
      </div>
      <v-tabs-window v-model="tab" class="z-1">
        <v-tabs-window-item value="1">
          <v-row>
            <v-col cols="12" class="text-center">
              <h2 class="text-h1 font-weight-medium text-white">
                {{ temperature !== null ? temperature + '°C' : 'Loading...' }}
              </h2>
              <span class="text-subtitle-1 text-medium-emphasis text-white">Current Temperature</span>
            </v-col>
          </v-row>
        </v-tabs-window-item>
        <v-tabs-window-item value="2">
          <v-row>
            <v-col cols="12" class="text-center">
              <h2 class="text-h1 font-weight-medium text-white">
                {{ humidity !== null ? humidity + '%' : 'Loading...' }}
              </h2>
              <span class="text-subtitle-1 text-medium-emphasis text-white">Current Humidity</span>
            </v-col>
          </v-row>
        </v-tabs-window-item>
      </v-tabs-window>
    </v-card-text>
  </v-card>
</template>
