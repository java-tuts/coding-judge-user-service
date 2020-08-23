package com.vighnesh153.userservice.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserLoginDto {
    @NotEmpty
    @Size(min = 1)
    private String email;

    @NotEmpty
    @Size(min = 6)
    private String password;
}
