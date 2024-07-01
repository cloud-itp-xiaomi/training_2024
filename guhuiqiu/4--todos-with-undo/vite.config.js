import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5174, // 可以根据需要修改端口
  },
  build: {
    rollupOptions: {
      input: {
        main: "./index.html", // 确认指向根目录下的 index.html
      },
    },
  },
});
