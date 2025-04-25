import path from 'path';
import { defineConfig } from 'vite'
import UnoCSS from 'unocss/vite'
import react from '@vitejs/plugin-react'
import legacy from '@vitejs/plugin-legacy';

// https://vitejs.dev/config/
export default defineConfig({
  build:{
    target: 'es2015'
  },
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src')
    }
  },
  server: {
    port: 8123,
    proxy: {
      '/mockApi': {
        target: 'http://127.0.0.1:8101',
        changeOrigin: true,
        rewrite: (path) => {
          console.log('path:',path)
          const p = path.replace(/^\/mockApi/, '');
          console.log("p",p)
          return p
        }
      },
    }
  },
  plugins: [
    react(),
    UnoCSS(),
    legacy({
      targets: ['ie >= 11', 'chrome <= 60'],
      additionalLegacyPolyfills: ['regenerator-runtime/runtime'],
      renderLegacyChunks: true,
      polyfills: [
        'es.symbol',
        'es.promise',
        'es.promise.finally',
        'es.map',
        'es.set',
        'es.array.filter',
        'es.array.for-each',
        'es.array.flat-map',
        'es.object.define-properties',
        'es.object.define-property',
        'es.object.get-own-property-descriptor',
        'es.object.get-own-property-descriptors',
        'es.object.keys',
        'es.object.to-string',
        'web.dom-collections.for-each',
        'esnext.global-this',
        'esnext.string.match-all'
      ]
    })
  ],
})
