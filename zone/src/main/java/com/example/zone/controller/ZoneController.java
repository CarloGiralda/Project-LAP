package com.example.zone.controller;

import com.example.zone.dto.SubscriptionDTO;
import com.example.zone.model.Zone;
import com.example.zone.service.AreaServiceMap;
import com.example.zone.service.ZoneService;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(path = "/area")
public class ZoneController {
    private ZoneService zoneService;
    private final AreaServiceMap geocodingService;

    @GetMapping(path = "/subscribe")
    public ResponseEntity<?> subscribe(@RequestParam("center") String center, @RequestParam("radius") String radius, @RequestParam("locationName") String locationName, @RequestHeader("Logged-In-User") String username) {
        try {
            String[] center_lat_lon = center.split("/");
            SubscriptionDTO subscriptionDTO = new SubscriptionDTO(username, locationName, Double.parseDouble(center_lat_lon[0]), Double.parseDouble(center_lat_lon[1]), Double.parseDouble(radius));
            zoneService.subscribeUserToZone(subscriptionDTO);
            return ResponseEntity.ok().body("Success");
        } catch (Exception e){
            return ResponseEntity.internalServerError().body("internal server error");
        }
    }

    @GetMapping(path = "/getSubscribedZone")
    public ResponseEntity<?> getZones(@RequestHeader("Logged-In-User") String username) {
        try {
            List<Zone> zones = zoneService.getZonesForUser(username);
            return ResponseEntity.ok().body(zones);
        } catch (Exception e){
            return ResponseEntity.internalServerError().body("internal server error");
        }
    }

    @DeleteMapping(path = "/deleteZoneSub/{zoneId}")
    public ResponseEntity<?> deleteSub(@PathVariable Long zoneId, @RequestHeader("Logged-In-User") String username) {
        try {
            zoneService.deleteSub(zoneId,username);
            return ResponseEntity.ok().body("user correctly unsubscribed");
        } catch (Exception e){
            return ResponseEntity.internalServerError().body("internal server error");
        }
    }


    @GetMapping(path = "/getSubscribedUser")
    public ResponseEntity<?> getSubscribedUser(@RequestParam("lat") String lat, @RequestParam("lon") String lon) {
        try {
            List<String> subUsers = zoneService.getSubscribedUser(lat, lon);
            return ResponseEntity.ok().body(subUsers);
        } catch (Exception e){
            return ResponseEntity.internalServerError().body("internal server error");
        }
    }

    @GetMapping(path = "/convertLocation")
    public ResponseEntity<?> convertLocation(@RequestParam("query") String query){
        JsonObject location;
        try {
            //Request to car search
            location = geocodingService.search(query);
            String result = location.get("lat").getAsString() + "/" + location.get("lon").getAsString();
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (IOException | JsonSyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(path = "/searchLocation",method = RequestMethod.GET,consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> searchLocation(@RequestParam("query") String query){
        JsonObject location;
        JsonObject ret=new JsonObject();
        try {
            //Request to car search
            location = geocodingService.search(query);
            ret.add("lat",location.get("lat"));
            ret.add("lon",location.get("lon"));
            return new ResponseEntity<>(ret,HttpStatus.OK);
        } catch (IOException | JsonSyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(path = "/searchName",method = RequestMethod.GET,consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> searchName(@RequestParam("query") String query){
        JsonObject location;
        JsonObject ret=new JsonObject();
        try {
            //Request to car search
            location = geocodingService.searchName(query);
            System.out.println(location);
            if (location.get("address").getAsJsonObject().get("city") != null) {
                ret.add("city", location.get("address").getAsJsonObject().get("city"));
            } else {
                ret.add("city", location.get("address").getAsJsonObject().get("town"));
            }
            return new ResponseEntity<>(ret,HttpStatus.OK);
        } catch (IOException | JsonSyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
