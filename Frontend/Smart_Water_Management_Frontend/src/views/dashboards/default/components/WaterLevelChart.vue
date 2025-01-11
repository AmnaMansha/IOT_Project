<script setup lang="ts">
import { ref, onMounted } from 'vue';
import axios from '@/plugins/axios';

const waterLevel = ref(0);
const chartData = ref([]);

const updateChart = (data) => {
  chartData.value = [{
    name: 'Water Level',
    data: [data.level]
  }];
};

const fetchWaterLevel = async () => {
  try {
    const response = await axios.get('/waterlevel/current');
    if (response.data.status === 'success') {
      waterLevel.value = response.data.level;
      // updateChart(response.data.data);
      waterLevel.value = response.data.level;
      // updateChart(response.data.data);
      console.log("water data",response.data.data);
    }
  } catch (error) {
    console.error('Failed to fetch water level:', error);
  }
};

onMounted(() => {
  fetchWaterLevel();
});
</script>

<template>
  <div>
    <v-row>
      <v-col cols="12" md="6">
        <div class="text-h3 mb-4">{{ waterLevel }}%</div>
        <apexchart
          type="line"
          height="350"
          :options="chartOptions"
          :series="chartData"
        />
      </v-col>
    </v-row>
  </div>
</template> 