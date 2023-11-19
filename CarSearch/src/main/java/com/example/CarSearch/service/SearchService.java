package com.example.CarSearch.service;

import com.example.CarSearch.model.ResponseDTO;
import com.example.CarSearch.model.SearchDTO;
import org.springframework.web.bind.annotation.RequestBody;

public interface SearchService {
    public ResponseDTO getCar(SearchDTO dto);
}
