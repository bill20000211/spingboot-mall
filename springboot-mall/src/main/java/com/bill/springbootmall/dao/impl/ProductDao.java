package com.bill.springbootmall.dao.impl;

import com.bill.springbootmall.dto.ProductRequest;
import com.bill.springbootmall.model.Product;

public interface ProductDao {

    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId,ProductRequest productRequest);
}
