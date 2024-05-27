package com.example.CarRating.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class AppUserDTO {
    private Long id;
    @Getter
    private String firstName;
    @Getter
    private String lastName;
    private String email;
    private String password;
    private String residence;
    private String contact;

    @Enumerated(EnumType.STRING)
    private AppUserRoleDTO appUserRole;
    private Boolean locked = false;
    private Boolean enabled = false;

    public AppUserDTO(Long id, String firstName, String lastName, String email, String password, AppUserRoleDTO appUserRole) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.appUserRole = appUserRole;
    }

    public String getUsername() {
        return email;
    }
    public boolean isAccountNonExpired() {
        return true;
    }
    public boolean isAccountNonLocked() {
        return !locked;
    }
    public boolean isCredentialsNonExpired() {
        return true;
    }
    public boolean isEnabled() {
        return enabled;
    }
}
