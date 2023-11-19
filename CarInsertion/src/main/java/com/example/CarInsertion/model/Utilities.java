package com.example.CarInsertion.model;

import jakarta.persistence.*;

@Entity
@Table(name = "utilities")
public class Utilities {
    public enum Assistant {
        Android,
        Apple
    }

    @Id
    @SequenceGenerator(name = "ut_gen", sequenceName = "ut_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ut_gen")
    private Long utid;
    private boolean display;
    private Assistant assistant;
    private boolean airConditioning;
    private boolean startAndStop;
    private boolean navigationSystem;
    private boolean parkingAssistant;
    private boolean bluetooth;
    private boolean USBPorts;
    private boolean CDPlayer;
    private boolean radioAMFM;
    private boolean cruiseControl;
    private boolean parkingCamera;
    private boolean surroundAudio;
    private String description;

    public Utilities() {
    }

    public Utilities(Assistant assistant, boolean airConditioning, boolean startAndStop, boolean display, boolean navigationSystem,
                     boolean parkingAssistant, boolean bluetooth, boolean USBPorts, boolean CDPlayer, boolean radioAMFM, boolean cruiseControl,
                     boolean parkingCamera, boolean surroundAudio, String description) {
        this.assistant = assistant;
        this.airConditioning = airConditioning;
        this.startAndStop = startAndStop;
        this.display = display;
        this.navigationSystem = navigationSystem;
        this.parkingAssistant = parkingAssistant;
        this.bluetooth = bluetooth;
        this.USBPorts = USBPorts;
        this.CDPlayer = CDPlayer;
        this.radioAMFM = radioAMFM;
        this.cruiseControl = cruiseControl;
        this.parkingCamera = parkingCamera;
        this.surroundAudio = surroundAudio;
        this.description = description;
    }

    public void setUtid(Long utid) {
        this.utid = utid;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUtid() {
        return utid;
    }

    public Assistant getAssistant() {
        return assistant;
    }

    public boolean getAirConditioning() {
        return airConditioning;
    }

    public boolean getStartAndStop() {
        return startAndStop;
    }

    public boolean getDisplay() {
        return display;
    }

    public boolean getNavigationSystem() {
        return navigationSystem;
    }

    public boolean getParkingAssistant() {
        return parkingAssistant;
    }

    public boolean getBluetooth() {
        return bluetooth;
    }

    public boolean getUSBPorts() {
        return USBPorts;
    }

    public boolean getCDPlayer() {
        return CDPlayer;
    }

    public boolean getRadioAMFM() {
        return radioAMFM;
    }

    public boolean getCruiseControl() {
        return cruiseControl;
    }

    public boolean getParkingCamera() {
        return parkingCamera;
    }

    public boolean getSurroundAudio() {
        return surroundAudio;
    }

    public String getDescription() {
        return description;
    }
}
