// src/components/PollutionTabs.js
import React, { useState } from 'react';
import { Tabs, Tab, Box, Typography, Paper } from '@mui/material';
import { format } from 'date-fns';

const PollutionDetails = ({ data }) => {
  if (!data?.list?.[0]) return null;
  const { main, components } = data.list[0];

  return (
    <Box sx={{ p: 2 }}>
      <Typography variant="h6">AQI: {main.aqi}</Typography>
      <Typography>CO: {components.co}</Typography>
      <Typography>NO2: {components.no2}</Typography>
      <Typography>O3: {components.o3}</Typography>
      <Typography>PM2.5: {components.pm2_5}</Typography>
      <Typography>PM10: {components.pm10}</Typography>
    </Box>
  );
};

const PollutionTabs = ({ cityName, current, forecast, history }) => {
  const [value, setValue] = useState(0);

  if (!cityName) {
    return (
      <Paper sx={{ p: 2 }}>
        <Typography>Sélectionnez une ville sur la carte</Typography>
      </Paper>
    );
  }

  return (
    <Paper>
      <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
        <Tabs value={value} onChange={(e, v) => setValue(v)}>
          <Tab label="Actuel" />
          <Tab label="Prévisions" />
          <Tab label="Historique" />
        </Tabs>
      </Box>
      <Box sx={{ p: 2 }}>
        <Typography variant="h5" gutterBottom>{cityName}</Typography>
        
        {value === 0 && <PollutionDetails data={current} />}
        
        {value === 1 && forecast?.list?.map((item, index) => (
          <Box key={index} sx={{ mb: 2 }}>
            <Typography variant="subtitle1">
              {format(new Date(item.dt * 1000), 'dd/MM/yyyy HH:mm')}
            </Typography>
            <PollutionDetails data={{ list: [item] }} />
          </Box>
        ))}
        
        {value === 2 && history?.list?.map((item, index) => (
          <Box key={index} sx={{ mb: 2 }}>
            <Typography variant="subtitle1">
              {format(new Date(item.dt * 1000), 'dd/MM/yyyy HH:mm')}
            </Typography>
            <PollutionDetails data={{ list: [item] }} />
          </Box>
        ))}
      </Box>
    </Paper>
  );
};

export default PollutionTabs;