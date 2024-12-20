// src/utils/constants.js
export const CITIES = {
  MOROCCO: [
    { name: 'Casablanca', lat: 33.5731, lon: -7.5898 },
    { name: 'Rabat', lat: 34.0209, lon: -6.8416 },
    { name: 'Marrakech', lat: 31.6295, lon: -7.9811 },
    { name: 'Fès', lat: 34.0333, lon: -5.0000 },
    { name: 'Tanger', lat: 35.7595, lon: -5.8340 },
    { name: 'El Jadida', lat: 33.2316, lon: -8.5007 },
    { name: 'Agadir', lat: 30.4278, lon: -9.5981 },
    { name: 'Oujda', lat: 34.6814, lon: -1.9086 },
    { name: 'Nador', lat: 35.1681, lon: -2.9335 },
    { name: 'Tetouan', lat: 35.5785, lon: -5.3684 },
    { name: 'Kenitra', lat: 34.2610, lon: -6.5802 },
    { name: 'Beni Mellal', lat: 32.3372, lon: -6.3498 },
    { name: 'Safi', lat: 32.3008, lon: -9.2275 },
    { name: 'Essaouira', lat: 31.5125, lon: -9.7749 },
    { name: 'Taza', lat: 34.2133, lon: -3.9986 },
    { name: 'Ouarzazate', lat: 30.9189, lon: -6.8934 },
    { name: 'Tiznit', lat: 29.6974, lon: -9.7316 },
    { name: 'Tan-Tan', lat: 28.4378, lon: -11.1014 },
    { name: 'Guelmim', lat: 28.9870, lon: -10.0574 },
    { name: 'Laâyoune', lat: 27.1253, lon: -13.1625 },
    { name: 'Sous-Massa', lat: 29.4064, lon: -10.0299 },
    { name: 'Guelmim-Oued Noun', lat: 28.4445, lon: -10.0634 },
    { name: 'Errachidia', lat: 31.9314, lon: -4.4246 },
    { name: 'Figuig', lat: 32.1138, lon: -1.2296 },
    { name: 'Zagora', lat: 30.3322, lon: -5.8380 }
    
  ],
  SPAIN: [
    { name: 'Madrid', lat: 40.4168, lon: -3.7038 },
    { name: 'Barcelona', lat: 41.3851, lon: 2.1734 },
    { name: 'Valencia', lat: 39.4699, lon: -0.3763 },
    { name: 'Seville', lat: 37.3891, lon: -5.9845 },
    { name: 'Bilbao', lat: 43.2627, lon: -2.9253 }
  ],
  FRANCE: [
    { name: 'Paris', lat: 48.8566, lon: 2.3522 },
    { name: 'Marseille', lat: 43.2965, lon: 5.3698 },
    { name: 'Lyon', lat: 45.7640, lon: 4.8357 },
    { name: 'Toulouse', lat: 43.6047, lon: 1.4442 },
    { name: 'Nice', lat: 43.7102, lon: 7.2620 }
  ],
  ITALY: [
    { name: 'Rome', lat: 41.9028, lon: 12.4964 },
    { name: 'Milan', lat: 45.4642, lon: 9.1900 },
    { name: 'Naples', lat: 40.8518, lon: 14.2681 },
    { name: 'Turin', lat: 45.0703, lon: 7.6869 },
    { name: 'Florence', lat: 43.7696, lon: 11.2558 }
  ],
  PORTUGAL: [
    { name: 'Lisbon', lat: 38.7223, lon: -9.1393 },
    { name: 'Porto', lat: 41.1579, lon: -8.6291 },
    { name: 'Braga', lat: 41.5518, lon: -8.4229 },
    { name: 'Coimbra', lat: 40.2033, lon: -8.4103 },
    { name: 'Faro', lat: 37.0194, lon: -7.9322 }
  ]
};

// Combine all cities for the API calls
export const ALL_CITIES = Object.values(CITIES).flat();

// Center map on Mediterranean region
export const MAP_CENTER = [41.9028, 2.9534]; // Centered between the countries
export const DEFAULT_ZOOM = 5; // Adjusted for wider view

// Color scheme for different AQI levels
export const AQI_COLORS = {
  GOOD: '#00e400',
  MODERATE: '#ffff00',
  UNHEALTHY_SENSITIVE: '#ff7e00',
  UNHEALTHY: '#ff0000',
  VERY_UNHEALTHY: '#8f3f97',
  HAZARDOUS: '#7e0023'
};