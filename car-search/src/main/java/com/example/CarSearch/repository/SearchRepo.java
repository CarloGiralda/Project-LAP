package com.example.CarSearch.repository;

import com.example.CarSearch.model.SearchDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchRepo {
    List<SearchDTO> getCarOfferUtFromSearchDTO(SearchDTO dto);
    List<SearchDTO> getCarOfferUtFromId(Long id);
    List<SearchDTO> getCars();
    String getRenter(Long id);
}
