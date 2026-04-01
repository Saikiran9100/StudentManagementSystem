import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { CssBaseline, ThemeProvider, createTheme } from '@mui/material';
import App from './App.jsx';
import './index.css';

const theme = createTheme({
  palette: {
    primary: { main: '#1d4ed8', light: '#bfdbfe', dark: '#0f172a' },
    secondary: { main: '#f97316' },
    background: { default: '#f3f7fb', paper: '#ffffff' },
  },
  shape: { borderRadius: 18 },
  typography: {
    fontFamily: '"Trebuchet MS", "Segoe UI", sans-serif',
    h3: { letterSpacing: '-0.04em' },
    h5: { letterSpacing: '-0.02em' },
  },
  components: {
    MuiButton: { styleOverrides: { root: { borderRadius: 14, textTransform: 'none', fontWeight: 700 } } },
    MuiPaper: { styleOverrides: { root: { backgroundImage: 'none' } } },
  },
});

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <App />
    </ThemeProvider>
  </StrictMode>,
);
