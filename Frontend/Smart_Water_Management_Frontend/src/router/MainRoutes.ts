const MainRoutes = {
  path: '/main',
  meta: {
    requiresAuth: true
  },
  redirect: '/main/dashboard/default',
  component: () => import('@/layouts/full/FullLayout.vue'),
  children: [
    {
      name: 'UserDashboard',
      path: '/main/user-dashboard',
      component: () => import('@/views/dashboards/user/UserDashboard.vue'),
      meta: {
        requiresAuth: true,
        role: 'USER'
      }
    },
    {
      name: 'AdminDashboard',
      path: '/main/dashboard/default',
      component: () => import('@/views/dashboards/default/DefaultDashboard.vue'),
      meta: {
        requiresAuth: true,
        role: 'ADMIN'
      }
    },
    {
      name: 'LandingPage',
      path: '/',
      component: () => import('@/views/dashboards/default/DefaultDashboard.vue')
    },
    {
      name: 'Default',
      path: '/dashboard/default',
      component: () => import('@/views/dashboards/default/DefaultDashboard.vue')
    },
    {
      name: 'Starter',
      path: '/starter',
      component: () => import('@/views/StarterPage.vue')
    },
  
  ]
};

export default MainRoutes;
