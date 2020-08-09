package com.vighnesh153.userservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
    private Long id;

    private String fullName;

    private boolean active;

    private String email;

    public UserResponseDto(Long id, String fullName, boolean active, String email) {
        this.id = id;
        this.fullName = fullName;
        this.active = active;
        this.email = email;
    }
}
