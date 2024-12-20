// src/components/UserProfile.js
import React, { useState, useEffect } from 'react';
import { Box, Paper, Typography, Button, List, ListItem, ListItemText, Divider, Snackbar, Alert, CircularProgress, Stack } from '@mui/material';
import { format, parseISO } from 'date-fns';
import NotificationPreferences from './NotificationPreferences';
import FavoritesList from './FavoritesList';

const UserProfile = ({ userId }) => {
  const [preferences, setPreferences] = useState({
    aqiThreshold: 3,
    emailNotificationsEnabled: true,
    appNotificationsEnabled: true
  });
  const [userDetails, setUserDetails] = useState(null);
  const [alertHistory, setAlertHistory] = useState([]);
  const [loading, setLoading] = useState(true);
  const [notification, setNotification] = useState({
    open: false,
    message: '',
    severity: 'info'
  });

  useEffect(() => {
    loadUserData();
  }, [userId]);

  const formatTimestamp = (timestamp) => {
    try {
      if (!timestamp) return 'Date non disponible';
      return format(parseISO(timestamp), 'dd/MM/yyyy HH:mm');
    } catch (error) {
      console.error('Error formatting date:', error);
      return 'Date invalide';
    }
  };

  const loadUserData = async () => {
    if (!userId) {
      showNotification('ID utilisateur manquant', 'error');
      return;
    }

    setLoading(true);
    const token = localStorage.getItem('token');
    const headers = {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
      'Authorization': `Bearer ${token}`
    };

    try {
      const [userResponse, prefsResponse, historyResponse] = await Promise.all([
        fetch(`http://localhost:8080/api/auth/user/${userId}`, { method: 'GET', headers }),
        fetch(`http://localhost:8080/api/auth/preferences/${userId}`, { method: 'GET', headers }),
        fetch(`http://localhost:8080/api/auth/alerts/history/${userId}`, { method: 'GET', headers })
      ]);

      if (!userResponse.ok) throw new Error('Erreur lors du chargement du profil');
      
      const userData = await userResponse.json();
      console.log('User data:', userData);
      setUserDetails(userData);

      if (prefsResponse.ok) {
        const prefsData = await prefsResponse.json();
        setPreferences(prefsData);
      }

      if (historyResponse.ok) {
        const historyData = await historyResponse.json();
        console.log('Raw alert history:', historyData);

        if (Array.isArray(historyData)) {
          const validatedHistory = historyData
            .filter(alert => alert && alert.id)
            .map(alert => ({
              id: alert.id,
              cityName: alert.cityName,
              aqi: alert.aqi,
              priority: alert.priority,
              timestamp: alert.timestamp,
              message: alert.message,
              latitude: alert.latitude,
              longitude: alert.longitude
            }));

          console.log('Validated history:', validatedHistory);
          setAlertHistory(validatedHistory);
        }
      }
    } catch (error) {
      console.error('Error loading data:', error);
      showNotification(error.message, 'error');
    } finally {
      setLoading(false);
    }
  };

  const handlePreferencesSubmit = async (e) => {
    e.preventDefault();
    const token = localStorage.getItem('token');

    try {
      const response = await fetch(`http://localhost:8080/api/auth/preferences/${userId}`, {
        method: 'PUT',
        headers: { 
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(preferences)
      });

      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || 'Erreur de mise à jour');
      }

      showNotification('Préférences mises à jour avec succès', 'success');
      await loadUserData();
    } catch (error) {
      showNotification(error.message, 'error');
    }
  };

  const showNotification = (message, severity = 'info') => {
    setNotification({ open: true, message, severity });
  };

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box sx={{ maxWidth: 800, mx: 'auto', mt: 4, p: 3 }}>
      <Paper sx={{ p: 3, mb: 3 }}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 4 }}>
          <Typography variant="h4" sx={{ color: '#00fff5' }}>
            {userDetails ? `Profil de ${userDetails.username}` : 'Chargement...'}
          </Typography>
          {userDetails && (
            <Typography variant="subtitle1" sx={{ color: '#00fff5' }}>
              {userDetails.email}
            </Typography>
          )}
        </Box>

        <Box component="form" onSubmit={handlePreferencesSubmit}>
          <NotificationPreferences 
            preferences={preferences}
            setPreferences={setPreferences}
          />

          <Button
            type="submit"
            variant="contained"
            fullWidth
            sx={{
              bgcolor: '#00fff5',
              color: '#000',
              '&:hover': { bgcolor: '#00cccc' }
            }}
          >
            Sauvegarder les Préférences
          </Button>
        </Box>
      </Paper>

      <Paper sx={{ p: 3, mb: 3 }}>
        <FavoritesList />
      </Paper>

      <Paper sx={{ p: 3 }}>
        <Typography variant="h6" sx={{ mb: 3, color: '#00fff5' }}>
          Historique des Alertes
        </Typography>
        {alertHistory && alertHistory.length > 0 ? (
          <List>
            {alertHistory.map((alert, index) => (
              <React.Fragment key={alert.id || index}>
                <ListItem>
                  <ListItemText
                    primary={
                      <Box sx={{ color: '#fff', fontWeight: 'medium' }}>
                        {alert.cityName || 'Ville inconnue'} - AQI: {alert.aqi || 'N/A'}
                      </Box>
                    }
                    secondary={
                      <Stack spacing={1} sx={{ mt: 1 }}>
                        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                          <Box
                            sx={{ 
                              color: alert.priority?.color || '#fff',
                              bgcolor: 'rgba(0,0,0,0.2)',
                              px: 1,
                              py: 0.5,
                              borderRadius: 1
                            }}
                          >
                            {alert.priority?.label || 'Niveau non défini'}
                          </Box>
                          <Typography variant="body2" sx={{ color: 'text.secondary' }}>
                            {formatTimestamp(alert.timestamp)}
                          </Typography>
                        </Box>
                        {alert.message && (
                          <Typography variant="body2" sx={{ color: 'text.secondary' }}>
                            {alert.message}
                          </Typography>
                        )}
                      </Stack>
                    }
                  />
                </ListItem>
                {index < alertHistory.length - 1 && <Divider />}
              </React.Fragment>
            ))}
          </List>
        ) : (
          <Box sx={{ 
            py: 4, 
            display: 'flex', 
            justifyContent: 'center',
            alignItems: 'center',
            bgcolor: 'rgba(0,0,0,0.2)',
            borderRadius: 1
          }}>
            <Typography sx={{ color: 'text.secondary' }}>
              Aucune alerte dans l'historique
            </Typography>
          </Box>
        )}
      </Paper>

      <Snackbar
        open={notification.open}
        autoHideDuration={4000}
        onClose={() => setNotification(prev => ({ ...prev, open: false }))}
      >
        <Alert 
          severity={notification.severity}
          onClose={() => setNotification(prev => ({ ...prev, open: false }))}
        >
          {notification.message}
        </Alert>
      </Snackbar>
    </Box>
  );
};

export default UserProfile;