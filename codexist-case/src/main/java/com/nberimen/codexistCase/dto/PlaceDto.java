package com.nberimen.codexistCase.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlaceDto {
    private double latitude;
    private double longitude;
    private double radius;
    private String name;
}
