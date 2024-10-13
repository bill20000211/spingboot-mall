package com.bill.springbootmall.controller;

import com.bill.springbootmall.dto.CreateOrderRequest;
import com.bill.springbootmall.dto.OrderQueryParams;
import com.bill.springbootmall.model.Order;
import com.bill.springbootmall.service.OrderService;
import com.bill.springbootmall.util.JwtUtil;
import com.bill.springbootmall.util.Page;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Validated
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtUtil jwtUtil;

    private final static Logger log = LoggerFactory.getLogger(OrderController.class);

    @GetMapping("/users/orders")
    public ResponseEntity<Page<Order>> getOrders(
            //@PathVariable Integer userId,
            @RequestParam(defaultValue = "10") @Max(100) @Min(0) Integer limit,
            @RequestParam(defaultValue = "0") @Min(0) Integer offset
    ){
        // 從 Token 解析出 userId
        Integer userId = jwtUtil.getUserIdFromToken();

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 前端傳遞之參數
        OrderQueryParams orderQueryParams = new OrderQueryParams();
        orderQueryParams.setUserId(userId);
        orderQueryParams.setLimit(limit);
        orderQueryParams.setOffset(offset);

        // 取得 order list
        List<Order> orderList = orderService.getOrders(orderQueryParams);

        // 取得 order 總筆數（用於計算幾頁）
        Integer total = orderService.countOrder(orderQueryParams);

        // 分頁
        Page<Order> pageInfo = new Page<>();
        pageInfo.setLimit(limit);
        pageInfo.setOffset(offset);
        pageInfo.setTotal(total); // 計算幾頁
        pageInfo.setResults(orderList);

        return ResponseEntity.status(HttpStatus.OK).body(pageInfo);

    }

    // 訂單是帳號的附屬功能，在所有帳號中 ID 為 userId 的帳號中創建訂單
    @PostMapping("/users/orders")
    public ResponseEntity<Order> createOrder(// @PathVariable Integer userId,
                                             @RequestBody @Valid CreateOrderRequest createOrderRequest) {
        // 從 Token 解析出 userId
        Integer userId = jwtUtil.getUserIdFromToken();

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer orderId = orderService.createOrder(userId, createOrderRequest);

        Order order = orderService.getOrderById(orderId);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
}
