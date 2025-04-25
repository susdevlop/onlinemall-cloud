import 'core-js/stable'
import 'regenerator-runtime/runtime.js'
import 'react-app-polyfill/ie9'
import 'react-app-polyfill/stable'
// import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import 'virtual:uno.css'

import App from './App.tsx'

createRoot(document.getElementById('root')!).render(
    <App />
)
// createRoot(document.getElementById('root')!).render(
//     <StrictMode>
//         <App />
//     </StrictMode>,
// )
