package com.example.carbook.model.booking;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "bookings", uniqueConstraints = @UniqueConstraint(columnNames = {"cid","fromDay","toDay","fromHour","toHour"}))
public class Booking {

    @Id
    @SequenceGenerator(name = "bookings_seq", sequenceName = "bookings_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookings_seq")
    private Long bid;
    private Long cid;
    private String fromDay;
    private String toDay;
    private String fromHour;
    private String toHour;
    private String madeDate; // TODO check if necessary
    private String username;
    private String description;

    public Booking(Long cid, String fromDay, String toDay, String fromHour, String toHour, String madeDate, String username, String description) {
        this.cid = cid;
        this.fromDay = fromDay;
        this.toDay = toDay;
        this.fromHour = fromHour;
        this.toHour = toHour;
        this.madeDate = madeDate;
        this.username = username;
        this.description = description;
    }


}
