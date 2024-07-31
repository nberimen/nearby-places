package com.nberimen.codexistCase.service;

import com.nberimen.codexistCase.dto.PlaceDto;
import com.nberimen.codexistCase.entity.Place;
import com.nberimen.codexistCase.mapper.PlaceMapper;
import com.nberimen.codexistCase.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;

    public List<PlaceDto> getPlaces(final double longitude, final double latitude, final double radius) {
        final var places = placeRepository.findByLongitudeAndLatitudeAndRadius(longitude, latitude, radius);
        return PlaceMapper.INSTANCE.convertToPlaceDtoList(places);
    }

}
