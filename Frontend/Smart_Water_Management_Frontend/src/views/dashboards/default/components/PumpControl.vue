<!-- <script setup lang="ts">
import { ref, onMounted } from 'vue';
import axios from 'axios';

// API Base URL
const baseUrl = 'http://localhost:8080';

// Pump status and loading indicators
const pumpStatus = ref(false);
const loading = ref(false);
const changedBy = ref('');
const lastStatusChange = ref('');

// Axios instance with Basic Auth
const axiosInstance = axios.create({
  baseURL: baseUrl,
  auth: {
    username: localStorage.getItem('email') || '',
    password: localStorage.getItem('password') || ''
  },
});

// Function to fetch the current pump status
const fetchPumpStatus = async () => {
  try {
    const response = await axiosInstance.get('/api/pump/status');
    if (response.data) {
      pumpStatus.value = response.data.isRunning;
      changedBy.value = response.data.changedBy;
      lastStatusChange.value = response.data.lastStatusChange;
    }
  } catch (error) {
    console.error('Failed to fetch pump status:', error);
  }
};

// Function to toggle the pump's status
const togglePump = async () => {
  try {
    loading.value = true;
    const response = await axiosInstance.post(
      `/api/pump/control?status=${!pumpStatus.value}&username=${email}`
    );
    if (response.data) {
      pumpStatus.value = response.data.isRunning;
      changedBy.value = response.data.changedBy;
      lastStatusChange.value = response.data.lastStatusChange;
    }
  } catch (error) {
    console.error('Failed to control pump:', error);
  } finally {
    loading.value = false;
  }
};

// Fetch the pump status when the component is mounted
onMounted(() => {
  fetchPumpStatus();
});
</script>

<template>
  <v-card elevation="2" class="pa-4 mb-4">
    <v-card-title class="text-h6 font-weight-bold">Pump Status</v-card-title>
    <v-card-text>
      <div class="d-flex align-center justify-space-between mb-4">
        <div>
          <p class="text-subtitle-1">Status:</p>
          <p :class="pumpStatus ? 'text-success' : 'text-error'">
            {{ pumpStatus ? 'ON' : 'OFF' }}
          </p>
          <p>Changed By: {{ changedBy }}</p>
          <p>Last Changed: {{ new Date(lastStatusChange).toLocaleString() }}</p>
        </div>
        <v-btn 
          :loading="loading"
          color="primary" 
          @click="togglePump">
          {{ pumpStatus ? 'Turn OFF' : 'Turn ON' }}
        </v-btn>
      </div>
    </v-card-text>
  </v-card>
</template> -->
<script setup lang="ts">
import { ref, onMounted,computed } from 'vue';
import axios from 'axios';

// API Base URL
const baseUrl = 'http://localhost:8080';

// Pump status and loading indicators
const pumpStatus = ref(false);
const loading = ref(false);
const changedBy = ref('');
const lastStatusChange = ref('');
const email = localStorage.getItem('email') || '';

// Axios instance with Basic Auth
const axiosInstance = axios.create({
  baseURL: baseUrl,
  auth: {
    username: localStorage.getItem('email') || '',
    password: localStorage.getItem('password') || ''
  },
});

// Function to fetch the current pump status
const fetchPumpStatus = async () => {
  try {
    const response = await axiosInstance.get('/api/pump/status');
    if (response.data) {
      pumpStatus.value = response.data.isRunning;
      changedBy.value = response.data.changedBy;
      lastStatusChange.value = response.data.lastStatusChange;
    }
  } catch (error) {
    console.error('Failed to fetch pump status:', error);
  }
};

// Function to toggle the pump's status
const togglePump = async () => {
  try {
    loading.value = true;
    // If pump is currently ON, we'll turn it OFF and vice versa
    const newStatus = !pumpStatus.value;
    
    const response = await axiosInstance.post(
      `/api/pump/control?status=${newStatus}&username=${email}`
    );
    
    if (response.data) {
      // Update local state with the response from the server
      pumpStatus.value = response.data.isRunning;
      changedBy.value = response.data.changedBy;
      lastStatusChange.value = response.data.lastStatusChange;
    }
  } catch (error) {
    console.error('Failed to control pump:', error);
    // Optionally show an error message to the user
    alert('Failed to control pump. Please try again.');
  } finally {
    loading.value = false;
  }
};

// Computed property for button text
const buttonText = computed(() => {
  return loading.value ? 'Processing...' : (pumpStatus.value ? 'Turn OFF' : 'Turn ON');
});

// Computed property for status color
const statusColor = computed(() => {
  return pumpStatus.value ? 'success' : 'error';
});

// Fetch the pump status when the component is mounted
onMounted(() => {
  fetchPumpStatus();
});
</script>

<template>
  <v-card elevation="2" class="pa-4 mb-4">
    <v-card-title class="text-h6 font-weight-bold">Pump Status</v-card-title>
    <v-card-text>
      <div class="d-flex align-center justify-space-between mb-4">
        <div>
          <p class="text-subtitle-1">Status:</p>
          <p :class="`text-${statusColor}`">
            {{ pumpStatus ? 'ON' : 'OFF' }}
          </p>
          <p>Changed By: {{ changedBy }}</p>
          <p>Last Changed: {{ new Date(lastStatusChange).toLocaleString() }}</p>
        </div>
        <v-btn 
          :loading="loading"
          :color="pumpStatus ? 'error' : 'success'"
          :disabled="loading"
          @click="togglePump"
        >
          {{ buttonText }}
        </v-btn>
      </div>
    </v-card-text>
  </v-card>
</template>