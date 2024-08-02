import { useEffect, useState, useRef } from 'react';
import { GoogleMap, LoadScript, Marker, InfoWindow } from '@react-google-maps/api';
import { getPlaces } from '../api/apiCalls';
import Input from '../components/Input';
import Button from '../components/Button';

const initialCenter = {
    lat: 41.015137,
    lng: 28.979530
};

const API_KEY = import.meta.env.VITE_GOOGLE_MAPS_API_KEY;
const HomePage = () => {
    const [places, setPlaces] = useState([]);
    const [selectedPlace, setSelectedPlace] = useState(null);
    const [mapCenter, setMapCenter] = useState(initialCenter);
    const [longitude, setLongitude] = useState('');
    const [latitude, setLatitude] = useState('');
    const [radius, setRadius] = useState('');
    const mapRef = useRef(null);

    const loadPlaces = async (longitude, latitude, radius) => {
        try {
            const response = await getPlaces({ longitude, latitude, radius });
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
            <div className='navbar bg-body-tertiary fixed-top'>
                <Input
                    id="longitude"
                    label="Longitude"
                    type="number"
                    placeholder="Longitude"
                    value={longitude}
                    onChange={(e) => setLongitude(e.target.value)}
                />

                <Input
                    id="latitude"
                    label="Latitude"
                    type="number"
                    placeholder="Latitude"
                    value={latitude}
                    onChange={(e) => setLatitude(e.target.value)}
                />

                <Input
                    id="radius"
                    label="Radius"
                    type="number"
                    placeholder="Radius"
                    value={radius}
                    onChange={(e) => setRadius(e.target.value)}
                />
                <Button disabled={!longitude || !latitude || !radius} onClick={handleSearch} label="Search" />
            </div>
            <div>
                <LoadScript googleMapsApiKey={API_KEY}>
                    <GoogleMap mapContainerStyle={{ width: '100vw', height: '100vh' }} center={mapCenter} zoom={12} onLoad={(map) => (mapRef.current = map)}>
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
        </div>
    );
};

export default HomePage;