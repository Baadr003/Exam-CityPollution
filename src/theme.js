// src/theme.js
import { createTheme } from '@mui/material/styles';

export const theme = createTheme({
  palette: {
    mode: 'dark',
    primary: {
      main: '#00fff5',
    },
    secondary: {
      main: '#7928ca',
    },
    background: {
      default: '#0a192f',
      paper: '#172a45',
    },
  },
  typography: {
    fontFamily: '"Orbitron", "Roboto", sans-serif',
  },
  components: {
    MuiPaper: {
      styleOverrides: {
        root: {
          backgroundImage: 'linear-gradient(rgba(23, 42, 69, 0.7), rgba(23, 42, 69, 0.9))',
          backdropFilter: 'blur(10px)',
          border: '1px solid rgba(0, 255, 245, 0.1)',
        },
      },
    },
  },
});