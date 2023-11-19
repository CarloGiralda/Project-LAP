package com.example.CarSearch.repository;

import com.example.CarSearch.model.Offer;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class OfferSpecifications {
    public static Specification<Offer> equalAvailable(Boolean available) {
        if (available == null) {
            return null;
        }
        return (car, query, cb) -> {
            return cb.equal(car.get("available"), available);
        };
    }

    public static Specification<Offer> equalFromDate(LocalDate fromDate) {
        if (fromDate == null) {
            return null;
        }
        return (car, query, cb) -> {
            return cb.equal(car.get("fromDate"), fromDate);
        };
    }

    public static Specification<Offer> equalToDate(LocalDate toDate) {
        if (toDate == null) {
            return null;
        }
        return (car, query, cb) -> {
            return cb.equal(car.get("toDate"), toDate);
        };
    }

    public static Specification<Offer> equalPricePerDay(String pricePerDay) {
        if (pricePerDay == null) {
            return null;
        }
        return (car, query, cb) -> {
            return cb.equal(car.get("pricePerDay"), pricePerDay);
        };
    }
}
