<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';
import DashboardCard from '@/components/shared/DashboardCard.vue';
import EnvironmentStats from './components/EnvironmentStats.vue';
import UserManagement from './components/UserManagement.vue';

import PumpControl from './components/PumpControl.vue';
import WaterLeakageMonitor from '../common/WaterLeakageMonitor.vue';
import Analysis from './components/Analysis.vue';
import WaterLevel from './components/WaterLevel.vue';
import Alerts from './components/Alerts.vue';

// Refresh interval for real-time data
const refreshInterval = ref(null);

onMounted(() => {
  // Set up periodic refresh
  refreshInterval.value = setInterval(() => {
    // Refresh data
  }, 30000);
});

onUnmounted(() => {
  if (refreshInterval.value) {
    clearInterval(refreshInterval.value);
  }
});
</script>

<template>
  <v-container fluid>
    <!-- Top Row: Real-time Monitoring -->
    <v-row>
      <!-- Water Level -->
      <v-col cols="12" md="6">
        <DashboardCard title="Water Level Monitoring">
          <!-- <WaterLevelChart /> -->
          <WaterLevel/>
        </DashboardCard>
      </v-col>

      <!-- Temperature & Humidity -->
      <v-col cols="12" md="6">
        <DashboardCard title="Environment Monitoring" class="mb-4">
          <EnvironmentStats />
          <WaterLeakageMonitor/>
        </DashboardCard>
       
      </v-col>

    </v-row>

    <!-- Middle Row: Controls & Alerts -->
    <v-row>
      <!-- Pump Control -->
      <v-col cols="12" md="6">
        <DashboardCard title="Pump Control">
          <!-- <PumpControl /> -->
          <PumpControl/>

        </DashboardCard>
      </v-col>

      <!-- Active Alerts -->
      <v-col cols="12" md="6">
        <DashboardCard title="System Alerts">
          <!-- <AlertsTable /> -->
          <Alerts/>
         
        </DashboardCard>
      </v-col>
    </v-row>

    <!-- Bottom Row: Management & Analytics -->
    <v-row>
      <!-- User Management -->
      <v-col cols="12" md="6">
        <DashboardCard title="User Management">
          <UserManagement />
          
        </DashboardCard>
      </v-col>

      <!-- Historical Data -->
      <v-col cols="12" md="6">
        <DashboardCard title="Historical Analytics">
          <!-- <HistoricalData /> -->
          <Analysis/>
        </DashboardCard>
      </v-col>
    </v-row>
  </v-container>
</template>