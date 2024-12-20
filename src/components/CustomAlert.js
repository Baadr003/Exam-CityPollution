// src/components/CustomAlert.js
import React from 'react';
import { Alert, Box, Typography } from '@mui/material';
import { getAQILevel } from '../utils/aqiUtils';

const CustomAlert = ({ aqi, message, onClose }) => {
  const aqiInfo = getAQILevel(aqi);

  return (
    <Alert
      severity="warning"
      onClose={onClose}
      sx={{
        backgroundColor: `${aqiInfo.color}15`,
        borderLeft: `6px solid ${aqiInfo.color}`,
        '& .MuiAlert-icon': {
          color: aqiInfo.color
        }
      }}
    >
      <Box>
        <Typography variant="subtitle1" sx={{ fontWeight: 'bold', color: aqiInfo.color }}>
          {aqiInfo.label}
        </Typography>
        <Typography variant="body2">
          {message}
        </Typography>
        <Typography variant="caption" sx={{ color: 'text.secondary' }}>
          {aqiInfo.description}
        </Typography>
      </Box>
    </Alert>
  );
};

export default CustomAlert;