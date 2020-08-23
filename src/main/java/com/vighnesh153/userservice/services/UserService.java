package com.vighnesh153.userservice.services;

import com.vighnesh153.userservice.dto.UserLoginDto;
import com.vighnesh153.userservice.dto.UserRegistrationDto;
import com.vighnesh153.userservice.models.Session;
import com.vighnesh153.userservice.models.User;

public interface UserService {

    User registerUser(UserRegistrationDto userRegistrationDto);

    User validateUser(String token);

    Session loginUser(UserLoginDto userLoginDto);

    Boolean isUserLoggedIn(String sessionToken);
}
