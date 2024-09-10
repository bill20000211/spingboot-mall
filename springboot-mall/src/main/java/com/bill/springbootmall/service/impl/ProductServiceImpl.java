package com.bill.springbootmall.service.impl;

import com.bill.springbootmall.dao.impl.ProductDao;
import com.bill.springbootmall.model.Product;
import com.bill.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    public Product getProductById(Integer productId) {
        Product product = productDao.getProductById(productId);
        return product;
    }
}
