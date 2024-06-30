package com.example.CarSearch.controller;

import com.example.CarSearch.model.*;
import com.example.CarSearch.service.SearchService;
import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequestMapping(path = "/carsearch")
@RestController
public class SearchController {
    private SearchService searchService;

    public SearchController(SearchService searchService) {
        super();
        this.searchService = searchService;
    }

    @PostMapping(path = "/searchbar")
    public ResponseEntity<?> searchForm(@RequestBody SearchDTO sdto) {
        List<SearchDTO> cars = searchService.getCar(sdto);
        ArrayList<ReturnDTO> response = new ArrayList<>();
        for (SearchDTO car : cars) {

            log.info("CAR PRICE: {} {}",car.getOffer().getPricePerHour(),Objects.equals(car.getOffer().getPricePerHour(), "-1") );
            if (!Objects.equals(car.getOffer().getPricePerHour(), "-1")){
                response.add(new ReturnDTO(car.getCar().getCid(),
                        car.getCar().getBrand(),
                        car.getCar().getModel(),
                        car.getOffer().getPricePerHour(),
                        car.getCar().getImage()));
            }

        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/getCarById/{id}")
    public ResponseEntity<?> getCarById(@PathVariable Long id) {
        List<SearchDTO> search = searchService.getSearchDTOById(id);
        if (search.size() != 1) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        SearchDTO response = search.get(0);

        // search service availability
        Boolean availability = searchService.getAvailability(id);

        // set availability
        response.getOffer().setAvailable(availability);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/getCarNameById/{id}")
    public ResponseEntity<?> returnName(@PathVariable Long id) {
        String car = searchService.getCarById(id);
        String[] names = car.split(",");
        String carName = names[0] + " model " + names[1];
        return new ResponseEntity<>(carName, HttpStatus.OK);
    }

    @GetMapping(path = "/search")
    @ResponseBody
    public ResponseEntity<?> searchMap(@RequestParam("query") String query, @RequestParam("radius") String radius) {
        String coordinates = searchService.convertStringIntoCoordinates(query);
        JsonArray cars = searchService.getCarsInsideRange(coordinates, Long.parseLong(radius));
        JsonObject location = new JsonObject();
        String[] lat_lon = coordinates.split("/");
        location.addProperty("lat", lat_lon[0]);
        location.addProperty("lon", lat_lon[1]);
        location.add("response", cars);
        return new ResponseEntity<>(location, HttpStatus.OK);
    }
/*
    @GetMapping(path = "/getCarsWithinRange")
    public ResponseEntity<String> returnCars(@RequestParam String position, @RequestParam Long range) {
        JsonArray cars = searchService.getCarsInsideRange(position, range);
        Gson gson = new Gson();
        String json = gson.toJson(cars);
        return new ResponseEntity<>(json, HttpStatus.OK);
    }
*/
    @GetMapping(path = "/getCarPreviewById/{id}")
    public ResponseEntity<?> getCarPreviewById(@PathVariable Long id) {
        try {
            CarPreviewDTO searchPreview = searchService.getCarPreview(id);
            return new ResponseEntity<>(searchPreview, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/getPriceById/{id}")
    public ResponseEntity<?> getPriceById(@PathVariable Long id) {
        List<SearchDTO> dtos = searchService.getSearchDTOById(id);
        if (dtos.size() != 1) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(dtos.get(0).getOffer().getPricePerHour(), HttpStatus.OK);
    }

    @GetMapping(path = "/getRenterUsername")
    public ResponseEntity<?> getRenterByUsername(@RequestParam Long id) {
        String renter = searchService.getRenter(id);
        JsonObject obj = new JsonObject();
        obj.addProperty("renter", renter);
        String object = obj.toString();
        return new ResponseEntity<>(object, HttpStatus.OK);
    }

    @GetMapping(path = "/getCarModelBrandImage/{id}")
    public ResponseEntity<?> getCarModelBrandImage(@PathVariable Long id) {
        String car = searchService.getCarById(id);
        String[] names = car.split(",");
        String brand = names[0];
        String model = names[1];
        byte[] image = searchService.getCarImageById(id);
        ReturnDTO dto = new ReturnDTO(id, brand, model, "", image);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
