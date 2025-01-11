<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import axios from 'axios'; // Import axios for API calls

// API integration
const waterLevels = ref([]); // Store fetched data
const timestamps = ref([]); // Store timestamps

// Your Basic Auth credentials
const basicAuthCredentials = {
  username: localStorage.getItem('email') || '',
  password: localStorage.getItem('password') || ''
};

// Fetch data from the API
const fetchWaterLevelData = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/waterlevel/current', {
      auth: {
        username: basicAuthCredentials.username,
        password: basicAuthCredentials.password
      }
    });
    const data = response.data.data;

    // Update the waterLevels and timestamps arrays
    waterLevels.value.push(data.level);
    timestamps.value.push(new Date(data.timestamp).toLocaleTimeString());
  } catch (error) {
    console.error('Error fetching water level data:', error);
  }
};

// Call the API when the component is mounted
onMounted(fetchWaterLevelData);

// Chart options
const chartOptions1 = computed(() => {
  return {
    chart: {
      type: 'bar',
      height: 480,
      fontFamily: `inherit`,
      foreColor: '#a1aab2',
      stacked: true
    },
    // colors: ['#eef2f6', '#1e88e5', '#5e35b1', '#ede7f6'],
    responsive: [
      {
        breakpoint: 480,
        options: {
          legend: {
            position: 'bottom',
            offsetX: -10,
            offsetY: 0
          }
        }
      }
    ],
    plotOptions: {
      bar: {
        horizontal: false,
        columnWidth: '20%'
      }
    },
    xaxis: {
      type: 'category',
      categories: timestamps.value // Use timestamps for the x-axis
    },
    legend: {
      show: true,
      fontFamily: `'Roboto', sans-serif`,
      position: 'bottom',
      offsetX: 20,
      labels: {
        useSeriesColors: false
      },
      markers: {
        width: 16,
        height: 16,
        radius: 5
      },
      itemMargin: {
        horizontal: 15,
        vertical: 8
      }
    },
    fill: {
      type: 'solid'
    },
    dataLabels: {
      enabled: false
    },
    grid: {
      show: true
    },
    tooltip: {
      theme: 'dark'
    }
  };
});

// Chart series
const lineChart1 = computed(() => {
  return {
    series: [
      {
        name: 'Water Level',
        data: waterLevels.value // Use waterLevels for the data
      }
    ]
  };
});

// Dropdown and items
const select = ref({ state: 'Today', abbr: 'FL' });
const items = [
  { state: 'Today', abbr: 'FL' },
  { state: 'This Month', abbr: 'GA' },
  { state: 'This Year', abbr: 'NE' }
];

</script>

<template>
  <v-card elevation="0" class="bg-secondary overflow-hidden bubble-shape bubble-secondary-shape">
    <v-card-text>
      <div class="d-flex align-start mb-6">
        <p>Check your water Level </p>
      
      </div>
      <v-row>
          <v-col cols="12" sm="9">
            <span class="text-subtitle-2 text-disabled font-weight-bold">Water Level</span>
            <h3 class="text-h3 mt-1">{{ waterLevels[waterLevels.length - 1] || 0 }}%</h3>
          </v-col>
         
        </v-row>
        <div class="mt-4">
          <apexchart
            type="bar"
            height="480"
            :options="chartOptions1"
            :series="lineChart1.series"
          ></apexchart>
        </div>
      <h2 class="text-h1 font-weight-medium">
        Water Level <a href="#"><CircleArrowUpRightIcon stroke-width="1.5" width="28" class="text-white" /> </a>
      </h2>
      <span class="text-subtitle-1 text-medium-emphasis text-white">Calculated</span>
    </v-card-text>
  </v-card>
</template>
