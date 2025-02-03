package com.sami.microservices.order.service;

import com.sami.microservices.order.client.InventoryClient;
import com.sami.microservices.order.event.OrderPlacedEvent;
import com.sami.microservices.order.model.Order;
import com.sami.microservices.order.repository.OrderRepository;
import com.sami.microservices.order.request.OrderRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;


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

           //send the message to kafka topic
          OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent(order.getOrderNumber(), orderRequest.userDetails().email());
           log.info("Start- Sending OrderPlacedEvent {} to Kafka Topic", orderPlacedEvent);
           kafkaTemplate.send("order-placed", orderPlacedEvent);
           log.info("End- Sending OrderPlacedEvent {} to Kafka Topic", orderPlacedEvent);

       }else {
           throw new RuntimeException("Product with sku code " + orderRequest.skuCode() + " is not in stock");
       }
    }
}