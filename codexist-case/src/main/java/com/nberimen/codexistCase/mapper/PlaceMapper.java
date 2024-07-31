package com.nberimen.codexistCase.mapper;

import com.nberimen.codexistCase.dto.PlaceDto;
import com.nberimen.codexistCase.entity.Place;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PlaceMapper {
    PlaceMapper INSTANCE = Mappers.getMapper(PlaceMapper.class);

    List<PlaceDto> convertToPlaceDtoList(List<Place> places);
}
