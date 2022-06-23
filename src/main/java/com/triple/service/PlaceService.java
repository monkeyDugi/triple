package com.triple.service;

import com.triple.domain.Place;
import com.triple.repository.PlaceRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PlaceService {
    private final PlaceRepository placeRepository;

    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public Place findById(UUID id) {
        return placeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 장소입니다."));
    }
}
