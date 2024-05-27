package com.example.userservice.appuser.dto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class SendEmailRequestDto {
    private String email;
    private String firstName;
    private String confirmationToken;
}
