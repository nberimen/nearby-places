package com.nberimen.codexistCase.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nberimen.codexistCase.dto.PlaceDto;
import com.nberimen.codexistCase.entity.Place;
import com.nberimen.codexistCase.mapper.PlaceMapper;
import com.nberimen.codexistCase.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final GooglePlacesService googlePlacesService;



    public List<PlaceDto> getPlaces(final double longitude, final double latitude, final double radius) {
        final var cachedPlaces = getCachedPlaces(longitude, latitude, radius);
        if(!CollectionUtils.isEmpty(cachedPlaces)){
            return PlaceMapper.INSTANCE.convertToPlaceDtoList(cachedPlaces);
        }
        return getPlacesFromGoogle(latitude, longitude, radius);
    }

    private List<PlaceDto> getPlacesFromGoogle(final double latitude, final double longitude, final double radius) {
        final var json = googlePlacesService.getPlaces(longitude, latitude, radius);
        try {
        return PlaceMapper.INSTANCE.convertToPlaceDtoList(parsePlaces(json));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private List<Place> getCachedPlaces(double longitude, double latitude, double radius) {
        final var latMin = latitude - radius;
        final var latMax = latitude + radius;
        final var lonMin = longitude - radius;
        final var lonMax = longitude + radius;
        return placeRepository.findByLatitudeBetweenAndLongitudeBetween(latMin, latMax, lonMin, lonMax);
    }

    private List<Place> parsePlaces(final String json) throws JsonProcessingException {
        final var responseMap = new ObjectMapper().readValue(json, Map.class);
        final var results = (List<Map<String, Object>>) responseMap.get("results");

        final var places = results.stream().map(result -> {
            final var location = (Map<String, Double>) ((Map<String, Object>) result.get("geometry")).get("location");
            return Place.builder()
                    .name((String) result.get("name"))
                    .address((String) result.get("vicinity"))
                    .latitude(location.get("lat"))
                    .longitude(location.get("lng"))
                    .build();
        }).toList();
        return saveAll(places);
    }

    private List<Place> saveAll(final List<Place> placeList) {
        final var places = placeRepository.saveAll(placeList);
        log.info("Save has been successfully.");
        return places;
    }

}
