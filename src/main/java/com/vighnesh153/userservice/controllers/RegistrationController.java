package com.vighnesh153.userservice.controllers;

import com.vighnesh153.userservice.dto.ResponseDto;
import com.vighnesh153.userservice.dto.UserDto;
import com.vighnesh153.userservice.dto.UserResponseDto;
import com.vighnesh153.userservice.models.User;
import com.vighnesh153.userservice.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/register")
    public ResponseDto<UserResponseDto> registerUser(@Valid @RequestBody UserDto userDto) {
        log.info("Received registration request got " + userDto.getEmail());
        User user = userService.registerUser(userDto);
        return new ResponseDto<>(
                new UserResponseDto(user.getId(), user.getFullName(), user.isActive(), user.getEmail()),
                HttpStatus.OK
        );
    }

    @GetMapping("/user/confirm")
    public ResponseDto<UserResponseDto> validateUser(@RequestParam String token) {
        User user = userService.validateUser(token);

        return new ResponseDto<>(
                user != null
                        ? new UserResponseDto(user.getId(), user.getFullName(), user.isActive(), user.getEmail())
                        : null,
                user != null
                        ? HttpStatus.OK
                        : HttpStatus.BAD_REQUEST
        );
    }
}
