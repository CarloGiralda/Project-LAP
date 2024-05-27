package com.example.CarSearch.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "utilities")
public class Utilities {
    @Id
    @SequenceGenerator(name = "ut_seq", sequenceName = "ut_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ut_seq")
    private Long utid;
    private Boolean display;
    private Boolean android;
    private Boolean apple;
    private Boolean airConditioning;
    private Boolean startAndStop;
    private Boolean navigationSystem;
    private Boolean parkingAssistant;
    private Boolean bluetooth;
    private Boolean usbPorts;
    private Boolean cdPlayer;
    private Boolean radioAMFM;
    private Boolean cruiseControl;
    private Boolean parkingCamera;
    private Boolean surroundAudio;
    private String description;

    public Utilities() {
    }

    public Utilities(Boolean android, Boolean apple, Boolean airConditioning, Boolean startAndStop, Boolean display, Boolean navigationSystem,
                     Boolean parkingAssistant, Boolean bluetooth, Boolean USBPorts, Boolean CDPlayer, Boolean radioAMFM, Boolean cruiseControl,
                     Boolean parkingCamera, Boolean surroundAudio, String description) {
        this.android = android;
        this.apple = apple;
        this.airConditioning = airConditioning;
        this.startAndStop = startAndStop;
        this.display = display;
        this.navigationSystem = navigationSystem;
        this.parkingAssistant = parkingAssistant;
        this.bluetooth = bluetooth;
        this.usbPorts = USBPorts;
        this.cdPlayer = CDPlayer;
        this.radioAMFM = radioAMFM;
        this.cruiseControl = cruiseControl;
        this.parkingCamera = parkingCamera;
        this.surroundAudio = surroundAudio;
        this.description = description;
    }
}
