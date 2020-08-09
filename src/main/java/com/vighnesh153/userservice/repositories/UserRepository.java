package com.vighnesh153.userservice.repositories;

import com.vighnesh153.userservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

// Type of object and type of uniquely-identified-by
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
