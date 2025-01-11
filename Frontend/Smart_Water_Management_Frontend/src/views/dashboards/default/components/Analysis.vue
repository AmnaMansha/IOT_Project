<!-- <template>
  <v-card elevation="0">
    <v-card variant="outlined">
      <v-card-text>
        <h4 class="text-h4">Historical Water Level, Temperature, and Alerts</h4>
        <apexchart 
          type="line" 
          height="400" 
          :options="chartOptions" 
          :series="chartData"
        ></apexchart>
      </v-card-text>
    </v-card>
  </v-card>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';
import ApexCharts from 'vue3-apexcharts';

// Data references
const waterLevelData = ref([]);
const temperatureData = ref([]);
const alertsData = ref([]);

// Axios instance with Basic Auth
const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080', // Replace with your actual base URL
  auth: {
    username: localStorage.getItem('email') || '',
    password: localStorage.getItem('password') || '',
  },
});

// Fetch water level data
const fetchWaterLevelData = async () => {
  try {
    const response = await axiosInstance.get('/api/waterlevel/current');
    if (response.data && response.data.timestamp) {
      waterLevelData.value = [{
        timestamp: new Date(response.data.timestamp),
        level: response.data.level,
      }];
    }
  } catch (error) {
    console.error('Failed to fetch water level data:', error);
  }
};

// Fetch temperature data
const fetchTemperatureData = async () => {
  try {
    const response = await axiosInstance.get('/api/temperature/current');
    if (response.data && response.data.timestamp) {
      temperatureData.value = [{
        timestamp: new Date(response.data.timestamp),
        temperature: response.data.temperature,
      }];
    }
  } catch (error) {
    console.error('Failed to fetch temperature data:', error);
  }
};

// Fetch active alerts data
const fetchAlertsData = async () => {
  try {
    const response = await axiosInstance.get('/api/alerts/active');
    if (Array.isArray(response.data)) {
      alertsData.value = response.data.map(alert => ({
        timestamp: new Date(alert.timestamp),
        severity: alert.severity,
        message: alert.message, // Optional: used for tooltips or detailed analysis
      }));
    }
  } catch (error) {
    console.error('Failed to fetch alerts:', error);
  }
};

// Fetch all data when component mounts
onMounted(() => {
  fetchWaterLevelData();
  fetchTemperatureData();
  fetchAlertsData();
});

// Map severity to a specific color
const mapSeverityToColor = (severity: string) => {
  switch (severity) {
    case 'LOW':
      return '#00E396';  // Green for Low severity
    case 'MEDIUM':
      return '#FFB74D';  // Orange for Medium severity
    case 'HIGH':
      return '#FF4560';  // Red for High severity
    default:
      return '#A0A0A0';  // Grey for unknown
  }
};

// Combine the data into chart series format
const chartData = computed(() => {
  const waterLevelSeries = waterLevelData.value.map(item => ({
    x: item.timestamp.getTime(), 
    y: item.level,
  }));

  const temperatureSeries = temperatureData.value.map(item => ({
    x: item.timestamp.getTime(), 
    y: item.temperature,
  }));

  const alertsSeries = alertsData.value.map(alert => {
  const severityValue = alert.severity === 'LOW' ? 1 : alert.severity === 'MEDIUM' ? 2 : alert.severity === 'HIGH' ? 3 : 0;
  // Ensure the severity is valid and not undefined or negative
  return {
    x: alert.timestamp.getTime(),
    y: severityValue >= 0 ? severityValue : 0,  // Default to 0 if severity is invalid
    color: mapSeverityToColor(alert.severity),
  };
});


  return [
    {
      name: 'Water Level',
      type: 'line',
      data: waterLevelSeries,
    },
    {
      name: 'Temperature',
      type: 'line',
      data: temperatureSeries,
    },
    {
      name: 'Alerts',
      type: 'scatter',
      data: alertsSeries.map(alert => ({
        x: alert.x,
        y: alert.y,
        backgroundColor: alert.color, // Use the color for each alert severity
      })),
    },
  ];
});

