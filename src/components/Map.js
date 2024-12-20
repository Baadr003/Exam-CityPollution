// src/components/Map.js
import React, { useEffect } from 'react';
import { MapContainer, TileLayer, Marker, Popup, useMap } from 'react-leaflet';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import 'leaflet.heat';
import { MAP_CENTER, DEFAULT_ZOOM } from '../utils/constants';


// Fix Leaflet icon issue
delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: require('leaflet/dist/images/marker-icon-2x.png'),
  iconUrl: require('leaflet/dist/images/marker-icon.png'),
  shadowUrl: require('leaflet/dist/images/marker-shadow.png'),
});

const HeatLayer = ({ data }) => {
  const map = useMap();

  useEffect(() => {
    if (!data) return;

    const points = data.map(city => [
      city.coord.lat,
      city.coord.lon,
      city.list[0].main.aqi * 3 // Multiply for better visibility
    ]);

    const heat = L.heatLayer(points, {
      radius: 25,
      blur: 15,
      maxZoom: 10,
      gradient: {
        0.4: '#00ff00',
        0.6: '#ffff00',
        0.8: '#ff8000',
        1.0: '#ff0000'
      }
    }).addTo(map);

    return () => {
      map.removeLayer(heat);
    };
  }, [map, data]);

  return null;
};

const PollutionMap = ({ pollutionData, onCitySelect }) => {
  return (
    <MapContainer
  center={MAP_CENTER}
  zoom={DEFAULT_ZOOM}
  style={{ height: '70vh', width: '100%' }}
>
      <TileLayer
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        attribution='&copy; OpenStreetMap contributors'
      />
      {pollutionData && <HeatLayer data={pollutionData} />}
      
      {pollutionData?.map((city, index) => (
        <Marker
          key={index}
          position={[city.coord.lat, city.coord.lon]}
          eventHandlers={{
            click: () => onCitySelect?.(city)
          }}
        >
          <Popup>
            <div>
              <h3>{city.cityName}</h3>
              <p>AQI: {city.list[0].main.aqi}</p>
              <p>CO: {city.list[0].components.co}</p>
              <p>NO2: {city.list[0].components.no2}</p>
              <p>O3: {city.list[0].components.o3}</p>
              <p>PM2.5: {city.list[0].components.pm2_5}</p>
            </div>
          </Popup>
        </Marker>
      ))}
    </MapContainer>
  );
};

export default PollutionMap;