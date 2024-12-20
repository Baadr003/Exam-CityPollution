// src/components/CustomNotification.js
import React from 'react';
import { Snackbar, Alert, Typography, Box } from '@mui/material';
import { getAQILevel } from '../utils/aqiUtils';

const CustomNotification = ({ open, message, onClose, aqi }) => {
  const aqiInfo = getAQILevel(aqi);

  return (
    <Snackbar
      open={open}
      autoHideDuration={6000}
      onClose={onClose}
      anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
    >
      <Alert
        onClose={onClose}
        severity="warning"
        sx={{
          backgroundColor: `${aqiInfo?.color}15`,
          borderLeft: `6px solid ${aqiInfo?.color}`,
          '& .MuiAlert-icon': {
            color: aqiInfo?.color
          }
        }}
      >
        <Box>
          <Typography variant="subtitle1" sx={{ fontWeight: 'bold' }}>
            {aqiInfo?.label}
          </Typography>
          <Typography variant="body2">
            {message}
          </Typography>
        </Box>
      </Alert>
    </Snackbar>
  );
};

export default CustomNotification;