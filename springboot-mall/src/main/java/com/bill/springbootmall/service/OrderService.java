package com.bill.springbootmall.service;

import com.bill.springbootmall.dto.CreateOrderRequest;
import com.bill.springbootmall.model.Order;

public interface OrderService {

    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);

    Order getOrderById(Integer orderId);
}
