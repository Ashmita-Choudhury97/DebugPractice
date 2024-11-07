package com.debug.DebugPractice.controller;

import com.debug.DebugPractice.model.Item;
import com.debug.DebugPractice.repository.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    ItemRepository itemRepository;

    @GetMapping
    public List<Item> getAllItems()
    {
        return itemRepository.findAll();
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<Item> getItemById(@PathVariable Long id)
//    {
//        return itemRepository.findById(id)
//                .map(item->ResponseEntity.ok().body(item))
//                .orElse(ResponseEntity.notFound().build());
//    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        try {
            // Logging the incoming request
            logger.info("Fetching item with ID: {}", id);

            Optional<Item> optionalItem = itemRepository.findById(id);

            if (optionalItem.isPresent()) {
                Item item = optionalItem.get();

                // Transforming item to custom response format
                Item itemResponse = new Item(item.getId(), item.getName(), item.getDescription());

                // Logging successful retrieval
                logger.info("Item found: {}", itemResponse);

                return ResponseEntity.ok(itemResponse);
            } else {
                // Logging that item was not found
                logger.warn("Item with ID {} not found", id);

                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new Item("Item not found"));
            }
        } catch (Exception e) {
            // Logging exception details
            logger.error("An error occurred while fetching item with ID {}: {}", id, e.getMessage(), e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Item("An unexpected error occurred"));
        }
    }

    @PostMapping
    public Item createItem(@RequestBody Item item)
    {
        return  itemRepository.save(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody Item itemDetails) {
        return itemRepository.findById(id)
                .map(item -> {
                    item.setName(itemDetails.getName());
                    Item updatedItem = itemRepository.save(item);
                    return ResponseEntity.ok().body(updatedItem);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable Long id) {
        return itemRepository.findById(id)
                .map(item -> {
                    itemRepository.delete(item);  // Delete the item
                    return ResponseEntity.ok("Item deleted successfully");  // Return 200 OK with a message
                })
                .orElse(ResponseEntity.status(404).body("Item not found"));  // Return 404 with a message
    }

}
