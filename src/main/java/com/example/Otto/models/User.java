package com.example.Otto.models;

import com.example.Otto.models.enums.Role;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * The User class represents a user entity in the system.
 *
 * @author Dmytro Ukrainets
 * @version 1.0
 * @since 09.06.2024
 */
@Entity
@Table(name = "User")
@Data
public class User implements UserDetails {
    /**
     * The unique identifier for the user.
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The email address of the user (unique).
     */
    @Column(name = "email", unique = true)
    private String email;

    /**
     * The phone number of the user.
     */
    @Column(name = "phone")
    private String phone;

    /**
     * The name of the user.
     */
    @Column(name = "name")
    private String name;

    /**
     * Indicates whether the user account is active or not.
     */
    @Column(name = "active")
    private boolean active;

    /**
     * The avatar image associated with the user.
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "image_id")
    private Image avatar;

    /**
     * The password of the user.
     */
    @Column(name = "password", length = 1000)
    private String password;

    /**
     * The set of roles assigned to the user.
     */
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    /**
     * The date and time when the user was created.
     */
    private LocalDateTime dateOfCreated;

    /**
     * Initializes the dateOfCreated field with the current date and time before persisting the entity.
     */
    @PrePersist
    private void init() {
        dateOfCreated = LocalDateTime.now();
    }

    /**
     * Checks if the user has admin role.
     *
     * @return True if the user has admin role, otherwise false.
     */
    public boolean isAdmin() {
        return roles.contains(Role.ROLE_ADMIN);
    }

    // Security Methods

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
