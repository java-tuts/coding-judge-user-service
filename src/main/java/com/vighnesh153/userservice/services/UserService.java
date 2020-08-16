package com.vighnesh153.userservice.services;

import com.vighnesh153.userservice.dto.UserDto;
import com.vighnesh153.userservice.models.User;

public interface UserService {

    User registerUser(UserDto userDto);

    User validateUser(String token);
}
