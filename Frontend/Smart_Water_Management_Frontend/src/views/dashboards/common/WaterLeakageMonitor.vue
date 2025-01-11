<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue';
import axios from 'axios';
import { CircleArrowUpRightIcon } from 'vue-tabler-icons';

// Types
interface HumidityData {
  status: string;
  message: string;
  data: {
    id: number;
    humidity: number;
    timestamp: string;
  };
}

// State
const humidityData = ref<HumidityData[]>([]);
const timestamps = ref<string[]>([]);
const currentStatus = ref({
  status: 'loading',
  message: 'Checking humidity levels...',
  humidity: 0
});
const pollTimer = ref<number | null>(null);

// Constants
const HUMIDITY_THRESHOLD = 75.0;
const POLL_INTERVAL = 30000; // 30 seconds

// Your Basic Auth credentials
const basicAuthCredentials = {
  username: localStorage.getItem('email') || '',
  password: localStorage.getItem('password') || ''
};

// Fetch humidity data from both APIs
const fetchHumidityData = async () => {
  try {
    // First, fetch the humidity check for leakage
    const leakageResponse = await axios.get('http://localhost:8080/api/humidity/check-leakage', {
      auth: {
        username: basicAuthCredentials.username,
        password: basicAuthCredentials.password
      }
    });

    // Handle leakage status
    const leakageMessage = leakageResponse.data.message;
    const isLeakage = leakageMessage.includes('leakage detected'); // Logic to detect leakage

    // Then, fetch the current humidity data
    const humidityResponse = await axios.get('http://localhost:8080/api/humidity/current', {
      auth: {
        username: basicAuthCredentials.username,
        password: basicAuthCredentials.password
      }
    });

    const humidity = humidityResponse.data.data.humidity;
    const timestamp = humidityResponse.data.data.timestamp;

    currentStatus.value = {
      status: humidity > HUMIDITY_THRESHOLD ? 'error' : 'success',
      message: humidity > HUMIDITY_THRESHOLD ? 
        `Warning: High humidity detected (${humidity.toFixed(1)}%)` : 
        `Normal humidity levels (${humidity.toFixed(1)}%)`,
      humidity
    };

    // Add the new humidity data point
    humidityData.value.push({
      status: 'success',
      message: 'Data fetched successfully',
      data: { id: 0, humidity, timestamp }
    });
    timestamps.value.push(new Date(timestamp).toLocaleTimeString());

    // Keep only last 10 readings
    if (humidityData.value.length > 10) {
      humidityData.value.shift();
      timestamps.value.shift();
    }
  } catch (error) {
    console.error('Error fetching humidity data:', error);
    currentStatus.value = {
      status: 'error',
      message: 'Failed to fetch humidity data',
      humidity: 0
    };
  }
};

// Start/Stop polling functions remain the same
const startPolling = () => {
  fetchHumidityData();
  pollTimer.value = window.setInterval(fetchHumidityData, POLL_INTERVAL);
};

const stopPolling = () => {
  if (pollTimer.value) {
    window.clearInterval(pollTimer.value);
    pollTimer.value = null;
  }
};

// Chart options
const chartOptions = computed(() => ({
  chart: {
    type: 'line',
    height: 480,
    fontFamily: 'inherit',
    foreColor: '#a1aab2',
    toolbar: {
      show: true
    }
  },
  colors: ['#2196f3', '#ff9800'],
  stroke: {
    curve: 'smooth',
    width: [3, 2],
    dashArray: [0, 5]
  },
  responsive: [{
    breakpoint: 480,
    options: {
      legend: {
        position: 'bottom',
        offsetX: -10,
        offsetY: 0
      }
    }
  }],
  markers: {
    size: 4,
    colors: ['#2196f3', '#ff9800'],
    strokeColors: '#fff',
    strokeWidth: 2
  },
  xaxis: {
    type: 'category',
    categories: timestamps.value,
    labels: {
      style: {
        colors: '#a1aab2'
      }
    }
  },
  yaxis: {
    title: {
      text: 'Humidity Level (%)'
    },
    min: 0,
    max: 100,
    tickAmount: 5,
    labels: {
      style: {
        colors: '#a1aab2'
      }
    }
  },
  legend: {
    show: true,
    position: 'bottom',
    offsetY: 10,
    markers: {
      width: 16,
      height: 16,
      radius: 4
    },
    itemMargin: {
      horizontal: 15,
      vertical: 8
    }
  },
  grid: {
    borderColor: 'rgba(0,0,0,0.1)',
    strokeDashArray: 3
  },
  tooltip: {
    shared: true,
    intersect: false,
    theme: 'dark',
    y: {
      formatter: (value: number) => `${value.toFixed(1)}%`
    }
  }
}));

