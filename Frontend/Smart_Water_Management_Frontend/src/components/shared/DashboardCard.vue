<script setup lang="ts">
defineProps<{
  title?: string;
  subtitle?: string;
  loading?: boolean;
}>();
</script>

<template>
  <v-card class="h-100">
    <v-card-item v-if="title || subtitle">
      <v-card-title v-if="title">{{ title }}</v-card-title>
      <v-card-subtitle v-if="subtitle">{{ subtitle }}</v-card-subtitle>
    </v-card-item>

    <v-card-text>
      <v-skeleton-loader
        v-if="loading"
        type="article"
      />
      <slot v-else />
    </v-card-text>

    <slot name="actions" />
  </v-card>
</template>

<style scoped>
.h-100 {
  height: 100%;
}
</style> 