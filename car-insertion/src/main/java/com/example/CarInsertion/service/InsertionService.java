package com.example.CarInsertion.service;

import com.example.CarInsertion.model.AuctionDTO;
import com.example.CarInsertion.model.InsertionDTO;

import java.util.List;

public interface InsertionService {
    InsertionDTO insert(InsertionDTO dto);
    AuctionDTO insertAuction(AuctionDTO dto);
    int update(InsertionDTO dto);
    List<InsertionDTO> retrieveCars(String username);
}
