package com.bill.springbootmall.service;

import com.bill.springbootmall.dto.CreateOrderRequest;
import com.bill.springbootmall.dto.OrderQueryParams;
import com.bill.springbootmall.model.Order;

import java.util.List;

public interface OrderService {

    Integer countOrder(OrderQueryParams orderQueryParams);

    List<Order> getOrders(OrderQueryParams orderQueryParams);

    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);

    Order getOrderById(Integer orderId);
}
