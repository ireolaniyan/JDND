package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;

    private UserRepository userRepo = mock(UserRepository.class);
    private OrderRepository orderRepo = mock(OrderRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObject(orderController, "userRepository", userRepo);
        TestUtils.injectObject(orderController, "orderRepository", orderRepo);
    }

    @Test
    public void submit_order_happy_path() {
        // Create a dummy cart and user
        Cart cart = new Cart();
        cart.setId(0L);

        User user = new User();
        user.setUsername("Ire");
        user.setCart(cart);
        cart.setUser(user);

        // Create a dummy item for the cart
        Item item = new Item();
        item.setId(0L);
        item.setName("Test item");
        item.setPrice(BigDecimal.valueOf(20.00));

        // Add the item to the cart
        cart.addItem(item);

        // Stub the user repo
        when(userRepo.findByUsername(user.getUsername())).thenReturn(user);

        final ResponseEntity<UserOrder> response = orderController.submit(user.getUsername());
        UserOrder order = response.getBody();
        assertEquals(BigDecimal.valueOf(20.00), order.getTotal());
    }
}
