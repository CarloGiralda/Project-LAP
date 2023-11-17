package com.example.CarSearch.controller;

import com.example.CarSearch.model.*;
import com.example.CarSearch.repository.*;
import com.example.CarSearch.service.SearchService;
import com.example.CarSearch.service.SearchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.data.jpa.domain.Specification.where;

@Controller
public class SearchController {

    private SearchService searchService;

    private SearchDTO sdto;

    public SearchController(SearchService searchService) {
        super();
        this.searchService = searchService;
    }

    @GetMapping(path = "/")
    public String createInsertForm(Model model) {
        Car car = new Car();
        Offer offer = new Offer();
        Utilities ut = new Utilities();
        model.addAttribute("car", car);
        model.addAttribute("offer", offer);
        model.addAttribute("utilities", ut);
        return "form";
    }

    @PostMapping(path = "/cars")
    public String filters(@ModelAttribute("car") Car car, @ModelAttribute("offer") Offer offer, @ModelAttribute("utilities") Utilities ut) {
        sdto = new SearchDTO(car, offer, ut);
        return "redirect:/cars";
    }

    @GetMapping(path = "/cars")
    public String cars(Model model) {
        ResponseDTO dto = searchService.getCar(sdto);
        model.addAttribute("cars", dto.getCars());
        return "cars";
    }
}
