package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;
    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObject(itemController, "itemRepository", itemRepo);
    }

    @Test
    public void getItemById_happy_path() {
        Item item = new Item();
        item.setId(0L);
        item.setPrice(BigDecimal.valueOf(150.99));
        item.setName("Air Jordan");
        item.setDescription("Michael Jordan Shoes by Nike");

        when(itemRepo.findById(0L)).thenReturn(Optional.of(item));
        ResponseEntity<Item> response = itemController.getItemById(0L);

        Item responseItem = response.getBody();

        assert responseItem != null;
        assertEquals(BigDecimal.valueOf(150.99), responseItem.getPrice());
        assertEquals("Air Jordan", responseItem.getName());
        assertEquals("Michael Jordan Shoes by Nike", responseItem.getDescription());
    }

    @Test
    public void getItems_happy_path() {
        List<Item> items = new ArrayList<>();

        Item item = new Item();
        item.setId(0L);
        item.setPrice(BigDecimal.valueOf(150.99));
        item.setName("Air Jordan");
        item.setDescription("Michael Jordan Shoes by Nike");

        Item item2 = new Item();
        item2.setId(1L);
        item2.setPrice(BigDecimal.valueOf(120.99));
        item2.setName("Toms");
        item2.setDescription("Toms' Shoes");

        Item item3 = new Item();
        item3.setId(1L);
        item3.setPrice(BigDecimal.valueOf(120.99));
        item3.setName("Yeezy Boosts");
        item3.setDescription("Yeezy Shoes by Kanye West");

        items.add(item);
        items.add(item2);
        items.add(item3);

        when(itemRepo.findAll()).thenReturn(items);
        ResponseEntity<List<Item>> response = itemController.getItems();
        List<Item> itemsResponse = response.getBody();
        assertEquals(3, itemsResponse.size());
    }
}
