package com.vighnesh153.userservice.events.listeners;

import com.vighnesh153.userservice.events.SuccessfulRegistrationEvent;
import com.vighnesh153.userservice.models.User;
import com.vighnesh153.userservice.models.VerificationToken;
import com.vighnesh153.userservice.repositories.VerificationTokenRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class SuccessfulRegistrationEventListener implements ApplicationListener<SuccessfulRegistrationEvent> {

    private final VerificationTokenRepository verificationTokenRepository;

    public SuccessfulRegistrationEventListener(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @Override
    public void onApplicationEvent(SuccessfulRegistrationEvent successfulRegistrationEvent) {
        User registeredUser = successfulRegistrationEvent.getRegisteredUser();

        VerificationToken token = new VerificationToken(registeredUser);

        verificationTokenRepository.save(token);

        // TODO: Send the token string to email of the user
    }
}
