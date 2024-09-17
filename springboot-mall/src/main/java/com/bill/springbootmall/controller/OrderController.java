package com.bill.springbootmall.controller;

import com.bill.springbootmall.dto.CreateOrderRequest;
import com.bill.springbootmall.model.Order;
import com.bill.springbootmall.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    // 訂單是帳號的附屬功能，在所有帳號中 ID 為 userId 的帳號中創建訂單
    @PostMapping("/users/{userId}/orders")
    public ResponseEntity<Order> createOrder(@PathVariable Integer userId,
                                         @RequestBody @Valid CreateOrderRequest createOrderRequest) {

        Integer orderId = orderService.createOrder(userId, createOrderRequest);

        Order order = orderService.getOrderById(orderId);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
}
