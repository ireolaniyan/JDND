package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private CartController cartController;

    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObject(cartController, "userRepository", userRepo);
        TestUtils.injectObject(cartController, "cartRepository", cartRepo);
        TestUtils.injectObject(cartController, "itemRepository", itemRepo);
    }

    @Test
    public void add_to_cart_happy_path() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(0L);
        request.setQuantity(1);
        request.setUsername("Ire");

        // Create dummy cart and user
        Cart cart = new Cart();
        cart.setId(0L);

        User user = new User();
        user.setUsername("Ire");
        user.setCart(cart);
        cart.setUser(user);

        // Create dummy item for the cart
        Item item = new Item();
        item.setId(0L);
        item.setName("New Test item");
        item.setPrice(BigDecimal.valueOf(20.00));

        // Stub the user repo
        when(userRepo.findByUsername(request.getUsername())).thenReturn(user);
        // Stub the item repo
        when(itemRepo.findById(item.getId())).thenReturn(Optional.of(item));

        final ResponseEntity<Cart> response = cartController.addTocart(request);
        Cart cart2 = response.getBody();
        assertEquals("Ire", cart2.getUser().getUsername());
        assertEquals(1, cart2.getItems().size());
    }

    @Test
    public void remove_from_cart_happy_path() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(0L);
        request.setQuantity(1);
        request.setUsername("Ire");

        // Create dummy cart and user
        Cart cart = new Cart();
        cart.setId(0L);

        User user = new User();
        user.setUsername("Ire");
        user.setCart(cart);
        cart.setUser(user);

        // Create a dummy item for the cart
        Item item = new Item();
        item.setId(0L);
        item.setName("New test item");
        item.setPrice(BigDecimal.valueOf(18.99));

        // Stub the user repo
        when(userRepo.findByUsername(request.getUsername())).thenReturn(user);
        // Stub the item repo
        when(itemRepo.findById(item.getId())).thenReturn(Optional.of(item));

        final ResponseEntity<Cart> response = cartController.removeFromcart(request);

        Cart cart2 = response.getBody();
        assertTrue(cart2.getItems().size() == 0);
    }
}
