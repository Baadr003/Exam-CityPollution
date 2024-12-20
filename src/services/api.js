// src/services/api.js
import axios from 'axios';

const BASE_URL = 'http://localhost:8080/api';

export const api = axios.create({
  baseURL: BASE_URL,
  headers: {
    'Content-Type': 'application/json'
  }
});

export const pollutionService = {
  getCurrent: async (lat, lon) => {
    try {
      const response = await api.get(`/pollution/current?lat=${lat}&lon=${lon}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching current pollution:', error);
      throw error;
    }
  },

  getForecast: async (lat, lon) => {
    try {
      const response = await api.get(`/pollution/forecast?lat=${lat}&lon=${lon}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching forecast:', error);
      throw error;
    }
  },

  getHistory: async (lat, lon, start, end) => {
    try {
      const response = await api.get(
        `/pollution/history?lat=${lat}&lon=${lon}&start=${start}&end=${end}`
      );
      return response.data;
    } catch (error) {
      console.error('Error fetching history:', error);
      throw error;
    }
  },

  getAllCitiesPollution: async (cities) => {
    try {
      const requests = cities.map(city => 
        api.get(`/pollution/current?lat=${city.lat}&lon=${city.lon}`)
      );
      const responses = await Promise.all(requests);
      return responses.map((response, index) => ({
        ...response.data,
        cityName: cities[index].name
      }));
    } catch (error) {
      console.error('Error fetching cities pollution:', error);
      throw error;
    }
  }
};

export const authService = {
  login: async (credentials) => {
    try {
      const response = await api.post('/auth/login', credentials);
      if (response.data.success) {
        localStorage.setItem('userId', response.data.userId);
        localStorage.setItem('isAuthenticated', 'true');
      }
      return response.data;
    } catch (error) {
      console.error('Login error:', error);
      throw error;
    }
  },

  register: async (userData) => {
    try {
      const response = await api.post('/auth/register', userData);
      return response.data;
    } catch (error) {
      console.error('Registration error:', error);
      throw error;
    }
  },

  logout: () => {
    localStorage.removeItem('userId');
    localStorage.removeItem('isAuthenticated');
  },

  isAuthenticated: () => {
    return localStorage.getItem('isAuthenticated') === 'true';
  }
};

export const favoriteService = {
  getFavorites: async () => {
    const userId = localStorage.getItem('userId');
    try {
      const response = await api.get(`/favorites?userId=${userId}`);
      return response.data;
    } catch (error) {
      console.error('Error getting favorites:', error);
      throw error;
    }
  },

  addFavorite: async (city) => {
    const userId = localStorage.getItem('userId');
    try {
      const response = await api.post(`/favorites?userId=${userId}`, {
        cityName: city.cityName,
        latitude: city.coord.lat,
        longitude: city.coord.lon
      });
      return response.data;
    } catch (error) {
      console.error('Error adding favorite:', error);
      throw error;
    }
  },

  removeFavorite: async (cityId) => {
    const userId = localStorage.getItem('userId');
    try {
      const response = await api.delete(`/favorites/${cityId}?userId=${userId}`);
      return response.data;
    } catch (error) {
      console.error('Error removing favorite:', error);
      throw error;
    }
  }
};