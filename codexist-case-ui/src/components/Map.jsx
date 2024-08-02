import { useEffect, useState, useRef } from 'react';
import { GoogleMap, LoadScript, Marker, InfoWindow } from '@react-google-maps/api';
import { getPlaces } from '../api/apiCalls';

const initialCenter = {
  lat: 40.730610,
  lng: -73.935242
};

const API_KEY = import.meta.env.VITE_GOOGLE_MAPS_API_KEY;
const Map = () => {
  const [places, setPlaces] = useState([]);
  const [selectedPlace, setSelectedPlace] = useState(null);
  const [mapCenter, setMapCenter] = useState(initialCenter);
  const [longitude, setLongitude] = useState('');
  const [latitude, setLatitude] = useState('');
  const [radius, setRadius] = useState('');
  const mapRef = useRef(null);

  const loadPlaces = async (longitude, latitude, radius) => {
    try {
      const response = await getPlaces({longitude, latitude, radius});
      setPlaces(response.data);
      setMapCenter({ lat: parseFloat(latitude), lng: parseFloat(longitude) });
    } catch (error) {
      console.error('Error fetching places:', error);
    }
  };

  useEffect(() => {
    loadPlaces(initialCenter.lng, initialCenter.lat, 1);
  }, []);

  const handleSearch = () => {
    if (longitude && latitude && radius) {
      loadPlaces(longitude, latitude, radius);
    } else {
      alert('Please enter valid longitude, latitude, and radius values');
    }
  };

  return (
    <div>
      <div style={{ marginBottom: '20px' }}>
        <input
          type="text"
          placeholder={longitude}
          value={longitude}
          onChange={(e) => setLongitude(e.target.value)}
          style={{ marginRight: '10px' }}
        />
        <input
          type="text"
          placeholder={latitude}
          value={latitude}
          onChange={(e) => setLatitude(e.target.value)}
          style={{ marginRight: '10px' }}
        />
        <input
          type="text"
          placeholder={radius}
          value={radius}
          onChange={(e) => setRadius(e.target.value)}
          style={{ marginRight: '10px' }}
        />
        <button onClick={handleSearch}>Search</button>
      </div>      
      
      <LoadScript googleMapsApiKey={API_KEY}>
        <GoogleMap mapContainerStyle={{width:'100vw', height:'100vh'}} center={mapCenter} zoom={12} onLoad={(map) => (mapRef.current = map)}>
          {places.map((place, index) => (
            <Marker
              key={index}
              position={{ lat: place.latitude, lng: place.longitude }}
              onClick={() => setSelectedPlace(place)}
              icon={{
                url: 'https://maps.google.com/mapfiles/ms/icons/red-dot.png',
              }}
            />
          ))}
          {selectedPlace && (
            <InfoWindow
              position={{ lat: selectedPlace.latitude, lng: selectedPlace.longitude }}
              onCloseClick={() => setSelectedPlace(null)}
            >
              <div>
                <h2>{selectedPlace.name}</h2>
                <p>{selectedPlace.address}</p>
                <p>lat: {selectedPlace.latitude}</p>
                <p>lng: {selectedPlace.longitude}</p>
              </div>
            </InfoWindow>
          )}
        </GoogleMap>
      </LoadScript>
    </div>
  );
};

export default Map;