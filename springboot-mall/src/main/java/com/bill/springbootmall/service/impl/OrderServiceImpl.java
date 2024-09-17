package com.bill.springbootmall.service.impl;

import com.bill.springbootmall.dao.OrderDao;
import com.bill.springbootmall.dao.ProductDao;
import com.bill.springbootmall.dao.UserDao;
import com.bill.springbootmall.dto.BuyItem;
import com.bill.springbootmall.dto.CreateOrderRequest;
import com.bill.springbootmall.dto.OrderQueryParams;
import com.bill.springbootmall.model.Order;
import com.bill.springbootmall.model.OrderItem;
import com.bill.springbootmall.model.Product;
import com.bill.springbootmall.model.User;
import com.bill.springbootmall.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {
    // 在 Service 注入多個 Dao 是常見的
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    private final static Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    public Integer countOrder(OrderQueryParams orderQueryParams) {
        return orderDao.countOrder(orderQueryParams);
    }

    @Override
    public List<Order> getOrders(OrderQueryParams orderQueryParams) {
        List<Order> orderList = orderDao.getOrders(orderQueryParams);

        for (Order order : orderList) {
            List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(order.getOrderId());

            order.setOrderItemList(orderItemList);
        }

        return orderList;
    }

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
        // 檢查 User 是否存在
        User user = userDao.getUserById(userId);
        if (user == null) {
            log.warn("此 userId {} 不存在", userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();
        List<BuyItem> buyItemList = createOrderRequest.getBuyItemList();

        // python 的 for(buyItem in buyItems):
        for (BuyItem buyItem : buyItemList) {
            Product product = productDao.getProductById(buyItem.getProductId());

            // product 是否存在、庫存是否足夠
            if (product == null) {
                log.warn("商品 {} 不存在", buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            } else if (product.getStock() < buyItem.getQuantity()) {
                log.warn("商品 {} 的庫存不足，無法購買。剩餘庫存 {}",
                        buyItem.getProductId(), product.getStock());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            Integer productId = product.getProductId();
            // 扣除商品庫存
            productDao.updateStock(productId, product.getStock() - buyItem.getQuantity());

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
