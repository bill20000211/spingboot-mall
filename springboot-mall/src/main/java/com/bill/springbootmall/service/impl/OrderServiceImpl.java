package com.bill.springbootmall.service.impl;

import com.bill.springbootmall.dao.OrderDao;
import com.bill.springbootmall.dao.ProductDao;
import com.bill.springbootmall.dto.BuyItem;
import com.bill.springbootmall.dto.CreateOrderRequest;
import com.bill.springbootmall.model.Order;
import com.bill.springbootmall.model.OrderItem;
import com.bill.springbootmall.model.Product;
import com.bill.springbootmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Override
    public Order getOrderById(Integer orderId) {
        Order order = orderDao.getOrderById(orderId);

        List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(orderId);

        order.setOrderItemList(orderItemList);

        return order;
    }

    @Transactional
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {
        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();
        List<BuyItem> buyItemList = createOrderRequest.getBuyItemList();

        // python 的 for(buyItem in buyItems):
        for (BuyItem buyItem : buyItemList) {
            Product product = productDao.getProductById(buyItem.getProductId());
            Integer productId = product.getProductId();
            // 計算金額
            int amount = product.getPrice() * buyItem.getQuantity();
            totalAmount += amount;
            // 轉換 BuyItem to OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(productId);
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(amount);

            orderItemList.add(orderItem);
        }

        // 創建訂單
        Integer orderId = orderDao.createOrder(userId, totalAmount);

        orderDao.createOrderItems(orderId, orderItemList);

        return orderId;
    }
}
