package com.nberimen.nearbyPlaces.mapper;

import com.nberimen.nearbyPlaces.dto.PlaceDto;
import com.nberimen.nearbyPlaces.entity.Place;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PlaceMapper {
    PlaceMapper INSTANCE = Mappers.getMapper(PlaceMapper.class);

    List<PlaceDto> convertToPlaceDtoList(List<Place> places);
}
