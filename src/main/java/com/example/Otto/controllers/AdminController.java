package com.example.Otto.controllers;

import com.example.Otto.models.User;
import com.example.Otto.models.enums.Role;
import com.example.Otto.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import java.io.IOException;

/**
 * The AdminController class handles administrative functions for the Otto application.
 * It provides endpoints for listing users, banning users, and editing user roles.
 * This controller is restricted to users with the 'ROLE_ADMIN' authority.
 *
 * @author Dmytro Ukrainets
 * @version 1.0
 * @since 09.06.2024
 */
@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {
    /**
     * Service for managing users.
     */
    private final UserService userService;

    /**
     * Displays the admin panel with a list of all users.
     *
     * @param model the Model object to add attributes to.
     * @return the name of the admin view template.
     */
    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("users", userService.list());
        return "admin";
    }

    /**
     * Bans a user based on their ID.
     *
     * @param id the ID of the user to ban.
     * @return a redirect to the admin panel.
     */
    @PostMapping("/admin/user/ban/{id}")
    public String userBan(@PathVariable("id") Long id) {
        userService.banUser(id);
        return "redirect:/admin";
    }

    /**
     * Displays the user edit form for a specific user.
     *
     * @param userId the ID of the user to edit.
     * @param model  the Model object to add attributes to.
     * @return the name of the user-edit view template.
     */
    @GetMapping("/admin/user/edit/{userId}")
    public String userEdit(@PathVariable("userId") Long userId, Model model) {
        User user = userService.findById(userId);
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "user-edit";
    }

    /**
     * Handles the submission of the user edit form, updating the user's role.
     *
     * @param userId the ID of the user to edit.
     * @param role   the new role to assign to the user.
     * @return a redirect to the admin panel.
     */
    @PostMapping("/admin/user/edit")
    public String userEdit(@RequestParam("userId") Long userId,
                           @RequestParam(name = "roles", required = false) String role,
                           @RequestParam(name = "newPhone", required = false) String newPhone,
                           @RequestParam(name = "newName", required = false) String newName,
                           @RequestParam(name = "newAvatar", required = false) MultipartFile newAvatar) {
        User user = userService.findById(userId);
        try {
            userService.editUser(user, role, newPhone, newName, newAvatar);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/admin";
    }
}