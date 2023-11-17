package com.example.CarSearch.repository;

import com.example.CarSearch.model.Utilities;
import org.springframework.data.jpa.domain.Specification;

public class UtilitiesSpecifications {
    public static Specification<Utilities> equalDisplay(Boolean display) {
        if (display == null) {
            return null;
        }
        return (car, query, cb) -> {
            return cb.equal(car.get("display"), display);
        };
    }

    public static Specification<Utilities> equalAssistant(Utilities.Assistant assistant) {
        if (assistant == null) {
            return null;
        }
        return (car, query, cb) -> {
            return cb.equal(car.get("assistant"), assistant);
        };
    }

    public static Specification<Utilities> equalAirConditioning(Boolean ac) {
        if (ac == null) {
            return null;
        }
        return (car, query, cb) -> {
            return cb.equal(car.get("airConditioning"), ac);
        };
    }

    public static Specification<Utilities> equalStartAndStop(Boolean sas) {
        if (sas == null) {
            return null;
        }
        return (car, query, cb) -> {
            return cb.equal(car.get("startAndStop"), sas);
        };
    }

    public static Specification<Utilities> equalNavigationSystem(Boolean ns) {
        if (ns == null) {
            return null;
        }
        return (car, query, cb) -> {
            return cb.equal(car.get("navigationSystem"), ns);
        };
    }

    public static Specification<Utilities> equalParkingAssistant(Boolean pa) {
        if (pa == null) {
            return null;
        }
        return (car, query, cb) -> {
            return cb.equal(car.get("parkingAssistant"), pa);
        };
    }

    public static Specification<Utilities> equalBluetooth(Boolean bluetooth) {
        if (bluetooth == null) {
            return null;
        }
        return (car, query, cb) -> {
            return cb.equal(car.get("bluetooth"), bluetooth);
        };
    }

    public static Specification<Utilities> equalUSBPorts(Boolean USBPorts) {
        if (USBPorts == null) {
            return null;
        }
        return (car, query, cb) -> {
            return cb.equal(car.get("USBPorts"), USBPorts);
        };
    }

    public static Specification<Utilities> equalCDPlayer(Boolean CDPlayer) {
        if (CDPlayer == null) {
            return null;
        }
        return (car, query, cb) -> {
            return cb.equal(car.get("CDPlayer"), CDPlayer);
        };
    }

    public static Specification<Utilities> equalRadioAMFM(Boolean radioAMFM) {
        if (radioAMFM == null) {
            return null;
        }
        return (car, query, cb) -> {
            return cb.equal(car.get("radioAMFM"), radioAMFM);
        };
    }

    public static Specification<Utilities> equalCruiseControl(Boolean cruiseControl) {
        if (cruiseControl == null) {
            return null;
        }
        return (car, query, cb) -> {
            return cb.equal(car.get("cruiseControl"), cruiseControl);
        };
    }

    public static Specification<Utilities> equalParkingCamera(Boolean parkingCamera) {
        if (parkingCamera == null) {
            return null;
        }
        return (car, query, cb) -> {
            return cb.equal(car.get("parkingCamera"), parkingCamera);
        };
    }

    public static Specification<Utilities> equalSurroundAudio(Boolean surroundAudio) {
        if (surroundAudio == null) {
            return null;
        }
        return (car, query, cb) -> {
            return cb.equal(car.get("surroundAudio"), surroundAudio);
        };
    }
}
