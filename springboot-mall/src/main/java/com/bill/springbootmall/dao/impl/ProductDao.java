package com.bill.springbootmall.dao.impl;

import com.bill.springbootmall.constant.ProductCategory;
import com.bill.springbootmall.dto.ProductRequest;
import com.bill.springbootmall.model.Product;

import java.util.List;

public interface ProductDao {

    List<Product> getProducts(ProductCategory category,
                              String search);

    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId,
                       ProductRequest productRequest);

    void deleteProductById(Integer productId);
}
