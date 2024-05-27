package com.example.CarSearch.service;

import com.example.CarSearch.model.*;
import com.example.CarSearch.repository.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@Service
public class SearchServiceImpl implements SearchService {
    private final SearchRepo searchRepo;
    private final CarRepo carRepo;
    private final DiscoveryClientService discoveryClientService;
    private final RestTemplate restTemplate;

    @Autowired
    public SearchServiceImpl(SearchRepo searchRepo, CarRepo carRepo, DiscoveryClientService discoveryClientService, RestTemplate restTemplate) {
        this.searchRepo = searchRepo;
        this.carRepo = carRepo;
        this.discoveryClientService = discoveryClientService;
        this.restTemplate = restTemplate;
    }

    @Override
    public CarPreviewDTO getCarPreview(Long carId){
        Car car = carRepo.findCarPreviewById(carId);
        log.info("BRAND PREVIEW: " + car.getBrand());
        return new CarPreviewDTO(car.getBrand(), car.getModel(), car.getEngine(), car.getYear());
    }

    @Override
    public List<SearchDTO> getCar(SearchDTO dto) {
        List<SearchDTO> result = new ArrayList<>();
        // if the brand/model is not an empty string, then proceed with the classification
        if (!dto.getCar().getBrand().equalsIgnoreCase("")) {
            // by default both "model" and "brand" are set to the same value (the one inserted into the search bar)
            String[] brand_model = dto.getCar().getModel().split(" ");
            // Search: "[model]" or "[brand]"
            if (brand_model.length == 1) {
                // two different cases:
                // - first case: the string refers to the brand
                // so we set the brand to it and the model to null (because there is only one word)
                dto.getCar().setBrand(brand_model[0]);
                dto.getCar().setModel(null);
                List<SearchDTO> model_dtos = searchRepo.getCarOfferUtFromSearchDTO(dto);
                // - second case: the string refers to the model
                // so we set the model to it and the brand to null (because there is only one word)
                dto.getCar().setModel(brand_model[0]);
                dto.getCar().setBrand(null);
                List<SearchDTO> brand_dtos = searchRepo.getCarOfferUtFromSearchDTO(dto);
                // merge the two results obtained
                result = Stream.concat(model_dtos.stream(), brand_dtos.stream()).toList();
            }
            // Search: "[brand] [model]", then assign them respectively
            if (brand_model.length == 2) {
                dto.getCar().setBrand(brand_model[0]);
                dto.getCar().setModel(brand_model[1]);
                result = searchRepo.getCarOfferUtFromSearchDTO(dto);
            }
            // Search: "[brand] model [model]", then ignore the "model" word
            else if (brand_model.length == 3 && brand_model[1].equals("model")) {
                dto.getCar().setBrand(brand_model[0]);
                dto.getCar().setModel(brand_model[2]);
                result = searchRepo.getCarOfferUtFromSearchDTO(dto);
            }
            // if it contains more than 3 words or the middle one is not "model", then return an empty list
        }
        // otherwise set them to null (so that they will not be included in the search)
        else {
            dto.getCar().setBrand(null);
            dto.getCar().setModel(null);
            result = searchRepo.getCarOfferUtFromSearchDTO(dto);
        }
        return result;
    }

    @Override
    public List<SearchDTO> getSearchDTOById(Long id) {
        return searchRepo.getCarOfferUtFromId(id);
    }

    @Override
    public Boolean getAvailability(Long id){
        String carBookUrl = discoveryClientService.getServiceUrl("CARBOOK-SERVICE");
        String carAvailabilityUrl = carBookUrl + "/reservation" + "/getCarAvailability/" + id.toString();

        // REST call to book service to know car availability
        ResponseEntity<Boolean> availabilityResponse = restTemplate.getForEntity(carAvailabilityUrl, Boolean.class);
        if (availabilityResponse.getStatusCode() != HttpStatus.OK){
            log.info("error, cannot check car validity");
            throw new RuntimeException("error, cannot check car validity");
        }

        log.info("availability response: {}", availabilityResponse.getBody());
        return availabilityResponse.getBody();
    }

    @Override
    public String getCarById(Long id) {
        return carRepo.findCarById(id);
    }

    @Override
    public String getRenter(Long id) {
        return searchRepo.getRenter(id);
    }

    @Override
    public JsonArray getCarsInsideRange(String position, Long range) {
        List<SearchDTO> cars = searchRepo.getCars();
        JsonArray carsInsideRange = new JsonArray();
        String[] lat_lon = position.split("/");
        // latitude is lat_lon[0]
        // longitude is lat_lon[1]
        for (int i = 0; i < cars.size(); i++) {
            String zone = cars.get(i).getOffer().getZoneLocation();
            String[] car_lat_lon = zone.split("/");
            // distance(x,y) = ( (x1-y1)^2 + (x2-y2)^2 )^0.5
            double distance = computeDistance(Double.parseDouble(lat_lon[0]), Double.parseDouble(lat_lon[1]), Double.parseDouble(car_lat_lon[0]), Double.parseDouble(car_lat_lon[1]));
            if (distance < range) {
                JsonObject object=new JsonObject();
                String[] pos=cars.get(i).getOffer().getZoneLocation().split("/");
                object.addProperty("lat",pos[0]);
                object.addProperty("lon",pos[1]);
                object.addProperty("name",cars.get(i).getCar().getBrand() + " model " + cars.get(i).getCar().getModel());
                object.addProperty("id",i);
                object.addProperty("cid",cars.get(i).getCar().getCid());
                carsInsideRange.add(object);
            }
        }
        return carsInsideRange;
    }

    private double computeDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6378.137;
        double dLat = lat2 * Math.PI / 180 - lat1 * Math.PI / 180;
        double dLon = lon2 * Math.PI / 180 - lon1 * Math.PI / 180;
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c;
        return d * 1000; // meters
    }

    public String convertStringIntoCoordinates(String location) {
        // send the request to area service
        String zoneServiceUrl = discoveryClientService.getServiceUrl("AREA-SERVICE") + "/area/convertLocation?query=" + location; ;
        log.info("Sending request to area service at {}", zoneServiceUrl);

        // make the request to area service
        ResponseEntity<String> response = restTemplate.getForEntity(zoneServiceUrl, String.class);
        String body = Objects.requireNonNull(response.getBody()).replaceAll("\"", "");

        return body;
    }
}
