package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;
    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObject(userController, "userRepository", userRepo);
        TestUtils.injectObject(userController, "cartRepository", cartRepo);
        TestUtils.injectObject(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void create_user_happy_path() {
        when(encoder.encode("password567")).thenReturn("thisIsMocked");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("Ire");
        r.setPassword("password567");
        r.setConfirmPassword("password567");

        final ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("Ire", user.getUsername());
        assertEquals("thisIsMocked", user.getPassword());

    }

    @Test
    public void verify_get_user_by_username() {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("Ire");
        r.setPassword("password567");
        r.setConfirmPassword("password567");

        final ResponseEntity<User> response = userController.createUser(r);
        User user = response.getBody();
        when(userRepo.findByUsername(r.getUsername())).thenReturn(user);

        ResponseEntity<User> response2 = userController.findByUserName(r.getUsername());
        User user2 = response2.getBody();
        assertEquals("Ire", user2.getUsername());
    }

    @Test
    public void verify_get_user_by_userId() {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("Ire");
        r.setPassword("password567");
        r.setConfirmPassword("password567");

        final ResponseEntity<User> response = userController.createUser(r);
        User user = response.getBody();
        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));

        ResponseEntity<User> response2 = userController.findById(user.getId());
        User user2 = response2.getBody();

        assertEquals(0, user2.getId());

    }
}
