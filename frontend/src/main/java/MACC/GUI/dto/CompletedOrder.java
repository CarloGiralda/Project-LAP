package MACC.GUI.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompletedOrder {
    private PaymentStatus status;
    private String payId;

    public CompletedOrder(PaymentStatus status) {
        this.status = status;
    }
}