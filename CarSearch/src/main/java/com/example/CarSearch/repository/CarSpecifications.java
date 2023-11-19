package com.example.CarSearch.repository;

import com.example.CarSearch.model.Car;
import org.springframework.data.jpa.domain.Specification;

public class CarSpecifications {
    public static Specification<Car> equalYear(Long year) {
        if (year == null) {
            return null;
        }
        return (car, query, cb) -> {
            return cb.equal(car.get("year"), year);
        };
    }

    public static Specification<Car> equalPollLvl(String pollLvl) {
        if (pollLvl == null) {
            return null;
        }
        return (car, query, cb) -> {
            return cb.equal(car.get("pollutionLevel"), pollLvl);
        };
    }

    public static Specification<Car> equalFuel(String fuel) {
        if (fuel == null) {
            return null;
        }
        return (car, query, cb) -> {
            return cb.equal(car.get("fuel"), fuel);
        };
    }
    public static Specification<Car> equalBrand(String brand) {
        if (brand == null) {
            return null;
        }
        return (car, query, cb) -> {
            return cb.equal(car.get("brand"), brand);
        };
    }

    public static Specification<Car> equalPassengers(Long passengers) {
        if (passengers == null) {
            return null;
        }
        return (car, query, cb) -> {
            return cb.equal(car.get("passengers"), passengers);
        };
    }

    public static Specification<Car> likeModel(String model) {
        if (model == null) {
            return null;
        }
        return (car, query, cb) -> {
            return cb.like(car.get("model"), "%" + model + "%");
        };
    }

    public static Specification<Car> equalPosition(String position) {
        if (position == null) {
            return null;
        }
        return (car, query, cb) -> {
            return cb.equal(car.get("position"), position);
        };
    }

    public static Specification<Car> equalSize(Car.Size size) {
        if (size == null) {
            return null;
        }
        return (car, query, cb) -> {
            return cb.equal(car.get("size"), size);
        };
    }

    public static Specification<Car> equalDoor(Long carDoorNumber) {
        if (carDoorNumber == null) {
            return null;
        }
        return (car, query, cb) -> {
            return cb.equal(car.get("carDoorNumber"), carDoorNumber);
        };
    }

    public static Specification<Car> equalEngine(Car.Engine engine) {
        if (engine == null) {
            return null;
        }
        return (car, query, cb) -> {
            return cb.equal(car.get("engine"), engine);
        };
    }

    public static Specification<Car> equalTransmission(Car.Transmission transmission) {
        if (transmission == null) {
            return null;
        }
        return (car, query, cb) -> {
            return cb.equal(car.get("transmission"), transmission);
        };
    }
}
