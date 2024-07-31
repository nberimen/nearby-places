package com.nberimen.codexistCase.repository;

import com.nberimen.codexistCase.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    List<Place> findByLongitudeAndLatitudeAndRadius(double longitude, double latitude, double radius);
}