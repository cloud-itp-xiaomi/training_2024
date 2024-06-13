import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import { createStyleImportPlugin } from "vite-plugin-style-import";

export default defineConfig({
  plugins: [
    react(),
    createStyleImportPlugin({
      libs: [
        {
          libraryName: "antd",
          esModule: true,
          resolveStyle: (name) => `antd/es/${name}/style/index`,
        },
      ],
    }),
  ],
});
