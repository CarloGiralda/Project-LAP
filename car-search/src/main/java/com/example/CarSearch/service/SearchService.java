package com.example.CarSearch.service;

import com.example.CarSearch.model.CarPreviewDTO;
import com.example.CarSearch.model.SearchDTO;
import com.google.gson.JsonArray;

import java.util.List;

public interface SearchService {
    List<SearchDTO> getCar(SearchDTO dto);
    CarPreviewDTO getCarPreview(Long carId);
    List<SearchDTO> getSearchDTOById(Long id);
    String getCarById(Long id);
    String getRenter(Long id);
    JsonArray getCarsInsideRange(String position, Long range);
    Boolean getAvailability(Long id);
    String convertStringIntoCoordinates(String location);
}
