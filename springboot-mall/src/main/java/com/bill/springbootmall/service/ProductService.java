package com.bill.springbootmall.service;

import com.bill.springbootmall.dto.ProductRequest;
import com.bill.springbootmall.model.Product;

public interface ProductService {

    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId , ProductRequest productRequest);
}
