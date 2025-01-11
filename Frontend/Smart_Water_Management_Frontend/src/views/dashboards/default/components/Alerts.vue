<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';

// Import icons
import { ChevronUpIcon, ChevronDownIcon } from 'vue-tabler-icons';

// Base URL and Basic Auth credentials
const baseUrl = 'http://localhost:8080'; // Replace with the actual base URL
const email = localStorage.getItem('email') || '';
const password = localStorage.getItem('password') || '';

// Alerts data and loading state
const alerts = ref([]);
const loading = ref(false);

// Axios instance with Basic Auth
const axiosInstance = axios.create({
  baseURL: baseUrl,
  auth: {
    username: email,
    password: password,
  },
});

// Fetch active alerts from the API
const fetchAlerts = async () => {
  try {
    loading.value = true;
    const response = await axiosInstance.get('/api/alerts/active');
    if (Array.isArray(response.data.data)) {
      alerts.value = response.data.data;
      processAlertData(response.data.data);
    }
  } catch (error) {
    console.error('Failed to fetch alerts:', error);
  } finally {
    loading.value = false;
  }
};

// Process alert data by severity
const processAlertData = (alertsData: any[]) => {
  const severityCounts = { LOW: 0, MEDIUM: 0, HIGH: 0 };
  alertsData.forEach(alert => {
    if (alert.severity in severityCounts) {
      severityCounts[alert.severity]++;
    }
  });

  // Update chart series data with counts based on severity
  lineChart1.value.series[0].data = [
    severityCounts.LOW,
    severityCounts.MEDIUM,
    severityCounts.HIGH
  ];
};

// Fetch alerts when the component is mounted
onMounted(() => {
  fetchAlerts();
});

// Chart options (static example for demo purposes)
const chartOptions1 = computed(() => ({
  chart: {
    type: 'area',
    height: 95,
    fontFamily: `inherit`,
    foreColor: '#a1aab2',
    sparkline: {
      enabled: true,
    },
  },
  colors: ['#5e35b1'],
  dataLabels: {
    enabled: false,
  },
  stroke: {
    curve: 'smooth',
    width: 1,
  },
  tooltip: {
    theme: 'dark',
    fixed: {
      enabled: false,
    },
    x: {
      show: false,
    },
    y: {
      title: {
        formatter: () => 'Alert ',
      },
    },
    marker: {
      show: false,
    },
  },
}));

const lineChart1 = ref({
  series: [
    {
      data: [0, 15, 10, 50, 30, 40, 25], // Default dummy data, will be updated
    },
  ],
});
</script>

<template>
  <v-card elevation="0">
    <v-card variant="outlined">
      <v-card-text>
        <div class="d-flex align-center">
          <h4 class="text-h4 mt-1">Active Alerts</h4>
          <v-btn v-if="loading" color="primary" size="small" rounded loading class="ml-auto"></v-btn>
        </div>

        <v-card class="bg-lightsecondary mt-5">
          <div class="pa-5">
            <div class="d-flex align-start justify-space-between">
              <div>
                <h6 class="text-secondary text-h5">Alert Overview</h6>
                <span class="text-subtitle-2 text-medium-emphasis font-weight-bold">Real-time Data</span>
              </div>
              <h4 class="text-h4">Alerts Count: {{ alerts.length }}</h4>
            </div>
          </div>
          <div class="pa-5">
            <apexchart type="area" height="95" :options="chartOptions1" :series="lineChart1.series"> </apexchart>
          </div>
        </v-card>

        <div class="mt-4">
          <perfect-scrollbar v-bind:style="{ height: '270px' }">
            <v-list lines="two" class="py-0">
              <v-list-item
                v-for="(alert, i) in alerts"
                :key="alert.id"
                :value="alert"
                color="secondary"
                rounded="sm"
              >
                <template v-slot:append>
                  <div
                    :class="{
                      'bg-lightsuccess': alert.severity === 'LOW',
                      'bg-warning': alert.severity === 'MEDIUM',
                      'bg-lighterror': alert.severity === 'HIGH',
                    }"
                    class="rounded-sm d-flex align-center justify-center ml-3"
                    style="width: 20px; height: 20px"
                  >
                    <ChevronUpIcon
                      stroke-width="1.5"
                      width="20"
                      :class="{
                        'text-success': alert.severity === 'LOW',
                        'text-warning': alert.severity === 'MEDIUM',
                        'text-error': alert.severity === 'HIGH',
                      }"
                    />
                  </div>
                </template>
                <div class="d-inline-flex align-center justify-space-between w-100">
                  <div>
                    <h6 class="text-subtitle-1 text-medium-emphasis font-weight-bold">
                      {{ alert.type }}
                    </h6>
                    <span class="text-subtitle-2">{{ alert.message }}</span>
                  </div>

                  <div class="ml-auto text-subtitle-2 text-medium-emphasis">
                    {{ new Date(alert.timestamp).toLocaleString() }}
                  </div>
                </div>
              </v-list-item>
            </v-list>
          </perfect-scrollbar>

          <div class="text-center mt-3">
            <v-btn color="primary" variant="text">View All</v-btn>
          </div>
        </div>
      </v-card-text>
    </v-card>
  </v-card>
</template>
