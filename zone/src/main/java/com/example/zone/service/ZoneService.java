package com.example.zone.service;

import com.example.zone.dto.SubscriptionDTO;
import com.example.zone.model.Subscription;
import com.example.zone.model.Zone;
import com.example.zone.repository.SubscriptionRepository;
import com.example.zone.repository.ZoneRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ZoneService {
    private ZoneRepository zoneRepository;
    private SubscriptionRepository subscriptionRepository;

    public void subscribeUserToZone(SubscriptionDTO subscriptionDTO) {
        List<Zone> result = zoneRepository.findAllByCenterAndRadius(subscriptionDTO.getCenter_latitude(),
                subscriptionDTO.getCenter_longitude(), subscriptionDTO.getRadius());
        Zone zone = new Zone(subscriptionDTO.getLocationName(),subscriptionDTO.getCenter_latitude(), subscriptionDTO.getCenter_longitude(),
                subscriptionDTO.getRadius());
        Subscription subscription;
        if (result.isEmpty()) {
            Zone saved = zoneRepository.save(zone);
            subscription = new Subscription(saved.getId(), subscriptionDTO.getUsername());
        } else {
            subscription = new Subscription(result.get(0).getId(), subscriptionDTO.getUsername());
        }
        if (result.size() > 1) {
            throw new RuntimeException("There is a more than one zone.");
        }
        subscriptionRepository.save(subscription);
    }

    public List<String> getSubscribedUser(String lat, String lon){
        double[] lat_lon = {Double.parseDouble(lat), Double.parseDouble(lon)};
        List<Zone> zones = zoneRepository.findAll();
        List<Subscription> subs = subscriptionRepository.findAll();

        List<String> result = new ArrayList<>();

        for (Zone zone : zones) {
            // if the car is inside a zone
            if (computeDistance(lat_lon[0], lat_lon[1], zone.getCenter_latitude(), zone.getCenter_longitude()) <= zone.getRadius()) {
                // find all subscribers of that zone
                for (Subscription sub : subs) {
                    if (sub.getZoneId().equals(zone.getId())) {
                        result.add(sub.getUsername());
                    }
                }
            }
        }
        return result;
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

    // TODO TEST
    public List<Zone> getZonesForUser(String username) {

        // 1 - Get subs for username
        List<Subscription> subscriptions =  subscriptionRepository.findSubscriptionByUsername(username);
        List<Zone> zones = new ArrayList<>();

        // 2 - Get zones for subs id
        for (Subscription sub: subscriptions) {
            Optional<Zone> optionalZone = zoneRepository.findById(sub.getZoneId());

            if (optionalZone.isPresent()) {
                Zone zone = optionalZone.get();

                // Add the zone to the list
                zones.add(zone);
            }
        }

        return zones;
    }

    public void deleteSub(Long zoneId, String username) {
        Subscription subscription = subscriptionRepository.findSubscriptionByZoneIdAndUsername(zoneId, username);
        if (subscription != null){
            subscriptionRepository.deleteById(subscription.getId());
        } else {
            throw new RuntimeException("The user is not subscribed for zone " + zoneId);
        }
    }
}
