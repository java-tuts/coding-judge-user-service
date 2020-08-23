package com.vighnesh153.userservice.repositories;

import com.vighnesh153.userservice.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {

    Session findBySessionToken(String token);
}
