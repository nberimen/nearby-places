package com.nberimen.nearbyPlaces.controller;

import com.nberimen.nearbyPlaces.constants.Constants;
import com.nberimen.nearbyPlaces.dto.PlaceDto;
import com.nberimen.nearbyPlaces.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping(Constants.PLACES_API)
@RequiredArgsConstructor
public class PlacesController {

    private final PlaceService placeService;

    @GetMapping
    public ResponseEntity<List<PlaceDto>> getPlaces(@RequestParam double longitude, @RequestParam double latitude, @RequestParam double radius){
        return ResponseEntity.ok(placeService.getPlaces(longitude, latitude, radius));
    }
}
