<script setup lang="ts">
import { ref, onMounted } from 'vue';
import axios from 'axios';

// Alerts data
const alerts = ref<Array<{
  id: number;
  type: string;
  severity: string;
  message: string;
  isResolved: boolean;
  timestamp: string;
}>>([]);

// Your Basic Auth credentials
const basicAuthCredentials = {
  username: localStorage.getItem('email') || '',
  password: localStorage.getItem('password') || ''
};

// Fetch alerts
const fetchAlerts = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/alerts/active', {
      auth: {
        username: basicAuthCredentials.username,
        password: basicAuthCredentials.password,
      },
    });
    alerts.value = response.data.data;
  } catch (error) {
    console.error('Failed to fetch alerts:', error);
  }
};

// Fetch data on component mount
onMounted(() => {
  fetchAlerts();
});
</script>

<template>
  <div>
    <!-- Display alerts -->
    <div v-for="alert in alerts" :key="alert.id">
      <v-card
        elevation="0"
        :class="{
          'bg- bubble-warning': alert.severity === 'LOW',
          'bg-primary bubble-primary': alert.severity === 'MEDIUM',
          'bg-error bubble-primary': alert.severity === 'HIGH',
        }"
        class="overflow-hidden bubble-shape-sm mb-6"
      >
      <!-- <v-card elevation="0" class="bg-primary overflow-hidden bubble-shape-sm bubble-primary mb-6">
    <v-card-text class="pa-5">
      <div class="d-flex align-center ga-4">
        <v-btn color="darkprimary" icon rounded="sm" variant="flat">
          <TableIcon stroke-width="1.5" width="25" />
        </v-btn>
        <div>
          <h4 class="text-h4 font-weight-medium">{{ alert.message }}</h4>
          <span class="text-subtitle-2 text-medium-emphasis text-white">   Timestamp: {{ new Date(alert.timestamp).toLocaleString() }}</span>
        </div>
      </div>
    </v-card-text>
  </v-card> -->
        <v-card-text class="pa-5">
          <div class="d-flex align-center ga-4">
            <v-btn
              :color="alert.severity === 'LOW' ? 'primary' : alert.severity === 'MEDIUM' ? 'success' : 'error'"
              icon
              rounded="sm"
              variant="flat"
            >
              <TableIcon stroke-width="1.5" width="25" />
            </v-btn>
            <div>
              <h4 class="text-h4 font-weight-medium">{{ alert.message }}</h4>
              <span
                class="text-subtitle-2"
                :class="{
                  'text-medium-emphasis text-white': alert.severity === 'LOW',
                  'text-light-emphasis text-white': alert.severity === 'MEDIUM',
                  'text-light-emphasis text-danger': alert.severity === 'HIGH',
                }"
              >
                Timestamp: {{ new Date(alert.timestamp).toLocaleString() }}
              </span>
            </div>
          </div>
        </v-card-text>
      </v-card>
     
    </div>
  </div>
</template>

<style scoped>
/* Bubble Styling for Cards */
.bg-primary.bubble-primary {
  background: radial-gradient(circle at 20% 20%, #ffeb3b, transparent 70%);
}

.bg-success.bubble-success {
  background: radial-gradient(circle at 20% 20%, #4caf50, transparent 70%);
}

.bg-danger.bubble-danger {
  background: radial-gradient(circle at 20% 20%, #f44336, transparent 70%);
}

/* Bubble shape class */
.bubble-shape-sm {
  border-radius: 16px;
  overflow: hidden;
}
</style>
