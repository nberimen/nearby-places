package com.nberimen.codexistCase.service;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Locale;

@Service
@Slf4j
public class GooglePlacesService {

    private static final String GOOGLE_MAPS_API = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=%f,%f&radius=%f&key=%s";

    @Value("${google.places.api.key}")
    private String apiKey;

    public String getPlaces(double longitude, double latitude, double radius) {
        final var url = String.format(Locale.ENGLISH, GOOGLE_MAPS_API, latitude, longitude, radius, apiKey);
        final var client = new OkHttpClient();
        final var request = new Request.Builder().url(url).build();
        try (final var response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                log.error("Unexpected code " + response);
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
