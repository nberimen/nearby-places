package com.nberimen.codexistCase.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nberimen.codexistCase.constants.Constants;
import com.nberimen.codexistCase.dto.PlaceDto;
import com.nberimen.codexistCase.entity.Place;
import com.nberimen.codexistCase.mapper.PlaceMapper;
import com.nberimen.codexistCase.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaceService {

    private static final String NOMINATIM_API_ENDPOINT = "https://nominatim.openstreetmap.org/search?format=json&q=amenity&limit=50&viewbox=%f,%f,%f,%f";
    private final PlaceRepository placeRepository;

    public List<PlaceDto> getPlaces(final double longitude, final double latitude, final double radius) {
        final var cachedPlaces = getCachedPlaces(longitude, latitude, radius);
        if(!CollectionUtils.isEmpty(cachedPlaces)){
            return PlaceMapper.INSTANCE.convertToPlaceDtoList(cachedPlaces);
        }

        final var url = getNominatimUrl(longitude, latitude, radius);
        return getPlacesFromNominatim(url, radius);
    }

    private List<Place> getCachedPlaces(double longitude, double latitude, double radius) {
        final var latMin = latitude - radius;
        final var latMax = latitude + radius;
        final var lonMin = longitude - radius;
        final var lonMax = longitude + radius;
        return placeRepository.findByLatitudeBetweenAndLongitudeBetween(latMin, latMax, lonMin, lonMax);
    }

    private List<PlaceDto> getPlacesFromNominatim(final String url, final double radius) {
        try {
            final var client = HttpClientBuilder.create().build();
            final var request = new HttpGet(url);
            final var response = client.execute(request);

            if (response.getStatusLine().getStatusCode() == Constants.OK_STATUS_CODE) {
                final var json = EntityUtils.toString(response.getEntity());
                final var places = parsePlaces(json, radius);
                saveAll(places);
                return PlaceMapper.INSTANCE.convertToPlaceDtoList(places);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return Collections.emptyList();
    }

    private String getNominatimUrl(final double longitude, final double latitude, final double radius) {
        final var west = longitude - radius;
        final var south = latitude - radius;
        final var east = longitude + radius;
        final var north = latitude + radius;
        return String.format(Locale.ENGLISH, NOMINATIM_API_ENDPOINT, west, south, east, north);
    }

    private List<Place> parsePlaces(final String json, final double radius) {
        try {
            final var mapper = new ObjectMapper();
            final var jsonList = mapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {});
            return jsonList.stream().map(item -> Place.builder()
                    .latitude(Double.parseDouble(item.get("lat").toString()))
                    .longitude(Double.parseDouble(item.get("lon").toString()))
                    .radius(radius)
                    .name(item.get("name").toString())
                    .build())
                    .toList();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return Collections.emptyList();
    }

    private void saveAll(final List<Place> placeList) {
        placeRepository.saveAll(placeList);
        log.info("Save has been successfully.");
    }

}
