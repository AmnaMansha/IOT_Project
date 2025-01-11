<script setup lang="ts">
import { ref } from 'vue';
import { useAuthStore } from '@/stores/auth';
import { useToast } from 'vue-toastification';
import { useRouter } from 'vue-router';

const authStore = useAuthStore();
const toast = useToast();
const router = useRouter();

const formData = ref({
  name: '',
  email: '',
  password: '',
  confirmPassword: '',
  role: 'USER'
});

const loading = ref(false);

// const handleRegister = async () => {
//   try {
//     if (!formData.value.name || !formData.value.email || !formData.value.password) {
//       toast.error('Please fill in all required fields');
//       return;
//     }

//     if (formData.value.password !== formData.value.confirmPassword) {
//       toast.error('Passwords do not match');
//       return;
//     }

//     loading.value = true;

//     const response = await authStore.register({
//       name: formData.value.name,
//       email: formData.value.email,
//       password: formData.value.password,
//       role: formData.value.role
//     });

//     if (response.success) {
//       toast.success('Registration successful! Please login.');
//       router.push('/auth/login');
//     } else {
//       toast.error(response.message);
//     }
//   } catch (error) {
//     toast.error('Registration failed');
//   } finally {
//     loading.value = false;
//   }
// };
const handleRegister = async () => {
  try {
    if (!formData.value.name || !formData.value.email || !formData.value.password) {
      toast.error('Please fill in all required fields');
      return;
    }

    if (formData.value.password !== formData.value.confirmPassword) {
      toast.error('Passwords do not match');
      return;
    }

    loading.value = true;

    const response = await authStore.register({
      name: formData.value.name,
      email: formData.value.email,
      password: formData.value.password,
      role: formData.value.role
    });

    if (response.success) {
      toast.success('Registration successful! Please login.');
      router.push('/auth/login');
    } else {
      toast.error(response.message);
    }
  } catch (error) {
    toast.error('Registration failed');
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <v-form @submit.prevent="handleRegister">
    <v-text-field
      v-model="formData.name"
      label="Name"
      type="text"
      placeholder="Enter name"
      required
      class="mt-4"
    />
    <v-text-field
      v-model="formData.email"
      label="Email"
      type="email"
      placeholder="Enter email"
      required
    />
    <v-text-field
      v-model="formData.password"
      label="Password"
      type="password"
      placeholder="Enter password"
      required
    />
    <v-text-field
      v-model="formData.confirmPassword"
      label="Confirm Password"
      type="password"
      placeholder="Confirm password"
      required
    />
    <v-select
      v-model="formData.role"
      label="Role"
      :items="[
        { title: 'User', value: 'USER' },
        { title: 'Admin', value: 'ADMIN' }
      ]"
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
      Sign Up
    </v-btn>
    <div class="text-center mt-4">
      <router-link
        to="/auth/login"
        class="text-secondary text-decoration-none"
      >
        Already have an account? Sign In
      </router-link>
    </div>
  </v-form>
</template>
