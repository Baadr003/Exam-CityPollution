// NotificationPreferences.js
import React from 'react';
import {
  Box,
  Typography,
  Switch,
  Tooltip,
  Paper,
  Slider,
  Chip
} from '@mui/material';
import { Info } from '@mui/icons-material';

const NotificationPreferences = ({ preferences, setPreferences }) => {
  const marks = [
    { value: 1, label: 'Bon', color: '#00e400' },
    { value: 2, label: 'Modéré', color: '#ffff00' },
    { value: 3, label: 'Malsain', color: '#ff7e00' },
    { value: 4, label: 'Dangereux', color: '#ff0000' },
    { value: 5, label: 'Très Dangereux', color: '#8f3f97' }
  ];

  const getThresholdColor = (value) => {
    const mark = marks.find(m => m.value === value);
    return mark ? mark.color : '#7e0023';
  };

  return (
    <Paper sx={{ p: 3, mb: 3 }}>
      <Typography variant="h6" sx={{ mb: 3, color: '#00fff5' }}>
        Préférences de Notification
      </Typography>

      <Box sx={{ mb: 4 }}>
        <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
          <Typography>Niveau d'Alerte AQI</Typography>
          <Tooltip title="Vous serez notifié quand l'AQI atteindra ce niveau">
            <Info sx={{ ml: 1, fontSize: 16, color: '#00fff5' }} />
          </Tooltip>
        </Box>
        <Slider
          value={preferences.aqiThreshold}
          onChange={(_, value) => setPreferences(prev => ({
            ...prev,
            aqiThreshold: value
          }))}
          min={1}
          max={5}
          step={1}
          marks={marks}
          sx={{
            color: getThresholdColor(preferences.aqiThreshold),
            '& .MuiSlider-thumb': {
              backgroundColor: getThresholdColor(preferences.aqiThreshold),
            },
            '& .MuiSlider-mark': {
              backgroundColor: '#666',
            }
          }}
        />
        <Box sx={{ mt: 2, display: 'flex', alignItems: 'center' }}>
          <Typography>Niveau actuel:</Typography>
          <Chip
            label={marks[preferences.aqiThreshold - 1]?.label}
            sx={{
              ml: 1,
              backgroundColor: getThresholdColor(preferences.aqiThreshold),
              color: '#fff'
            }}
          />
        </Box>
      </Box>

      <Box sx={{ mb: 3 }}>
        <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
          <Typography>Notifications Email</Typography>
          <Switch
            checked={preferences.emailNotificationsEnabled}
            onChange={(e) => setPreferences(prev => ({
              ...prev,
              emailNotificationsEnabled: e.target.checked
            }))}
          />
        </Box>

        <Box sx={{ display: 'flex', alignItems: 'center' }}>
          <Typography>Notifications Application</Typography>
          <Switch
            checked={preferences.appNotificationsEnabled}
            onChange={(e) => setPreferences(prev => ({
              ...prev,
              appNotificationsEnabled: e.target.checked
            }))}
          />
        </Box>
      </Box>

      <Typography variant="caption" color="text.secondary">
        Les notifications sont envoyées quand l'AQI d'une ville favorite atteint ou dépasse votre niveau d'alerte
      </Typography>
    </Paper>
  );
};

export default NotificationPreferences;