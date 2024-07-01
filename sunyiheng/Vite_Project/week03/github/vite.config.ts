import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

// vite 配置跨域代理
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      "/api1": {
        target: "http://localhost:5000",
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api1/, ""),
      },
    },
  },
});
