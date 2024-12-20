// src/services/authService.js
const BASE_URL = 'http://localhost:8080/api/auth';

export const authService = {
  async login(credentials) {
    const response = await fetch(`${BASE_URL}/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(credentials),
    });
    const data = await response.json();
    if (data.success) {
      localStorage.setItem('token', data.token);
      localStorage.setItem('userId', data.userId);
    }
    return data;
  },

  async register(userData) {
    const response = await fetch(`${BASE_URL}/register`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(userData),
    });
    return response.json();
  },

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('userId');
  },

  getToken() {
    return localStorage.getItem('token');
  },

  getAuthHeaders() {
    const token = this.getToken();
    return {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    };
  },

  isAuthenticated() {
    return !!this.getToken();
  }
};