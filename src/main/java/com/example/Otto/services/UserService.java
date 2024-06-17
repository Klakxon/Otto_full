package com.example.Otto.services;

import com.example.Otto.models.Image;
import com.example.Otto.models.User;
import com.example.Otto.models.enums.Role;
import com.example.Otto.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * The UserService class provides services related to User entities.
 * It handles operations such as creating users, listing users, banning/unbanning users,
 * changing user roles, and finding users by ID.
 *
 * @author Dmytro Ukrainets
 * @version 1.0
 * @since 09.06.2024
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PerfumeService perfumeService;

    /**
     * Creates a new user.
     *
     * @param user the user to create
     * @return true if the user is created successfully, false otherwise
     */
    public boolean createUser(User user, MultipartFile file) throws IOException {
        String email = user.getEmail();
        if (userRepository.findByEmail(email) != null) {
            return false;
        }
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(Role.ROLE_USER);
        if (file != null && !file.isEmpty()) {
            Image avatar = perfumeService.toImageEntity(file);
            avatar.setPreviewImage(false);
            user.setAvatar(avatar);
        } else {
            user.setAvatar(null);
        }
        log.info("Saving new User with email: {}", email);
        userRepository.save(user);
        return true;
    }

    /**
     * Retrieves a list of all users.
     *
     * @return List<User> list of users
     */
    public List<User> list() {
        return userRepository.findAll();
    }

    /**
     * Bans or unbans a user by ID.
     *
     * @param id the ID of the user to ban/unban
     */
    public void banUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            if (user.isActive()) {
                user.setActive(false);
                log.info("Ban user with id = {}; email = {}", user.getId(), user.getEmail());
            } else {
                user.setActive(true);
                log.info("Unban user with id = {}; email = {}", user.getId(), user.getEmail());
            }
            userRepository.save(user);
        } else {
            log.info("User with id: {} not found", id);
        }
    }

    /**
     * Changes the role of a user.
     *
     * @param user      the user whose role to change
     * @param role      the new role for the user
     * @param newPhone
     * @param newName
     * @param newAvatar
     */
    public void editUser(User user, String role, String newPhone, String newName, MultipartFile newAvatar) throws IOException {
        user.getRoles().clear();
        user.getRoles().add(Role.valueOf(role));
        user.setPhone(newPhone);
        user.setName(newName);

        if (newAvatar.getSize() != 0) {
            Image avatar = perfumeService.toImageEntity(newAvatar);
            avatar.setPreviewImage(false);
            user.setAvatar(avatar);
        }
        userRepository.save(user);
    }

    /**
     * Retrieves a user by ID.
     *
     * @param userId the ID of the user to retrieve
     * @return User object representing the retrieved user
     */
    public User findById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public void editProfile(User user, String name, String phone, String password, MultipartFile file) throws IOException {
        if (file.getSize() != 0) {
            Image avatar = perfumeService.toImageEntity(file);
            avatar.setPreviewImage(false);
            user.setAvatar(avatar);
        }
        if (!name.isEmpty()) {
            user.setName(name);
        }
        if (!phone.isEmpty()) {
            user.setPhone(phone);
        }
        if (!password.isEmpty()) {
            user.setPassword(passwordEncoder.encode(password));
        }
        userRepository.save(user);
    }
}
