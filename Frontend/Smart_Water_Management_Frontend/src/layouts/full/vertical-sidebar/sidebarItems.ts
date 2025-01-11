export const sidebarItems = [
  {
    title: 'Dashboard',
    icon: 'mdi-view-dashboard-outline',
    to: '/dashboard'
  },
  {
    title: 'Water Monitoring',
    icon: 'mdi-water',
    children: [
      {
        title: 'Water Level',
        icon: 'mdi-water-percent',
        to: '/water-level'
      },
      {
        title: 'Temperature & Humidity',
        icon: 'mdi-thermometer',
        to: '/temperature-humidity'
      },
      {
        title: 'Pump Control',
        icon: 'mdi-pump',
        to: '/pump-control',
        role: 'admin'
      },
      {
        title: 'Alerts',
        icon: 'mdi-alert',
        to: '/alerts'
      }
    ]
  },
  // ... existing sidebar items ...
]; 