// Chart series
const chartSeries = computed(() => [
  {
    name: 'Humidity Level',
    data: humidityData.value.map(d => d.data.humidity)
  },
  {
    name: 'Threshold',
    data: Array(humidityData.value.length).fill(HUMIDITY_THRESHOLD)
  }
]);

// Lifecycle hooks remain the same
onMounted(() => {
  startPolling();
});

onUnmounted(() => {
  stopPolling();
});

const getStatusColor = computed(() => 
  currentStatus.value.status === 'success' ? 'success' : 'error'
);

const getAlertClass = computed(() => ({
  'alert-card': true,
  'alert-danger': currentStatus.value.status === 'error',
  'alert-success': currentStatus.value.status === 'success'
}));
</script>

<template>
  <v-card elevation="0" class="bg-lightsecondary overflow-hidden bubble-shape bubble-secondary-shape mt-5">
    <v-card-text>
      <!-- Header Section -->
      <div class="d-flex align-center justify-space-between mb-6">
        <div>
          <h2 class="text-h4 font-weight-medium mb-1">
            Water Leakage Monitor
            <CircleArrowUpRightIcon stroke-width="1.5" width="28" class="text-white" />
          </h2>
          <span class="text-subtitle-1 text-medium-emphasis text-secondary">
            Threshold: {{ HUMIDITY_THRESHOLD }}% humidity
          </span>
        </div>
      </div>

      <!-- Enhanced Status Card -->
      <v-card
        :class="getAlertClass"
        class="mb-6"
        rounded="sm"
       
      >
        <div class="alert-content">
          <div class="alert-icon">
            <v-icon
              :color="currentStatus.status === 'success' ? 'success' : 'error'"
              size="32"
            >
              {{ currentStatus.status === 'success' ? 'mdi-check-circle' : 'mdi-alert-circle' }}
            </v-icon>
          </div>
          <div class="alert-text">
            <div class="alert-title">{{ currentStatus.message }}</div>
            <div class="alert-subtitle">
              Last updated: {{ timestamps[timestamps.length - 1] || 'Never' }}
            </div>
          </div>
        </div>
      </v-card>

      <!-- Chart -->
      <div class="mt-4">
        <apexchart
          type="line"
          height="480"
          :options="chartOptions"
          :series="chartSeries"
        ></apexchart>
      </div>
    </v-card-text>
  </v-card>
</template>

<style scoped>
.bubble-shape {
  border-radius: 16px;
  position: relative;
  overflow: hidden;
}

.bubble-secondary-shape::before,
.bubble-secondary-shape::after {
  content: '';
  position: absolute;
  width: 210px;
  height: 210px;
  border-radius: 50%;
  background: linear-gradient(140.9deg, rgba(255, 255, 255, 0.1) -14.02%, rgba(255, 255, 255, 0.1) 70.50%);
}

.bubble-secondary-shape::before {
  top: -160px;
  right: -130px;
}

.bubble-secondary-shape::after {
  top: -30px;
  right: -180px;
  background: linear-gradient(210.04deg, rgba(255, 255, 255, 0.1) -50.94%, rgba(255, 255, 255, 0.1) 83.49%);
}

.alert-card {
  padding: 1.5rem;
  transition: all 0.3s ease;
  border: 1px solid transparent;
}

.alert-danger {
  background-color: floralwhite;
  border-color: rgba(244, 67, 54, 0.3);
}

.alert-success {
  background-color:floralwhite;
  border-color: rgba(76, 175, 80, 0.3);
}

.alert-content {
  display: flex;
  align-items: center;
}

.alert-icon {
  margin-right: 1rem;
}

.alert-text .alert-title {
  font-size: 1.25rem;
  font-weight: bold;
}

.alert-text .alert-subtitle {
  font-size: 0.875rem;
  color: #6c757d;
}
</style>
