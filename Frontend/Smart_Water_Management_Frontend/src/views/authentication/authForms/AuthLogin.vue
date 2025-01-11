<script setup lang="ts">
import { ref } from 'vue';
import { useAuthStore } from '@/stores/auth';
import { useToast } from 'vue-toastification';

// Store and Toast
const authStore = useAuthStore();
const toast = useToast();

// Form data
const email = ref('');
const password = ref('');
const loading = ref(false);

// Email validation regex
const isValidEmail = (email: string) => {
  const regex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
  return regex.test(email);
};

// Handle login
const handleLogin = async () => {
  if (!email.value || !password.value) {
    toast.error('Please fill in all fields');
    return;
  }

  if (!isValidEmail(email.value)) {
    toast.error('Please enter a valid email address');
    return;
  }

  loading.value = true;
  
  try {
    const response = await authStore.login({
      email: email.value.trim(),
      password: password.value
    });

    if (response.success) {
      toast.success('Login successful');
      // Routing handled in store
    } else {
      toast.error('Login failed');
    }
  } catch (error) {
    toast.error('Login failed');
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <v-form @submit.prevent="handleLogin">
    <v-text-field
      v-model="email"
      label="Email"
      type="email"
      placeholder="Enter email"
      required
      class="mt-4"
    />
    <v-text-field
      v-model="password"
      label="Password"
      type="password"
      placeholder="Enter password"
      required
    />
    <v-btn
      color="primary"
      size="large"
      block
      type="submit"
      :loading="loading"
      class="mt-6"
    >
      Sign In
    </v-btn>
    <div class="text-center mt-4">
      <router-link
        to="/auth/register"
        class="text-secondary text-decoration-none"
      >
        Create an account
      </router-link>
    </div>
  </v-form>
</template>
