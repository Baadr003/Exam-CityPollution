// src/utils/aqiUtils.js
export const AQI_LEVELS = {
  GOOD: {
    range: [0, 50],
    color: '#00e400',
    label: 'Bon',
    description: 'Qualité de l\'air satisfaisante'
  },
  MODERATE: {
    range: [51, 100],
    color: '#ffff00',
    label: 'Modéré',
    description: 'Qualité de l\'air acceptable'
  },
  UNHEALTHY_SENSITIVE: {
    range: [101, 150],
    color: '#ff7e00',
    label: 'Mauvais pour les groupes sensibles',
    description: 'Les personnes sensibles peuvent être affectées'
  },
  UNHEALTHY: {
    range: [151, 200],
    color: '#ff0000',
    label: 'Mauvais',
    description: 'Tout le monde peut commencer à ressentir des effets'
  },
  VERY_UNHEALTHY: {
    range: [201, 300],
    color: '#8f3f97',
    label: 'Très mauvais',
    description: 'Alertes sanitaires'
  },
  HAZARDOUS: {
    range: [301, 500],
    color: '#7e0023',
    label: 'Dangereux',
    description: 'Conditions d\'urgence'
  }
};

export const getAQILevel = (aqi) => {
  for (const [level, data] of Object.entries(AQI_LEVELS)) {
    if (aqi >= data.range[0] && aqi <= data.range[1]) {
      return { level, ...data };
    }
  }
  return AQI_LEVELS.HAZARDOUS;
};