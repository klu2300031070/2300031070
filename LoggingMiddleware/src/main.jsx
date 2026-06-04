import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import Vehicle_Scheduling from './Vehicle_Scheduling';


createRoot(document.getElementById('root')).render(
  <StrictMode>
    <App />
    <Vehicle_Scheduling/>
  </StrictMode>,
)
