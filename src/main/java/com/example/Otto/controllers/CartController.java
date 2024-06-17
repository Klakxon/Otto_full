package com.example.Otto.controllers;

import com.example.Otto.exeptions.UnauthorizedException;
import com.example.Otto.models.CartItem;
import com.example.Otto.models.Order;
import com.example.Otto.models.Perfume;
import com.example.Otto.models.User;
import com.example.Otto.repository.UserRepository;
import com.example.Otto.services.CartService;
import com.example.Otto.services.PerfumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final PerfumeService perfumeService;
    private final UserRepository userRepository;

    @GetMapping
    public String viewCart(Model model, Principal principal) {
        Long userId = getUserId(principal);
        List<CartItem> cartItems = cartService.getCartItems(userId);
        model.addAttribute("cartItems", cartItems); // Переконайтеся, що це додано
        return "cart"; // Це повертає шаблон 'cart.html'
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long perfumeId, @RequestParam int quantity, Principal principal) {
        Long userId = getUserId(principal);
        Perfume perfume = perfumeService.getPerfumeById(perfumeId);
        cartService.addToCart(userId, perfume, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam Long perfumeId, Principal principal) {
        Long userId = getUserId(principal);
        cartService.removeFromCart(userId, perfumeId);
        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String checkout(Principal principal, Model model) {
        Long userId = getUserId(principal);
        Order order = cartService.checkout(userId);
        model.addAttribute("order", order);
        return "orderConfirmation";
    }

    private Long getUserId(Principal principal) {
        if (principal != null) {
            User user = userRepository.findByEmail(principal.getName());
            if (user != null) {
                return user.getId();
            } else {
                throw new UsernameNotFoundException("User not found with email: " + principal.getName());
            }
        }
        throw new UnauthorizedException("User is not authenticated");
    }

}
