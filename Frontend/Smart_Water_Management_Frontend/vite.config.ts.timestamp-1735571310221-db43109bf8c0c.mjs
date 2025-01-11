// vite.config.ts
import { fileURLToPath, URL } from "url";
import { defineConfig } from "file:///C:/Users/Amna/Downloads/berry-free-vuetify-vuejs-admin-template-v1.2.0/berry-free-vuetify-vuejs-admin-template/node_modules/vite/dist/node/index.js";
import vue from "file:///C:/Users/Amna/Downloads/berry-free-vuetify-vuejs-admin-template-v1.2.0/berry-free-vuetify-vuejs-admin-template/node_modules/@vitejs/plugin-vue/dist/index.mjs";
import vuetify from "file:///C:/Users/Amna/Downloads/berry-free-vuetify-vuejs-admin-template-v1.2.0/berry-free-vuetify-vuejs-admin-template/node_modules/vite-plugin-vuetify/dist/index.mjs";
var __vite_injected_original_import_meta_url = "file:///C:/Users/Amna/Downloads/berry-free-vuetify-vuejs-admin-template-v1.2.0/berry-free-vuetify-vuejs-admin-template/vite.config.ts";
var vite_config_default = defineConfig({
  plugins: [
    vue({
      template: {
        compilerOptions: {
          isCustomElement: (tag) => ["v-list-recognize-title"].includes(tag)
        }
      }
    }),
    vuetify({
      autoImport: true
    })
  ],
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", __vite_injected_original_import_meta_url))
    }
  },
  css: {
    preprocessorOptions: {
      scss: {}
    }
  },
  build: {
    chunkSizeWarningLimit: 1024 * 1024
    // Set the limit to 1 MB
  },
  optimizeDeps: {
    exclude: ["vuetify"],
    entries: ["./src/**/*.vue"]
  }
});
export {
  vite_config_default as default
};
//# sourceMappingURL=data:application/json;base64,ewogICJ2ZXJzaW9uIjogMywKICAic291cmNlcyI6IFsidml0ZS5jb25maWcudHMiXSwKICAic291cmNlc0NvbnRlbnQiOiBbImNvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9kaXJuYW1lID0gXCJDOlxcXFxVc2Vyc1xcXFxBbW5hXFxcXERvd25sb2Fkc1xcXFxiZXJyeS1mcmVlLXZ1ZXRpZnktdnVlanMtYWRtaW4tdGVtcGxhdGUtdjEuMi4wXFxcXGJlcnJ5LWZyZWUtdnVldGlmeS12dWVqcy1hZG1pbi10ZW1wbGF0ZVwiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9maWxlbmFtZSA9IFwiQzpcXFxcVXNlcnNcXFxcQW1uYVxcXFxEb3dubG9hZHNcXFxcYmVycnktZnJlZS12dWV0aWZ5LXZ1ZWpzLWFkbWluLXRlbXBsYXRlLXYxLjIuMFxcXFxiZXJyeS1mcmVlLXZ1ZXRpZnktdnVlanMtYWRtaW4tdGVtcGxhdGVcXFxcdml0ZS5jb25maWcudHNcIjtjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfaW1wb3J0X21ldGFfdXJsID0gXCJmaWxlOi8vL0M6L1VzZXJzL0FtbmEvRG93bmxvYWRzL2JlcnJ5LWZyZWUtdnVldGlmeS12dWVqcy1hZG1pbi10ZW1wbGF0ZS12MS4yLjAvYmVycnktZnJlZS12dWV0aWZ5LXZ1ZWpzLWFkbWluLXRlbXBsYXRlL3ZpdGUuY29uZmlnLnRzXCI7aW1wb3J0IHsgZmlsZVVSTFRvUGF0aCwgVVJMIH0gZnJvbSAndXJsJztcbmltcG9ydCB7IGRlZmluZUNvbmZpZyB9IGZyb20gJ3ZpdGUnO1xuaW1wb3J0IHZ1ZSBmcm9tICdAdml0ZWpzL3BsdWdpbi12dWUnO1xuaW1wb3J0IHZ1ZXRpZnkgZnJvbSAndml0ZS1wbHVnaW4tdnVldGlmeSc7XG5cbi8vIGh0dHBzOi8vdml0ZWpzLmRldi9jb25maWcvXG5leHBvcnQgZGVmYXVsdCBkZWZpbmVDb25maWcoe1xuICBwbHVnaW5zOiBbXG4gICAgdnVlKHtcbiAgICAgIHRlbXBsYXRlOiB7XG4gICAgICAgIGNvbXBpbGVyT3B0aW9uczoge1xuICAgICAgICAgIGlzQ3VzdG9tRWxlbWVudDogKHRhZykgPT4gWyd2LWxpc3QtcmVjb2duaXplLXRpdGxlJ10uaW5jbHVkZXModGFnKVxuICAgICAgICB9XG4gICAgICB9XG4gICAgfSksXG4gICAgdnVldGlmeSh7XG4gICAgICBhdXRvSW1wb3J0OiB0cnVlXG4gICAgfSlcbiAgXSxcbiAgcmVzb2x2ZToge1xuICAgIGFsaWFzOiB7XG4gICAgICAnQCc6IGZpbGVVUkxUb1BhdGgobmV3IFVSTCgnLi9zcmMnLCBpbXBvcnQubWV0YS51cmwpKVxuICAgIH1cbiAgfSxcbiAgY3NzOiB7XG4gICAgcHJlcHJvY2Vzc29yT3B0aW9uczoge1xuICAgICAgc2Nzczoge31cbiAgICB9XG4gIH0sXG4gIGJ1aWxkOiB7XG4gICAgY2h1bmtTaXplV2FybmluZ0xpbWl0OiAxMDI0ICogMTAyNCAvLyBTZXQgdGhlIGxpbWl0IHRvIDEgTUJcbiAgfSxcbiAgb3B0aW1pemVEZXBzOiB7XG4gICAgZXhjbHVkZTogWyd2dWV0aWZ5J10sXG4gICAgZW50cmllczogWycuL3NyYy8qKi8qLnZ1ZSddXG4gIH1cbn0pO1xuIl0sCiAgIm1hcHBpbmdzIjogIjtBQUF3Z0IsU0FBUyxlQUFlLFdBQVc7QUFDM2lCLFNBQVMsb0JBQW9CO0FBQzdCLE9BQU8sU0FBUztBQUNoQixPQUFPLGFBQWE7QUFIMlQsSUFBTSwyQ0FBMkM7QUFNaFksSUFBTyxzQkFBUSxhQUFhO0FBQUEsRUFDMUIsU0FBUztBQUFBLElBQ1AsSUFBSTtBQUFBLE1BQ0YsVUFBVTtBQUFBLFFBQ1IsaUJBQWlCO0FBQUEsVUFDZixpQkFBaUIsQ0FBQyxRQUFRLENBQUMsd0JBQXdCLEVBQUUsU0FBUyxHQUFHO0FBQUEsUUFDbkU7QUFBQSxNQUNGO0FBQUEsSUFDRixDQUFDO0FBQUEsSUFDRCxRQUFRO0FBQUEsTUFDTixZQUFZO0FBQUEsSUFDZCxDQUFDO0FBQUEsRUFDSDtBQUFBLEVBQ0EsU0FBUztBQUFBLElBQ1AsT0FBTztBQUFBLE1BQ0wsS0FBSyxjQUFjLElBQUksSUFBSSxTQUFTLHdDQUFlLENBQUM7QUFBQSxJQUN0RDtBQUFBLEVBQ0Y7QUFBQSxFQUNBLEtBQUs7QUFBQSxJQUNILHFCQUFxQjtBQUFBLE1BQ25CLE1BQU0sQ0FBQztBQUFBLElBQ1Q7QUFBQSxFQUNGO0FBQUEsRUFDQSxPQUFPO0FBQUEsSUFDTCx1QkFBdUIsT0FBTztBQUFBO0FBQUEsRUFDaEM7QUFBQSxFQUNBLGNBQWM7QUFBQSxJQUNaLFNBQVMsQ0FBQyxTQUFTO0FBQUEsSUFDbkIsU0FBUyxDQUFDLGdCQUFnQjtBQUFBLEVBQzVCO0FBQ0YsQ0FBQzsiLAogICJuYW1lcyI6IFtdCn0K
