package com.bill.springbootmall.dao.impl;

import com.bill.springbootmall.model.Product;

public interface ProductDao {

    Product getProductById(Integer productId);
}
