package com.vighnesh153.userservice.models;

import com.vighnesh153.userservice.annotations.EmailAddress;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // GenerationType.IDENTITY means whatever ID is assigned
    // by DB upon new row creation, use that.


    private String fullName;

    private String password;

    private boolean active;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id",
                    referencedColumnName = "id"
            )
    )
    private List<Role> roles;
    // Join table -> Creates a new table for user and roles mapping based on
    // parameters passed.


    @Column(unique = true)
    @EmailAddress
    private String email;
}
