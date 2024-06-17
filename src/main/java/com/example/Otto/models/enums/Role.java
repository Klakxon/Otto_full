package com.example.Otto.models.enums;

import org.springframework.security.core.GrantedAuthority;

/**
 * The Role enum represents the different roles that a user can have within the application.
 * Each role corresponds to a set of permissions and access levels.
 * This enum implements the GrantedAuthority interface from Spring Security.
 *
 * @author Dmytro Ukrainets
 * @version 1.0
 * @since 09.06.2024
 */
public enum Role implements GrantedAuthority {
    /**
     * ROLE_USER represents a regular user with basic access and permissions.
     */
    ROLE_USER,

    /**
     * ROLE_ADMIN represents an administrator with elevated permissions and access to admin functionalities.
     */
    ROLE_ADMIN;

    /**
     * Returns the name of the role as the authority granted to the user.
     * This method is used by Spring Security to determine the user's granted authorities.
     *
     * @return the name of the role as a String.
     */
    @Override
    public String getAuthority() {
        return name();
    }
}