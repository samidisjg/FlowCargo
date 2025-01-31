package com.sami.microservices.order.service;

import com.sami.microservices.order.client.InventoryClient;
import com.sami.microservices.order.model.Order;
import com.sami.microservices.order.repository.OrderRepository;
import com.sami.microservices.order.request.OrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    public void placeOrder(OrderRequest orderRequest) {
       var isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());

       if (isProductInStock) {
           //map OrderRequest to Order Object
           Order order = new Order();
           order.setOrderNumber(UUID.randomUUID().toString());
           order.setPrice(orderRequest.price());
           order.setSkuCode(orderRequest.skuCode());
           order.setQuantity(orderRequest.quantity());

           //save order to OrderRepository
           orderRepository.save(order);

       }else {
           throw new RuntimeException("Product with sku code " + orderRequest.skuCode() + " is not in stock");
       }
    }
}