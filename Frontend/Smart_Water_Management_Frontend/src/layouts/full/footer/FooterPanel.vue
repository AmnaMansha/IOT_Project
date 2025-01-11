<script setup lang="ts">
import { shallowRef } from 'vue';

const footerLink = shallowRef([
  {
    title: 'Home',
    url: 'https://berrydashboard.io/vue/free'
  },
  {
    title: 'Documentation',
    url: 'https://codedthemes.gitbook.io/berry-vuetify/'
  },
  {
    title: 'Support',
    url: 'https://codedthemes.support-hub.io/'
  }
]);
</script>
<template>
  <v-footer class="px-0 footer mt-2">
    <v-row justify="center" align="center" no-gutters>
      <!-- <v-col cols="12" sm="6">
        <p class="text-body-1 mb-0 text-sm-left text-center">
          Smart water management system
         
        </p>
      </v-col> -->
      
    </v-row>
  </v-footer>
</template>

<style lang="scss">
.v-footer {
  position: unset;
}
footer {
  a {
    &:hover {
      color: rgb(var(--v-theme-primary)) !important;
    }
  }
}
</style>
