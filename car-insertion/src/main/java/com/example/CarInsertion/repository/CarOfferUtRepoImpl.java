package com.example.CarInsertion.repository;

import com.example.CarInsertion.model.Car;
import com.example.CarInsertion.model.InsertionDTO;
import com.example.CarInsertion.model.Offer;
import com.example.CarInsertion.model.Utilities;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;

import java.util.List;

public class CarOfferUtRepoImpl implements CarOfferUtRepo {
    @PersistenceContext
    EntityManager em;

    @Override
    public List<InsertionDTO> getCarOfferUtFromUsername(String username) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<InsertionDTO> criteriaQuery = criteriaBuilder.createQuery(InsertionDTO.class);
        Root<Car> carRoot = criteriaQuery.from(Car.class);
        Join<Car, Offer> offerOfCar = carRoot.join("offer_oid");
        Join<Car, Utilities> utOfCar = carRoot.join("utilities_utid");

        Predicate predicateForEquality = criteriaBuilder.equal(offerOfCar.get("renterUsername"), username);

        criteriaQuery.multiselect(carRoot, offerOfCar, utOfCar).where(predicateForEquality);
        Query query = em.createQuery(criteriaQuery);

        return query.getResultList();
    }

    @Override
    public int updateCar(Car car) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaUpdate<Car> criteriaQuery = criteriaBuilder.createCriteriaUpdate(Car.class);
        Root<Car> carRoot = criteriaQuery.from(Car.class);

        if (car.getPlateNum() != null) {
            criteriaQuery.set("plateNum", car.getPlateNum());
        }
        if (car.getYear() != null) {
            criteriaQuery.set("year", car.getYear());
        }
        if (car.getPollutionLevel() != null) {
            criteriaQuery.set("pollutionLevel", car.getPollutionLevel());
        }
        if (car.getFuel() != null) {
            criteriaQuery.set("fuel", car.getFuel());;
        }
        if (car.getBrand() != null) {
            criteriaQuery.set("brand", car.getBrand());
        }
        if (car.getPassengers() != null) {
            criteriaQuery.set("passengers", car.getPassengers());
        }
        if (car.getModel() != null) {
            criteriaQuery.set("model", car.getModel());
        }
        if (car.getClassification() != null) {
            criteriaQuery.set("classification", car.getClassification());
        }
        if (car.getCarDoorNumber() != null) {
            criteriaQuery.set("carDoorNumber", car.getCarDoorNumber());
        }
        if (car.getEngine() != null) {
            criteriaQuery.set("engine", car.getEngine());
        }
        if (car.getTransmission() != null) {
            criteriaQuery.set("transmission", car.getTransmission());
        }

        criteriaQuery.where(criteriaBuilder.equal(carRoot.get("cid"), car.getCid()));
        this.em.createQuery(criteriaQuery).executeUpdate();

        return 1;
    }

    @Override
    public int updateOffer(Offer offer) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaUpdate<Offer> criteriaQuery = criteriaBuilder.createCriteriaUpdate(Offer.class);
        Root<Offer> offerRoot = criteriaQuery.from(Offer.class);

        if (offer.getFromDate() != null) {
            criteriaQuery.set("fromDate", offer.getFromDate());
        }
        if (offer.getToDate() != null) {
            criteriaQuery.set("toDate", offer.getToDate());
        }
        if (offer.getPricePerHour() != null) {
            criteriaQuery.set("pricePerHour", offer.getPricePerHour());
        }
        if (offer.getZoneLocation() != null) {
            criteriaQuery.set("zoneLocation", offer.getZoneLocation());;
        }

        criteriaQuery.where(criteriaBuilder.equal(offerRoot.get("oid"), offer.getOid()));
        this.em.createQuery(criteriaQuery).executeUpdate();

        return 1;
    }

    @Override
    public int updateUtilities(Utilities ut) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaUpdate<Utilities> criteriaQuery = criteriaBuilder.createCriteriaUpdate(Utilities.class);
        Root<Utilities> utRoot = criteriaQuery.from(Utilities.class);

        if (ut.getApple() != null) {
            criteriaQuery.set("apple", ut.getApple());
        }
        if (ut.getAndroid() != null) {
            criteriaQuery.set("android", ut.getAndroid());
        }
        if (ut.getStartAndStop() != null) {
            criteriaQuery.set("startAndStop", ut.getStartAndStop());
        }
        if (ut.getNavigationSystem() != null) {
            criteriaQuery.set("navigationSystem", ut.getNavigationSystem());
        }
        if (ut.getParkingAssistant() != null) {
            criteriaQuery.set("parkingAssistant", ut.getParkingAssistant());
        }
        if (ut.getUsbPorts() != null) {
            criteriaQuery.set("usbPorts", ut.getUsbPorts());
        }
        if (ut.getCruiseControl() != null) {
            criteriaQuery.set("cruiseControl", ut.getCruiseControl());
        }
        if (ut.getParkingCamera() != null) {
            criteriaQuery.set("parkingCamera", ut.getParkingAssistant());
        }
        if (ut.getSurroundAudio() != null) {
            criteriaQuery.set("surroundAudio", ut.getSurroundAudio());
        }
        if (ut.getBluetooth() != null) {
            criteriaQuery.set("bluetooth", ut.getBluetooth());
        }
        if (ut.getRadioAMFM() != null) {
            criteriaQuery.set("radioAMFM", ut.getRadioAMFM());
        }
        if (ut.getDisplay() != null) {
            criteriaQuery.set("display", ut.getDisplay());
        }
        if (ut.getAirConditioning() != null) {
            criteriaQuery.set("airConditioning", ut.getAirConditioning());
        }
        if (ut.getCdPlayer() != null) {
            criteriaQuery.set("cdPlayer", ut.getCdPlayer());
        }
        if (ut.getDescription() != null) {
            criteriaQuery.set("description", ut.getDescription());
        }

        criteriaQuery.where(criteriaBuilder.equal(utRoot.get("utid"), ut.getUtid()));
        this.em.createQuery(criteriaQuery).executeUpdate();

        return 1;
    }
}
