import axios from "axios";

export const getPlaces = ({longitude, latitude, radius}) => {
    return axios.get('/api/places', {
        params: {longitude, latitude, radius}
    }); 
}
