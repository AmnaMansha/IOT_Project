import { createRouter, createWebHistory } from 'vue-router';
import MainRoutes from './MainRoutes';
import AuthRoutes from './AuthRoutes';
import { useAuthStore } from '@/stores/auth';

export const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/:pathMatch(.*)*',
      component: () => import('@/views/pages/maintenance/error/Error404Page.vue')
    },
    MainRoutes,
    AuthRoutes
  ]
});

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth);
  const requiredRole = to.matched.find(record => record.meta.role)?.meta.role;

  if (requiresAuth && !authStore.isAuthenticated) {
    next('/auth/login');
  } else if (requiresAuth && requiredRole) {
    if (authStore.user?.role === requiredRole) {
      next();
    } else if (authStore.user?.role === 'ADMIN') {
      next('/main/dashboard/default');
    } else if (authStore.user?.role === 'USER') {
      next('/main/user-dashboard');
    } else {
      next('/auth/login');
    }
  } else {
    next();
  }
});

export default router;