// Chart options for customization
const chartOptions = computed(() => ({
  chart: {
    type: 'line',
    height: 400,
  },
  stroke: {
    width: [2, 2, 0], // Water level and temperature lines, no stroke for alert markers
    curve: 'smooth',
  },
  xaxis: {
    type: 'datetime',
    categories: waterLevelData.value.map(item => item.timestamp.toLocaleString()), 
  },
  yaxis: [
    {
      title: {
        text: 'Water Level (m)',
      },
    },
    {
      opposite: true,
      title: {
        text: 'Temperature (°C)',
      },
    },
    {
      opposite: true,
      title: {
        text: 'Alert Severity',
      },
      labels: {
        formatter: function (val) {
          if (val === 1) return 'Low';
          if (val === 2) return 'Medium';
          if (val === 3) return 'High';
          return '';
        },
      },
    },
  ],
  markers: {
    size: 5,
    colors: ['#00E396', '#FFB74D', '#FF4560'], // Define colors for each severity
    hover: {
      size: 7,
    },
  },
  tooltip: {
    shared: true,
    intersect: false,
  },
  annotations: {
    xaxis: [
      ...alertsData.value.map(alert => ({
        x: alert.timestamp.getTime(),
        borderColor: mapSeverityToColor(alert.severity), // Color of the alert annotation
        label: {
          text: `Alert: ${alert.severity}`,
          style: {
            color: '#fff',
            background: mapSeverityToColor(alert.severity),
          },
        },
      })),
    ],
  },
}));

</script> -->
<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';

const select = ref({ state: 'Today', abbr: 'FL' });
const items = [
  { state: 'Today', abbr: 'FL' },
  { state: 'This Month', abbr: 'GA' },
  { state: 'This Year', abbr: 'NE' }
];

// Data placeholders for the chart
const humidityData = ref([]);
const temperatureData = ref([]);
const waterLevelData = ref([]);

// Axios instance
const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080', // Replace with your actual base URL
  auth: {
    username: localStorage.getItem('email') || '',
    password: localStorage.getItem('password') || '',
  },
});

// Fetch the data from APIs
const fetchData = async () => {
  try {
    const humidityResponse = await axiosInstance.get('/api/humidity/all');
    const temperatureResponse = await axiosInstance.get('/api/temperature/all');
    const waterLevelResponse = await axiosInstance.get('/api/waterlevel/all');

    // Format data for graph
    humidityData.value = humidityResponse.data.data.map(record => ({
      timestamp: new Date(record.timestamp).getTime(), // Convert to milliseconds
      humidity: record.humidity
    }));

    temperatureData.value = temperatureResponse.data.data.map(record => ({
      timestamp: new Date(record.timestamp).getTime(), // Convert to milliseconds
      temperature: record.temperature
    }));

    waterLevelData.value = waterLevelResponse.data.data.map(record => ({
      timestamp: new Date(record.timestamp).getTime(), // Convert to milliseconds
      level: record.level
    }));

    console.log(humidityData.value);
    console.log(temperatureData.value);
    console.log(waterLevelData.value);

  } catch (error) {
    console.error('Error fetching data:', error);
  }
};

// Run data fetch on mount
onMounted(() => {
  fetchData();
});

// Chart Options
const chartOptions1 = computed(() => {
  return {
    chart: {
      type: 'line', // Line chart for continuous data
      height: 480,
      fontFamily: `inherit`,
      foreColor: '#a1aab2',
      stacked: false // Change this to false for better line graph representation
    },
    colors: ['#1e88e5', '#f44336', '#4caf50'],
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
    xaxis: {
      type: 'datetime', // Ensure x-axis is datetime
      categories: humidityData.value.map(item => item.timestamp), // Use timestamps for X-axis
      labels: {
        formatter: function(value) {
          const date = new Date(value);
          return `${date.getHours()}:${date.getMinutes()}:${date.getSeconds()}`; // Display formatted timestamp
        }
      }
    },
    yaxis: {
      min: 0,
      max: 100, // Adjust the Y-axis scale
      title: {
        text: 'Degrees (%)'
      }
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

// Chart data
const lineChart1 = computed(() => {
  return {
    series: [
      {
        name: 'Humidity',
        data: humidityData.value.map(item => ({
          x: item.timestamp,
          y: item.humidity
        }))
      },
      {
        name: 'Temperature',
        data: temperatureData.value.map(item => ({
          x: item.timestamp,
          y: item.temperature
        }))
      },
      {
        name: 'Water Level',
        data: waterLevelData.value.map(item => ({
          x: item.timestamp,
          y: item.level
        }))
      }
    ]
  };
});
</script>

<template>
  <v-card elevation="0">
    <v-card variant="outlined">
      <v-card-text>
        <v-row>
          <v-col cols="12" sm="9">
            <span class="text-subtitle-2 text-disabled font-weight-bold">Analysis</span>
         
          </v-col>
          
        </v-row>
        <div class="mt-4">
          <apexchart type="line" height="480" :options="chartOptions1" :series="lineChart1.series"> </apexchart>
        </div>
      </v-card-text>
    </v-card>
  </v-card>
</template>
