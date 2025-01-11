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
    const response = await axios.get('http://localhost:8080/api/waterlevel/all', {
      auth: {
        username: basicAuthCredentials.username,
        password: basicAuthCredentials.password
      }
    });
    const data = response.data.data; // Access the 'data' array in the API response

    // Update the waterLevels and timestamps arrays with API data
    waterLevels.value = data.map(record => record.level); // Extract water levels
    timestamps.value = data.map(record => new Date(record.timestamp).toLocaleTimeString()); // Extract timestamps
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
      fontFamily: `'Roboto', sans-serif`, // Use a clean, modern font
      foreColor: '#333', // Dark text color for better readability
      stacked: true
    },
    colors: ['#1e88e5', '#ff4081', '#8e24aa', '#4caf50'], // Enhanced color palette
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
        columnWidth: '55%', // Slightly wider columns for better visibility
        endingShape: 'rounded' // Rounded edges for a softer appearance
      }
    },
    xaxis: {
      type: 'category',
      categories: timestamps.value, // Use timestamps for the x-axis
      labels: {
        style: {
          colors: '#a1aab2', // Light text color for x-axis labels
          fontSize: '12px', // Smaller font size for clarity
        }
      }
    },
    legend: {
      show: true,
      fontFamily: `'Roboto', sans-serif`,
      position: 'bottom',
      offsetX: 20,
      labels: {
        useSeriesColors: false,
        fontSize: '12px',
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
      type: 'solid',
      opacity: 0.9 // Slight opacity for a cleaner look
    },
    dataLabels: {
      enabled: false // Disable data labels for a less cluttered chart
    },
    grid: {
      show: true,
      borderColor: '#e0e0e0', // Light grid color to not overwhelm the chart
      strokeDashArray: 5, // Dashed grid lines for a subtle effect
      xaxis: {
        lines: {
          show: true
        }
      },
      yaxis: {
        lines: {
          show: true
        }
      }
    },
    tooltip: {
      theme: 'dark', // Dark tooltip theme
      style: {
        fontSize: '12px', // Smaller tooltip text for consistency
      },
      y: {
        formatter: function (val) {
          return val + '%'; // Add a percentage symbol for clarity
        }
      }
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
  <v-card elevation="0">
    <v-card variant="outlined">
      <v-card-text>
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
      </v-card-text>
    </v-card>
    
  </v-card>
</template>
