package com.example.CarSearch.repository;

import com.example.CarSearch.model.Car;
import com.example.CarSearch.model.Offer;
import com.example.CarSearch.model.SearchDTO;
import com.example.CarSearch.model.Utilities;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SearchRepoImpl implements SearchRepo {
    @PersistenceContext
    EntityManager em;

    @Override
    public List<SearchDTO> getCarOfferUtFromSearchDTO(SearchDTO dto) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<SearchDTO> criteriaQuery = criteriaBuilder.createQuery(SearchDTO.class);
        Root<Car> carRoot = criteriaQuery.from(Car.class);
        Join<Car, Offer> offerOfCar = carRoot.join("offer_oid");
        Join<Car, Utilities> utOfCar = carRoot.join("utilities_utid");

        ArrayList<Predicate> predList = new ArrayList<>();

        if (dto.getCar().getYear() != null) {
            Predicate pred1 = criteriaBuilder.equal(carRoot.get("year"), dto.getCar().getYear());
            predList.add(pred1);
        }
        if (dto.getCar().getPollutionLevel() != null) {
            Predicate pred2 = criteriaBuilder.equal(carRoot.get("pollutionLevel"), dto.getCar().getPollutionLevel());
            predList.add(pred2);
        }
        if (dto.getCar().getFuel() != null) {
            Predicate pred3 = criteriaBuilder.equal(carRoot.get("fuel"), dto.getCar().getFuel());
            predList.add(pred3);
        }
        if (dto.getCar().getBrand() != null) {
            Predicate pred4 = criteriaBuilder.equal(carRoot.get("brand"), dto.getCar().getBrand());
            predList.add(pred4);
        }
        if (dto.getCar().getPassengers() != null) {
            Predicate pred5 = criteriaBuilder.equal(carRoot.get("passengers"), dto.getCar().getPassengers());
            predList.add(pred5);
        }
        if (dto.getCar().getModel() != null) {
            Predicate pred6 = criteriaBuilder.equal(carRoot.get("model"), dto.getCar().getModel());
            predList.add(pred6);
        }
        if (dto.getCar().getClassification() != null) {
            Predicate pred8 = criteriaBuilder.equal(carRoot.get("classification"), dto.getCar().getClassification());
            predList.add(pred8);
        }
        if (dto.getCar().getCarDoorNumber() != null) {
            Predicate pred9 = criteriaBuilder.equal(carRoot.get("carDoorNumber"), dto.getCar().getCarDoorNumber());
            predList.add(pred9);
        }
        if (dto.getCar().getEngine() != null) {
            Predicate pred10 = criteriaBuilder.equal(carRoot.get("engine"), dto.getCar().getEngine());
            predList.add(pred10);
        }
        if (dto.getCar().getTransmission() != null) {
            Predicate pred11 = criteriaBuilder.equal(carRoot.get("transmission"), dto.getCar().getTransmission());
            predList.add(pred11);
        }
        if (dto.getOffer().getAvailable() != null) {
            Predicate pred12 = criteriaBuilder.equal(offerOfCar.get("available"), dto.getOffer().getAvailable());
            predList.add(pred12);
        }
        if (dto.getOffer().getFromDate() != null) {
            Predicate pred13 = criteriaBuilder.greaterThanOrEqualTo(offerOfCar.get("fromDate"), dto.getOffer().getFromDate());
            predList.add(pred13);
        }
        if (dto.getOffer().getToDate() != null) {
            Predicate pred14 = criteriaBuilder.lessThanOrEqualTo(offerOfCar.get("toDate"), dto.getOffer().getToDate());
            predList.add(pred14);
        }
        if (dto.getOffer().getPricePerHour() != null) {
            Predicate pred15 = criteriaBuilder.lessThanOrEqualTo(offerOfCar.get("pricePerHour"), dto.getOffer().getPricePerHour());
            predList.add(pred15);
        }
        if (dto.getOffer().getZoneLocation() != null) {
            Predicate pred29 = criteriaBuilder.equal(offerOfCar.get("zoneLocation"), dto.getOffer().getZoneLocation());
            predList.add(pred29);
        }
        if (dto.getUtilities().getDisplay() != null) {
            Predicate pred16 = criteriaBuilder.equal(utOfCar.get("display"), dto.getUtilities().getDisplay());
            predList.add(pred16);
        }
        if (dto.getUtilities().getAndroid() != null) {
            Predicate pred17 = criteriaBuilder.equal(utOfCar.get("android"), dto.getUtilities().getAndroid());
            predList.add(pred17);
        }
        if (dto.getUtilities().getApple() != null) {
            Predicate pred30 = criteriaBuilder.equal(utOfCar.get("apple"), dto.getUtilities().getApple());
            predList.add(pred30);
        }
        if (dto.getUtilities().getAirConditioning() != null) {
            Predicate pred18 = criteriaBuilder.equal(utOfCar.get("airConditioning"), dto.getUtilities().getAirConditioning());
            predList.add(pred18);
        }
        if (dto.getUtilities().getStartAndStop() != null) {
            Predicate pred19 = criteriaBuilder.equal(utOfCar.get("startAndStop"), dto.getUtilities().getStartAndStop());
            predList.add(pred19);
        }
        if (dto.getUtilities().getNavigationSystem() != null) {
            Predicate pred20 = criteriaBuilder.equal(utOfCar.get("navigationSystem"), dto.getUtilities().getNavigationSystem());
            predList.add(pred20);
        }
        if (dto.getUtilities().getParkingAssistant() != null) {
            Predicate pred21 = criteriaBuilder.equal(utOfCar.get("parkingAssistant"), dto.getUtilities().getParkingAssistant());
            predList.add(pred21);
        }
        if (dto.getUtilities().getBluetooth() != null) {
            Predicate pred22 = criteriaBuilder.equal(utOfCar.get("bluetooth"), dto.getUtilities().getBluetooth());
            predList.add(pred22);
        }
        if (dto.getUtilities().getUsbPorts() != null) {
            Predicate pred23 = criteriaBuilder.equal(utOfCar.get("usbPorts"), dto.getUtilities().getUsbPorts());
            predList.add(pred23);
        }
        if (dto.getUtilities().getCdPlayer() != null) {
            Predicate pred24 = criteriaBuilder.equal(utOfCar.get("cdPlayer"), dto.getUtilities().getCdPlayer());
            predList.add(pred24);
        }
        if (dto.getUtilities().getRadioAMFM() != null) {
            Predicate pred25 = criteriaBuilder.equal(utOfCar.get("radioAMFM"), dto.getUtilities().getRadioAMFM());
            predList.add(pred25);
        }
        if (dto.getUtilities().getCruiseControl() != null) {
            Predicate pred26 = criteriaBuilder.equal(utOfCar.get("cruiseControl"), dto.getUtilities().getCruiseControl());
            predList.add(pred26);
        }
        if (dto.getUtilities().getParkingCamera() != null) {
            Predicate pred27 = criteriaBuilder.equal(utOfCar.get("parkingCamera"), dto.getUtilities().getParkingCamera());
            predList.add(pred27);
        }
        if (dto.getUtilities().getSurroundAudio() != null) {
            Predicate pred28 = criteriaBuilder.equal(utOfCar.get("surroundAudio"), dto.getUtilities().getSurroundAudio());
            predList.add(pred28);
        }

        criteriaQuery.multiselect(carRoot, offerOfCar, utOfCar).where(predList.toArray(new Predicate[0]));
        Query query = em.createQuery(criteriaQuery);

        return query.getResultList();
    }

    @Override
    public List<SearchDTO> getCarOfferUtFromId(Long id) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<SearchDTO> criteriaQuery = criteriaBuilder.createQuery(SearchDTO.class);
        Root<Car> carRoot = criteriaQuery.from(Car.class);
        Join<Car, Offer> offerOfCar = carRoot.join("offer_oid");
        Join<Car, Utilities> utOfCar = carRoot.join("utilities_utid");

        Predicate predicateForEquality = criteriaBuilder.equal(carRoot.get("cid"), id);

        criteriaQuery.multiselect(carRoot, offerOfCar, utOfCar).where(predicateForEquality);
        Query query = em.createQuery(criteriaQuery);

        return query.getResultList();
    }

    @Override
    public List<SearchDTO> getCars() {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<SearchDTO> criteriaQuery = criteriaBuilder.createQuery(SearchDTO.class);
        Root<Car> carRoot = criteriaQuery.from(Car.class);
        Join<Car, Offer> offerOfCar = carRoot.join("offer_oid");
        Join<Car, Utilities> utOfCar = carRoot.join("utilities_utid");

        criteriaQuery.multiselect(carRoot, offerOfCar, utOfCar);
        Query query = em.createQuery(criteriaQuery);

        return query.getResultList();
    }

    @Override
    public String getRenter(Long id) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Offer> criteriaQuery = criteriaBuilder.createQuery(Offer.class);
        Root<Car> carRoot = criteriaQuery.from(Car.class);
        Join<Car, Offer> offerOfCar = carRoot.join("offer_oid");

        Predicate predicateForEquality = criteriaBuilder.equal(carRoot.get("cid"), id);

        criteriaQuery.select(offerOfCar).where(predicateForEquality);

        List<Offer> results =  em.createQuery(criteriaQuery).getResultList();
        if (results.size() != 1) {
            return null;
        }
        return results.get(0).getRenterUsername();
    }
}
