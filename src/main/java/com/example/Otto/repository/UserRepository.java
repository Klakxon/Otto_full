package com.example.Otto.repository;

import com.example.Otto.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The UserRepository interface provides access to the database for User entities.
 * It extends the JpaRepository interface, which provides CRUD operations for the User entity.
 *
 * @author Dmytro Ukrainets
 * @version 1.0
 * @since 09.06.2024
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Retrieves a user by their email.
     *
     * @param email the email of the user to search for
     * @return the user with the specified email, or null if not found
     */
    User findByEmail(String email);
}
