import React from 'react';
import { Paper, Typography, Grid, Box, LinearProgress } from '@mui/material';
import { styled } from '@mui/material/styles';

const GlowingBox = styled(Box)(({ theme, color }) => ({
  backgroundColor: 'rgba(0,0,0,0.2)',
  borderRadius: '10px',
  padding: '20px',
  boxShadow: `0 0 15px ${color}`,
  transition: 'all 0.3s ease',
  '&:hover': {
    transform: 'translateY(-5px)',
    boxShadow: `0 0 25px ${color}`,
  },
}));

const StyledProgress = styled(LinearProgress)(({ theme, value }) => ({
  height: 10,
  borderRadius: 5,
  '& .MuiLinearProgress-bar': {
    borderRadius: 5,
  },
}));

const PollutionInfo = ({ data }) => {
  if (!data?.list?.[0]) return null;
  const { main, components } = data.list[0];

  const getAQIInfo = (aqi) => {
    const levels = {
      1: { color: '#00e400', label: 'Excellent' },
      2: { color: '#ffff00', label: 'Bon' },
      3: { color: '#ff7e00', label: 'Modéré' },
      4: { color: '#ff0000', label: 'Mauvais' },
      5: { color: '#8f3f97', label: 'Très Mauvais' }
    };
    return levels[aqi] || { color: '#7e0023', label: 'Dangereux' };
  };

  const aqiInfo = getAQIInfo(main.aqi);

  return (
    <Paper elevation={24} sx={{ p: 3, m: 2, borderRadius: '15px' }}>
      <GlowingBox color={aqiInfo.color}>
        <Typography variant="h4" align="center" sx={{ mb: 3 }}>
          Qualité de l'Air
        </Typography>
        <Typography variant="h2" align="center" sx={{ 
          color: aqiInfo.color,
          fontWeight: 'bold',
          textShadow: `0 0 10px ${aqiInfo.color}`
        }}>
          {aqiInfo.label}
        </Typography>
        <Typography variant="h3" align="center" sx={{ mb: 4 }}>
          AQI: {main.aqi}
        </Typography>
      </GlowingBox>

      <Grid container spacing={3} sx={{ mt: 2 }}>
        {Object.entries(components).map(([key, value]) => (
          <Grid item xs={12} md={6} key={key}>
            <Box sx={{ p: 2, backgroundColor: 'rgba(0,0,0,0.2)', borderRadius: '10px' }}>
              <Typography variant="h6" sx={{ mb: 1 }}>
                {key.toUpperCase()}
              </Typography>
              <StyledProgress
                variant="determinate"
                value={Math.min((value / 100) * 100, 100)}
                sx={{ mb: 1 }}
              />
              <Typography variant="body1" sx={{ textAlign: 'right' }}>
                {value.toFixed(2)} µg/m³
              </Typography>
            </Box>
          </Grid>
        ))}
      </Grid>
    </Paper>
  );
};

export default PollutionInfo;