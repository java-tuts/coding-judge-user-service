package com.vighnesh153.userservice.controllers;

import com.vighnesh153.userservice.dto.ResponseDto;
import com.vighnesh153.userservice.dto.UserDto;
import com.vighnesh153.userservice.dto.UserResponseDto;
import com.vighnesh153.userservice.models.User;
import com.vighnesh153.userservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    @Autowired
    private UserService userService;

    @PostMapping("/user/register")
    public ResponseDto<UserResponseDto> registerUser(@RequestBody UserDto userDto) {

        User user = userService.registerUser(userDto);
        return new ResponseDto<>(
                new UserResponseDto(user.getId(), user.getFullName(), user.isActive(), user.getEmail()),
                HttpStatus.OK
        );
    }
}
