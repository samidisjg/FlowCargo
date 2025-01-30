package com.sami.microservices.inventory.service;


import com.sami.microservices.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

//@Service
//@RequiredArgsConstructor
//public class InventoryService {
//
//    private final InventoryRepository inventoryRepository;
//
//    public boolean isInStock(String skuCode, Integer quantity) {
//        //Find an inventory for a given skuCode where quantity >=0
//        return inventoryRepository.existsBySkuCodeAndQuantityIsGreaterThanEqual(skuCode,quantity);
//    }
//}

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public boolean isInStock(List<String> skuCodes, Integer quantity) {
        // Check for all SKUs if they exist and have the required quantity
        return skuCodes.stream()
                .allMatch(skuCode -> inventoryRepository.existsBySkuCodeAndQuantityIsGreaterThanEqual(skuCode, quantity));
    }
}
