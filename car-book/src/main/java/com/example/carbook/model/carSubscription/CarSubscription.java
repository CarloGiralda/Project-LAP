package com.example.carbook.model.carSubscription;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity

@Table(name = "carsubscription", uniqueConstraints = @UniqueConstraint(columnNames = {"userId","carId"}))
public class CarSubscription {

    @Id
    @SequenceGenerator(name = "carsubcription_seq", sequenceName = "carsubscription_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "carsubscription_seq")
    private Long id;
    private String userId;
    private Long carId;

    public CarSubscription(String usernameId, Long carId){
        this.userId = usernameId;
        this.carId = carId;
    }


}
