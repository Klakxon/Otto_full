package com.example.Otto.controllers;

import com.example.Otto.models.User;
import com.example.Otto.services.PerfumeService;
import com.example.Otto.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

/**
 * The UserController class handles user-related requests in the Otto application.
 * It provides endpoints for user login and registration.
 *
 * @author Dmytro Ukrainets
 * @version 1.0
 * @since 09.06.2024
 */
@Controller
@RequiredArgsConstructor
public class UserController {
    /**
     * Service for managing users.
     */
    private final UserService userService;
    private final PerfumeService perfumeService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Handles GET requests to the login page.
     *
     * @return the name of the view to render for the login page.
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * Handles GET requests to the registration page.
     *
     * @return the name of the view to render for the registration page.
     */
    @GetMapping("/registration")
    public String registration(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Failed to register user. Please try again.");
        }
        return "registration";
    }

    /**
     * Handles POST requests to register a new user.
     *
     * @param user the user details to register.
     * @return the name of the view to render or a redirect to the login page.
     */
    @PostMapping("/registration")
    public String createUser(@RequestParam("file") MultipartFile file, User user, Model model) {
        try {
            boolean isUserCreated = userService.createUser(user, file);
            if (!isUserCreated) {
                model.addAttribute("errorMessage", "User with this email already exists.");
                return "registration";
            }
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Failed to register user. Please try again.");
            return "registration";
        }
        return "redirect:/login";
    }

    @GetMapping("/user/edit")
    public String editProfileForm(Model model, Principal principal) {
        User user = perfumeService.getUserByPrincipal(principal);
        model.addAttribute("user", user);
        return "user-edit-profile";
    }

    @PostMapping("/user/edit")
    public String editProfile(@RequestParam("userId") Long userId,
                              @RequestParam("file") MultipartFile file,
                              @RequestParam("name") String name,
                              @RequestParam("phone") String phone,
                              @RequestParam("password") String password) {
        User user = userService.findById(userId);
        try {
            userService.editProfile(user, name, phone, password, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/perfumes";
    }
}