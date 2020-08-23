package com.vighnesh153.userservice.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserRegistrationDto {

    @NotEmpty
    @Size(min = 5)
    private String fullName;

    // TODO: Implement Custom Validators for email
    @NotEmpty
    @Size(min = 1)
    private String email;

    // TODO: Implement Custom Validators
    @NotEmpty
    @Size(min = 6)
    private String password;
}
