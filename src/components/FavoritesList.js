// src/components/FavoritesList.js
import React, { useEffect, useState } from 'react';
import { Box, Typography, List, ListItem, ListItemText, IconButton, Divider } from '@mui/material';
import { Delete } from '@mui/icons-material';
import { favoriteService } from '../services/api';
import { getAQILevel } from '../utils/aqiUtils';

const FavoritesList = () => {
  const [favorites, setFavorites] = useState([]);

  useEffect(() => {
    loadFavorites();
  }, []);

  const loadFavorites = async () => {
    try {
      const data = await favoriteService.getFavorites();
      setFavorites(data);
    } catch (error) {
      console.error('Error loading favorites:', error);
    }
  };

  const handleRemoveFavorite = async (cityId) => {
    try {
      await favoriteService.removeFavorite(cityId);
      loadFavorites();
    } catch (error) {
      console.error('Error removing favorite:', error);
    }
  };

  return (
    <Box sx={{ mt: 3 }}>
      <Typography variant="h6" sx={{ mb: 3, color: '#00fff5' }}>
        Villes Favorites
      </Typography>
      {favorites.length === 0 ? (
        <Typography>Aucune ville favorite</Typography>
      ) : (
        <List>
          {favorites.map((city, index) => (
            <React.Fragment key={city.id}>
              <ListItem
                secondaryAction={
                  <IconButton 
                    edge="end" 
                    onClick={() => handleRemoveFavorite(city.id)}
                    sx={{ color: '#ff4444' }}
                  >
                    <Delete />
                  </IconButton>
                }
              >
                <ListItemText
                  primary={city.cityName}
                  secondary={`AQI: ${city.currentAqi || 'N/A'}`}
                  sx={{
                    '& .MuiListItemText-primary': { color: '#fff' },
                    '& .MuiListItemText-secondary': { 
                      color: getAQILevel(city.currentAqi)?.color || '#fff'
                    }
                  }}
                />
              </ListItem>
              {index < favorites.length - 1 && <Divider />}
            </React.Fragment>
          ))}
        </List>
      )}
    </Box>
  );
};

export default FavoritesList;