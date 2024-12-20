// src/pages/TestAlerts.js
import React, { useState, useEffect } from 'react';
import { Box, Button, TextField, Typography, Paper, Snackbar, Alert } from '@mui/material';
import { WebSocketService } from '../services/websocketService';

const TestAlerts = () => {
  const userId = localStorage.getItem('userId');
  const [testData, setTestData] = useState({
    userId: userId,
    cityId: '',
    aqi: 301
  });
  const [messages, setMessages] = useState([]);
  const [notification, setNotification] = useState({
    open: false,
    message: '',
    severity: 'info'
  });

  useEffect(() => {
    const ws = new WebSocketService(userId);
    ws.connect((alert) => {
      console.log('Received alert:', alert);
      setMessages(prev => [alert, ...prev]);
      setNotification({
        open: true,
        message: alert.message,
        severity: 'warning'
      });
    });
    return () => ws.disconnect();
  }, [userId]);

  const handleTest = async () => {
    try {
      console.log('Sending test data:', testData);
      const response = await fetch('http://localhost:8080/api/test/simulate-alert', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(testData)
      });
      
      const data = await response.json();
      console.log('Test response:', data);
      
      setNotification({
        open: true,
        message: 'Test envoyé avec succès',
        severity: 'success'
      });
    } catch (error) {
      console.error('Test error:', error);
      setNotification({
        open: true,
        message: error.message,
        severity: 'error'
      });
    }
  };

  return (
    <Box sx={{ p: 3 }}>
      <Paper sx={{ p: 3, mb: 3 }}>
        <Typography variant="h5" sx={{ mb: 3, color: '#00fff5' }}>
          Test des Alertes
        </Typography>
        
        <Box sx={{ display: 'flex', gap: 2, mb: 3 }}>
          <TextField
            label="ID Ville"
            value={testData.cityId}
            onChange={(e) => setTestData(prev => ({ ...prev, cityId: e.target.value }))}
          />
          <TextField
            label="AQI"
            type="number"
            value={testData.aqi}
            onChange={(e) => setTestData(prev => ({ ...prev, aqi: parseInt(e.target.value) }))}
          />
          <Button 
            variant="contained"
            onClick={handleTest}
            sx={{ bgcolor: '#00fff5', color: '#000' }}
          >
            Envoyer Test
          </Button>
        </Box>

        <Typography variant="h6" sx={{ mb: 2, color: '#00fff5' }}>
          Messages Reçus:
        </Typography>
        {messages.map((msg, index) => (
          <Paper key={index} sx={{ p: 2, mb: 2, bgcolor: 'rgba(0,0,0,0.2)' }}>
            <Typography sx={{ color: msg.priority?.color }}>
              {msg.message}
            </Typography>
            <Typography variant="caption" sx={{ color: 'text.secondary' }}>
              AQI: {msg.aqi} | Niveau: {msg.priority?.label} | Ville: {msg.cityName}
            </Typography>
          </Paper>
        ))}
      </Paper>

      <Snackbar
        open={notification.open}
        autoHideDuration={4000}
        onClose={() => setNotification(prev => ({ ...prev, open: false }))}
      >
        <Alert severity={notification.severity}>
          {notification.message}
        </Alert>
      </Snackbar>
    </Box>
  );
};

export default TestAlerts;