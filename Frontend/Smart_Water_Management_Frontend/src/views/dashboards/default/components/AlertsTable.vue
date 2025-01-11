<template>
  <v-data-table
    :headers="headers"
    :items="alerts"
    :loading="loading"
  >
    <template #[`item.severity`]="{ item }">
      <v-chip
        :color="item.severity === 'HIGH' ? 'error' : item.severity === 'MEDIUM' ? 'warning' : 'success'"
        small
      >
        {{ item.severity }}
      </v-chip>
    </template>
    
    <template #[`item.actions`]="{ item }">
      <v-btn
        color="primary"
        small
        @click="resolveAlert(item.id)"
        :disabled="item.status === 'RESOLVED'"
      >
        Resolve
      </v-btn>
    </template>
  </v-data-table>
</template>