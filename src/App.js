// src/App.js
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { CssBaseline, ThemeProvider } from '@mui/material';
import MainPage from './pages/MainPage';
import UserProfile from './components/UserProfile';
import TestAlerts from './pages/TestAlerts'; // Add this import
import { theme } from './theme';

function App() {
  const [userId, setUserId] = React.useState(null);

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Router>
        <Routes>
          <Route path="/" element={<MainPage setUserId={setUserId} />} />
          <Route path="/profile" element={<UserProfile userId={userId} />} />
          <Route path="/test" element={<TestAlerts />} />
        </Routes>
      </Router>
    </ThemeProvider>
  );
}

export default App;