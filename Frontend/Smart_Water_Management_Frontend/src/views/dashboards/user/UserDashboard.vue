<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';
import ShowWaterLevel from './components/ShowWaterLevel.vue';
import SensorData from './components/SensorData.vue';
import ActiveAlert from './components/ActiveAlert.vue';
import WaterLeakageMonitor from '../common/WaterLeakageMonitor.vue';
// import DashboardCard from '@/components/shared/DashboardCard.vue';
// import EnvironmentStats from './components/EnvironmentStats.vue';
// import UserManagement from './components/UserManagement.vue';

// import PumpControl from './components/PumpControl.vue';

// import Analysis from './components/Analysis.vue';
// import WaterLevel from './components/WaterLevel.vue';
// import Alerts from './components/Alerts.vue';

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
    <v-row class="pa-10">
      <!-- Water Level -->
      <v-col cols="12" md="6">
        <DashboardCard title="Water Level Monitoring">
         <ShowWaterLevel/>
         
        </DashboardCard>
      </v-col>

      <!-- Temperature & Humidity -->
      <v-col cols="12" md="6">
        <DashboardCard title="Environment Monitoring">
          <SensorData/>
          <ActiveAlert/>
        </DashboardCard>
      </v-col>
    </v-row>

    <!-- Middle Row: Controls & Alerts -->
    <v-row>
    
      <!-- Active Alerts -->
      <v-col cols="12" md="12">
        <DashboardCard title="System Alerts">
          <!-- <AlertsTable /> -->
       <WaterLeakageMonitor/>
         
        </DashboardCard>
      </v-col>
    </v-row>

    <!-- Bottom Row: Management & Analytics -->
    <v-row>
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