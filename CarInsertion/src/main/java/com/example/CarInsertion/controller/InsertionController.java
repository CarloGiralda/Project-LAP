package com.example.CarInsertion.controller;

import com.example.CarInsertion.model.Car;
import com.example.CarInsertion.model.InsertionDTO;
import com.example.CarInsertion.model.Offer;
import com.example.CarInsertion.model.Utilities;
import com.example.CarInsertion.service.InsertionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class InsertionController {
    private InsertionService insertionService;
    private InsertionDTO dto;

    public InsertionController(InsertionService insertionService) {
        super();
        this.insertionService = insertionService;
    }

    @GetMapping(path = "/insertcar")
    public String getCarForm() {
        return "carForm";
    }

    @GetMapping(path = "/insertutilities")
    public String getUtilitiesForm() {
        return "utilitiesForm";
    }

    @GetMapping(path = "/insertoffer")
    public String getOfferForm() {
        dto = new InsertionDTO();
        return "offerForm";
    }

    @PostMapping(path = "/insertcar")
    public String postCar(@RequestBody Car car) {
        if(dto == null) {
            dto = new InsertionDTO();
        }
        dto.setCar(car);
        return "redirect:/insertutilities";
    }

    @PostMapping(path = "/insertutilities")
    public String postUtilities(@RequestBody Utilities ut) {
        if(dto == null) {
            dto = new InsertionDTO();
        }
        dto.setUtilities(ut);
        return "redirect:/insertSuccess";
    }

    @PostMapping(path = "/insertoffer")
    public String postOffer(@RequestBody Offer offer) {
        if(dto == null) {
            dto = new InsertionDTO();
        }
        dto.setOffer(offer);
        return "redirect:/insertcar";
    }

    @GetMapping(path = "/insertSuccess")
    public String success() {
        insertionService.insert(dto);
        return "success.html";
    }
}
