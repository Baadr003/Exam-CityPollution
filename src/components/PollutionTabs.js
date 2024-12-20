// src/components/PollutionTabs.js
import React, { useState, useEffect } from 'react';
import { Box, Typography, Button, Tabs, Tab, Snackbar, Alert, CircularProgress } from '@mui/material';
import { Star, StarBorder } from '@mui/icons-material';
import { styled } from '@mui/material/styles';
import { favoriteService } from '../services/favoriteService';

const StyledTabs = styled(Tabs)({
  '& .MuiTabs-indicator': {
    backgroundColor: '#00fff5',
  }
});

const StyledTab = styled(Tab)({
  color: '#fff',
  '&.Mui-selected': {
    color: '#00fff5',
  }
});

const PollutionDetails = ({ data }) => {
  if (!data?.list?.[0]) return null;
  const { main, components } = data.list[0];

  return (
    <Box sx={{ 
      p: 2, 
      bgcolor: 'rgba(0,0,0,0.2)', 
      borderRadius: '10px',
      border: '1px solid rgba(0, 255, 245, 0.1)',
    }}>
      <Typography variant="h6" sx={{ color: '#00fff5' }}>AQI: {main.aqi}</Typography>
      {Object.entries(components).map(([key, value]) => (
        <Typography key={key} sx={{ my: 1 }}>
          {key.toUpperCase()}: {value.toFixed(2)}
        </Typography>
      ))}
    </Box>
  );
};

const PollutionTabs = ({ cityName, current, forecast, history, isAuthenticated }) => {
  const [value, setValue] = useState(0);
  const [isFavorite, setIsFavorite] = useState(false);
  const [favorites, setFavorites] = useState([]);
  const [loading, setLoading] = useState(false);
  const [notification, setNotification] = useState({
    open: false,
    message: '',
    severity: 'info'
  });

  useEffect(() => {
    if (isAuthenticated) {
      loadFavorites();
    }
  }, [isAuthenticated]);

  useEffect(() => {
    if (cityName && favorites.length > 0) {
      const isCityFavorite = favorites.some(fav => 
        fav.latitude === current?.coord?.lat && 
        fav.longitude === current?.coord?.lon
      );
      setIsFavorite(isCityFavorite);
    }
  }, [cityName, favorites, current]);

  const loadFavorites = async () => {
    try {
      const response = await favoriteService.getFavorites();
      setFavorites(response);
    } catch (error) {
      showNotification('Erreur lors du chargement des favoris', 'error');
    }
  };

  const handleAddToFavorites = async () => {
    if (!isAuthenticated) {
      showNotification('Veuillez vous connecter pour ajouter des favoris', 'warning');
      return;
    }

    setLoading(true);
    try {
      if (isFavorite) {
        const favoriteCity = favorites.find(fav => 
          fav.latitude === current.coord.lat && 
          fav.longitude === current.coord.lon
        );
        await favoriteService.removeFavorite(favoriteCity.id);
        showNotification('Ville retirée des favoris', 'success');
      } else {
        await favoriteService.addFavorite({
          cityName,
          coord: {
            lat: current.coord.lat,
            lon: current.coord.lon
          }
        });
        showNotification('Ville ajoutée aux favoris', 'success');
      }
      await loadFavorites();
    } catch (error) {
      showNotification('Erreur lors de la gestion des favoris', 'error');
    } finally {
      setLoading(false);
    }
  };

  const showNotification = (message, severity = 'info') => {
    setNotification({ open: true, message, severity });
  };

  if (!cityName) {
    return (
      <Box sx={{ p: 2, textAlign: 'center' }}>
        <Typography>Sélectionnez une ville sur la carte</Typography>
      </Box>
    );
  }

  return (
    <Box sx={{ 
      p: 2,
      borderRadius: '15px',
      backgroundColor: 'rgba(23, 42, 69, 0.9)',
    }}>
      <Box sx={{ 
        display: 'flex', 
        justifyContent: 'space-between', 
        alignItems: 'center',
        mb: 2 
      }}>
        <Typography variant="h5" sx={{ color: '#00fff5' }}>
          {cityName}
        </Typography>
        {isAuthenticated && (
          <Button
            startIcon={loading ? <CircularProgress size={20} /> : (isFavorite ? <Star /> : <StarBorder />)}
            onClick={handleAddToFavorites}
            disabled={loading}
            sx={{ 
              color: isFavorite ? '#FFD700' : '#00fff5',
              borderColor: isFavorite ? '#FFD700' : '#00fff5',
              '&:hover': {
                color: '#FFD700',
                borderColor: '#FFD700'
              }
            }}
            variant="outlined"
          >
            {isFavorite ? 'Retirer des favoris' : 'Ajouter aux favoris'}
          </Button>
        )}
      </Box>

      <StyledTabs value={value} onChange={(e, v) => setValue(v)} centered>
        <StyledTab label="Actuel" />
        <StyledTab label="Prévisions" />
        <StyledTab label="Historique" />
      </StyledTabs>

      <Box sx={{ mt: 3 }}>
        {value === 0 && <PollutionDetails data={current} />}
        {value === 1 && forecast?.list?.map((item, index) => (
          <PollutionDetails key={index} data={{ list: [item] }} />
        ))}
        {value === 2 && history?.list?.map((item, index) => (
          <PollutionDetails key={index} data={{ list: [item] }} />
        ))}
      </Box>

      <Snackbar
        open={notification.open}
        autoHideDuration={4000}
        onClose={() => setNotification(prev => ({ ...prev, open: false }))}
      >
        <Alert severity={notification.severity} onClose={() => setNotification(prev => ({ ...prev, open: false }))}>
          {notification.message}
        </Alert>
      </Snackbar>
    </Box>
  );
};

export default PollutionTabs;