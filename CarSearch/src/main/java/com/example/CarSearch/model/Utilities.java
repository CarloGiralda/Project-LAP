package com.example.CarSearch.model;

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
    private Boolean display;
    private Assistant assistant;
    private Boolean airConditioning;
    private Boolean startAndStop;
    private Boolean navigationSystem;
    private Boolean parkingAssistant;
    private Boolean bluetooth;
    private Boolean USBPorts;
    private Boolean CDPlayer;
    private Boolean radioAMFM;
    private Boolean cruiseControl;
    private Boolean parkingCamera;
    private Boolean surroundAudio;
    private String description;

    public Utilities() {
    }

    public Utilities(Assistant assistant, Boolean airConditioning, Boolean startAndStop, Boolean display, Boolean navigationSystem,
                     Boolean parkingAssistant, Boolean bluetooth, Boolean USBPorts, Boolean CDPlayer, Boolean radioAMFM, Boolean cruiseControl,
                     Boolean parkingCamera, Boolean surroundAudio, String description) {
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

    public void setDisplay(Boolean display) {
        this.display = display;
    }

    public void setAssistant(Assistant assistant) {
        this.assistant = assistant;
    }

    public void setAirConditioning(Boolean airConditioning) {
        this.airConditioning = airConditioning;
    }

    public void setStartAndStop(Boolean startAndStop) {
        this.startAndStop = startAndStop;
    }

    public void setNavigationSystem(Boolean navigationSystem) {
        this.navigationSystem = navigationSystem;
    }

    public void setParkingAssistant(Boolean parkingAssistant) {
        this.parkingAssistant = parkingAssistant;
    }

    public void setBluetooth(Boolean bluetooth) {
        this.bluetooth = bluetooth;
    }

    public void setUSBPorts(Boolean USBPorts) {
        this.USBPorts = USBPorts;
    }

    public void setCDPlayer(Boolean CDPlayer) {
        this.CDPlayer = CDPlayer;
    }

    public void setRadioAMFM(Boolean radioAMFM) {
        this.radioAMFM = radioAMFM;
    }

    public void setCruiseControl(Boolean cruiseControl) {
        this.cruiseControl = cruiseControl;
    }

    public void setParkingCamera(Boolean parkingCamera) {
        this.parkingCamera = parkingCamera;
    }

    public void setSurroundAudio(Boolean surroundAudio) {
        this.surroundAudio = surroundAudio;
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

    public Boolean getAirConditioning() {
        return airConditioning;
    }

    public Boolean getStartAndStop() {
        return startAndStop;
    }

    public Boolean getDisplay() {
        return display;
    }

    public Boolean getNavigationSystem() {
        return navigationSystem;
    }

    public Boolean getParkingAssistant() {
        return parkingAssistant;
    }

    public Boolean getBluetooth() {
        return bluetooth;
    }

    public Boolean getUSBPorts() {
        return USBPorts;
    }

    public Boolean getCDPlayer() {
        return CDPlayer;
    }

    public Boolean getRadioAMFM() {
        return radioAMFM;
    }

    public Boolean getCruiseControl() {
        return cruiseControl;
    }

    public Boolean getParkingCamera() {
        return parkingCamera;
    }

    public Boolean getSurroundAudio() {
        return surroundAudio;
    }

    public String getDescription() {
        return description;
    }
}
