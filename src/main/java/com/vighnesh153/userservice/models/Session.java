package com.vighnesh153.userservice.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "sessions")
@Getter
@Setter
public class Session {
    @Getter
    private static final int VALIDITY_TIME_IN_MINUTES = 24 * 60; // in minutes

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sessionToken;

    @OneToOne(targetEntity = User.class)
    private User user;

    private Date validUntil;

    public Session() { }

    public Session(User user) {
        this.sessionToken = generateRandomUniqueToken();
        this.user = user;

        this.validUntil = calculateValidUntilTime();
    }

    private static String generateRandomUniqueToken() {
        return UUID.randomUUID().toString();
    }

    private Date calculateValidUntilTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, VALIDITY_TIME_IN_MINUTES);
        return calendar.getTime();
    }
}
