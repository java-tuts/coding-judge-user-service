package com.vighnesh153.userservice.controllers;

import com.vighnesh153.userservice.dto.ResponseDto;
import com.vighnesh153.userservice.dto.UserLoginDto;
import com.vighnesh153.userservice.dto.UserRegistrationDto;
import com.vighnesh153.userservice.dto.UserResponseDto;
import com.vighnesh153.userservice.models.Session;
import com.vighnesh153.userservice.models.User;
import com.vighnesh153.userservice.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Duration;

@Slf4j
@RestController
public class RegistrationController {

    private final static String SESSION_COOKIE_NAME = "SESSIONID";

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/register")
    public ResponseDto<UserResponseDto> registerUser(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {
        log.info("Received registration request got " + userRegistrationDto.getEmail());
        User user = userService.registerUser(userRegistrationDto);
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

    @PostMapping("/user/login")
    public ResponseEntity<UserResponseDto> loginUser(@Valid @RequestBody UserLoginDto userLoginDto) {
        log.info("Received login request for email " + userLoginDto.getEmail());
        Session session = userService.loginUser(userLoginDto);

        if (session == null) {
            // Invalid credentials
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        User user = session.getUser();
        ResponseCookie cookie = ResponseCookie
                .from(SESSION_COOKIE_NAME, session.getSessionToken())
                .maxAge(Duration.ofMinutes(Session.getVALIDITY_TIME_IN_MINUTES()))
                .sameSite("Strict")
                .path("/")
                .httpOnly(true)
                .secure(true)
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(
                new UserResponseDto(user.getId(), user.getFullName(), user.isActive(), user.getEmail())
        );
    }

    @GetMapping("/user/isLoggedIn")
    private ResponseDto<Boolean> isUserLoggedIn(@CookieValue(SESSION_COOKIE_NAME) String sessionToken) {
        Boolean isLoggedIn = userService.isUserLoggedIn(sessionToken);
        return new ResponseDto<>(isLoggedIn, HttpStatus.OK);
    }

    @GetMapping("/user/logout")
    private ResponseDto<Boolean> logoutUser(@CookieValue(SESSION_COOKIE_NAME) String sessionToken) {
        Boolean loggedOut = userService.logoutUser(sessionToken);
        return new ResponseDto<>(
                loggedOut,
                loggedOut ? HttpStatus.OK : HttpStatus.BAD_REQUEST
        );
    }
}
