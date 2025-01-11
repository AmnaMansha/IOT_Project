
<template>
  <v-card elevation="0">
    <v-card variant="outlined">
      <v-card-text>
        <h4 class="text-h4">Users List</h4>
        <v-table class="styled-table">
          <thead>
            <tr>
              <th class="text-left">ID</th>
              <th class="text-left">Email</th>
              <th class="text-left">Role</th>
              <th class="text-center">Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="user in users" :key="user.id">
              <td>{{ user.id }}</td>
              <td>{{ user.email }}</td>
              <td>
                <v-chip
                  :color="user.role === 'ADMIN' ? 'primary' : 'secondary'"
                  :text-color="white"
                  size="small"
                >
                  {{ user.role }}
                </v-chip>
              </td>
              <td class="text-center">
                <v-btn
                 
                  color="error"
                  size="small"
                  variant="text"
                  @click="deleteUser(user.id)"
                  :loading="deleting === user.id"
                >
                delete 
                </v-btn>
              </td>
            </tr>
          </tbody>
        </v-table>

        <!-- Confirmation Dialog -->
        <v-dialog v-model="showConfirmDialog" max-width="400">
          <v-card>
            <v-card-title class="text-h5">
              Confirm Delete
            </v-card-title>
            <v-card-text>
              Are you sure you want to delete this user?
            </v-card-text>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="grey" text @click="showConfirmDialog = false">
                Cancel
              </v-btn>
              <v-btn color="error" @click="confirmDelete">
                Delete
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>

        <!-- Snackbar for notifications -->
        <v-snackbar v-model="showSnackbar" :color="snackbarColor" timeout="3000">
          {{ snackbarMessage }}
        </v-snackbar>
      </v-card-text>
    </v-card>
  </v-card>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import axios from 'axios';

const users = ref<any[]>([]);
const deleting = ref<number | null>(null);
const showConfirmDialog = ref(false);
const userToDelete = ref<number | null>(null);
const showSnackbar = ref(false);
const snackbarMessage = ref('');
const snackbarColor = ref('');

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080',
  auth: {
    username: localStorage.getItem('email') || '',
    password: localStorage.getItem('password') || ''
  },
});

const fetchUserData = async () => {
  try {
    const response = await axiosInstance.get('/api/auth/users');
    if (Array.isArray(response.data)) {
      users.value = response.data;
    }
  } catch (error) {
    showNotification('Failed to fetch users', 'error');
  }
};

const deleteUser = (id: number) => {
  userToDelete.value = id;
  showConfirmDialog.value = true;
};

const confirmDelete = async () => {
  if (!userToDelete.value) return;
  
  deleting.value = userToDelete.value;
  showConfirmDialog.value = false;
  
  try {
    await axiosInstance.delete(`/api/auth/delete/${userToDelete.value}`);
    await fetchUserData(); // Refresh the list
    showNotification('User deleted successfully', 'success');
  } catch (error) {
    showNotification('Failed to delete user', 'error');
  } finally {
    deleting.value = null;
    userToDelete.value = null;
  }
};

const showNotification = (message: string, type: 'success' | 'error') => {
  snackbarMessage.value = message;
  snackbarColor.value = type;
  showSnackbar.value = true;
};

onMounted(() => {
  fetchUserData();
});
</script>

<style scoped>
.text-h4 {
  font-weight: 600;
  margin-bottom: 20px;
  color: #333;
}

.styled-table {
  width: 100%;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.styled-table th {
  background-color: #1976D2;
  color: white;
  font-weight: 500;
  padding: 16px;
  font-size: 0.95rem;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.styled-table td {
  padding: 12px 16px;
  font-size: 0.9rem;
  border-bottom: 1px solid #eee;
}

.styled-table tbody tr {
  transition: background-color 0.3s;
}

.styled-table tbody tr:hover {
  background-color: #f5f5f5;
}

.v-chip {
  font-size: 0.85rem;
}

.actions-cell {
  width: 100px;
}
</style>