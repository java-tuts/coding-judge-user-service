package com.vighnesh153.userservice.services;

import com.vighnesh153.userservice.dto.UserLoginDto;
import com.vighnesh153.userservice.dto.UserRegistrationDto;
import com.vighnesh153.userservice.events.SuccessfulRegistrationEvent;
import com.vighnesh153.userservice.models.Session;
import com.vighnesh153.userservice.models.User;
import com.vighnesh153.userservice.models.VerificationToken;
import com.vighnesh153.userservice.repositories.SessionRepository;
import com.vighnesh153.userservice.repositories.UserRepository;
import com.vighnesh153.userservice.repositories.VerificationTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final SessionRepository sessionRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public UserServiceImpl(
            UserRepository userRepository,
            VerificationTokenRepository verificationTokenRepository,
            SessionRepository sessionRepository,
            ApplicationEventPublisher applicationEventPublisher
    ) {
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.sessionRepository = sessionRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public User registerUser(UserRegistrationDto userRegistrationDto) {
        if (userRepository.findByEmail(userRegistrationDto.getEmail()) != null) {
            return null;
        }

        User user = new User();
        user.setEmail(userRegistrationDto.getEmail());
        user.setFullName(userRegistrationDto.getFullName());
        user.setActive(false);
        user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));    // TODO: encrypt password

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

    @Override
    public Session loginUser(UserLoginDto userLoginDto) {
        User user = userRepository.findByEmail(userLoginDto.getEmail());
        if (user == null) {
            log.info("(LOGIN) No user found with email as " + userLoginDto.getEmail());
            return null;
        }

        if (!passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
            log.info("(LOGIN) Invalid password: " +
                    userLoginDto.getPassword() +
                    " for email: " +
                    user.getEmail());
            return null;
        }

        Session session = new Session(user);
        sessionRepository.save(session);

        return session;
    }

    @Override
    public Boolean isUserLoggedIn(String sessionToken) {
        Session session = sessionRepository.findBySessionToken(sessionToken);
        if (session == null) {
            return false;
        }

        if (session.getValidUntil().getTime() <= new Date().getTime()) {
            sessionRepository.delete(session);
            return false;
        }

        return true;
    }

    @Override
    public Boolean logoutUser(String sessionToken) {
        Session session = sessionRepository.findBySessionToken(sessionToken);

        if (session == null) {
            log.info("(LOGOUT) Session doesn't exist for with token: " + sessionToken);
            return false;
        }

        sessionRepository.delete(session);
        return true;
    }
}
