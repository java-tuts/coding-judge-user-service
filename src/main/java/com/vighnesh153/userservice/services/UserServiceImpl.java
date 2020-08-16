package com.vighnesh153.userservice.services;

import com.vighnesh153.userservice.dto.UserDto;
import com.vighnesh153.userservice.events.SuccessfulRegistrationEvent;
import com.vighnesh153.userservice.models.User;
import com.vighnesh153.userservice.models.VerificationToken;
import com.vighnesh153.userservice.repositories.UserRepository;
import com.vighnesh153.userservice.repositories.VerificationTokenRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public UserServiceImpl(
            UserRepository userRepository,
            VerificationTokenRepository verificationTokenRepository,
            ApplicationEventPublisher applicationEventPublisher
    ) {
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public User registerUser(UserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()) != null) {
            // TODO: throw Exception
        }

        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFullName(userDto.getFullName());
        user.setActive(false);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));    // TODO: encrypt password

        User savedUser = userRepository.save(user);

        applicationEventPublisher.publishEvent(new SuccessfulRegistrationEvent(savedUser));

        return savedUser;
    }

    @Override
    public User validateUser(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

        // Token doesn't exists
        if (verificationToken == null) {
            return null;
        }

        // Token expired
        if (verificationToken.getExpiryTime().getTime() <= new Date().getTime()) {
            // Delete the verification token from the table because it is of no use.
            verificationTokenRepository.delete(verificationToken);

            return null;
        }

        User verifiedUser = verificationToken.getUser();
        verifiedUser.setActive(true);

        userRepository.save(verifiedUser);

        // Delete the verification token from the table because it is of no use.
        verificationTokenRepository.delete(verificationToken);

        return verifiedUser;
    }
}
