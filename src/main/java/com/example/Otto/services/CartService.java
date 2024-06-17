package com.example.Otto.services;

import com.example.Otto.models.*;
import com.example.Otto.repository.CartItemRepository;
import com.example.Otto.repository.CartRepository;
import com.example.Otto.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartService {
    private final UserService userService;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;

    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    public void addToCart(Long userId, Perfume perfume, int quantity) {
        Cart cart = getCartByUserId(userId);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(userService.findById(userId));
        }

        CartItem item = new CartItem();
        item.setPerfume(perfume);
        item.setQuantity(quantity);
        item.setCart(cart);

        cart.getItems().add(item);
        cartRepository.save(cart);
    }

    public void removeFromCart(Long userId, Long perfumeId) {
        Cart cart = getCartByUserId(userId);
        if (cart != null) {
            cart.getItems().removeIf(item -> item.getPerfume().getId().equals(perfumeId));
            cartRepository.save(cart);
        }
    }

    public Order checkout(Long userId) {
        Cart cart = getCartByUserId(userId);
        if (cart == null || cart.getItems().isEmpty()) {
            return null;
        }

        Order order = new Order();
        order.setUser(cart.getUser());

        double total = 0;
        for (CartItem item : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setPerfume(item.getPerfume());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setOrder(order);

            order.getItems().add(orderItem);
            total += item.getQuantity() * item.getPerfume().getPrice();
        }
        order.setTotal(total);

        orderRepository.save(order);
        cartRepository.delete(cart);

        return order;
    }

    public List<CartItem> getCartItems(Long userId) {
        return cartItemRepository.findByUserId(userId);
    }
}
