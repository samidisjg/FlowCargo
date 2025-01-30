package com.sami.microservices.inventory.controller;


import com.sami.microservices.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@RestController
//@RequestMapping("/api/inventory")
//@RequiredArgsConstructor
//public class InventoryController {
//    private final InventoryService inventoryService;
//
//    @GetMapping
//    @ResponseStatus(HttpStatus.OK)
//    public boolean isInStock(@RequestParam String skuCode, @RequestParam Integer quantity) {
//        return inventoryService.isInStock(skuCode, quantity);
//    }
//
//}

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)


    public boolean isInStock(
            @RequestParam List<String> skuCode,
            @RequestParam Integer quantity) {
        return inventoryService.isInStock(skuCode, quantity);
    }
}
