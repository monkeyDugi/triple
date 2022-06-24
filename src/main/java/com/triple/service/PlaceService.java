package com.triple.service;

import com.triple.domain.Place;
import com.triple.domain.User;
import com.triple.repository.PlaceRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PlaceService {
    private final PlaceRepository placeRepository;

    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public Place findById(UUID id, User user) {
        return placeRepository.findById(id)
                .orElse(Place.emptyPlace(id, user));
    }
}
