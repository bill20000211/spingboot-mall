package com.bill.springbootmall.service.impl;

import com.bill.springbootmall.dao.ProductDao;
import com.bill.springbootmall.dto.ProductQueryParams;
import com.bill.springbootmall.dto.ProductRequest;
import com.bill.springbootmall.model.Product;
import com.bill.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    public List<Product> getProducts(ProductQueryParams productQueryParams) {

        List<Product> productList = productDao.getProducts(productQueryParams);

        return productList;
    }

    @Override
    public Product getProductById(Integer productId) {
        Product product = productDao.getProductById(productId);
        return product;
    }

    @Override
    public Integer createProduct(ProductRequest productRequest) {
        Integer productId = productDao.createProduct(productRequest);
        return productId;
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        productDao.updateProduct(productId, productRequest);
    }

    @Override
    public void deleteProductById(Integer productId) {
        productDao.deleteProductById(productId);
    }

    @Override
    public Integer countProducts(ProductQueryParams productQueryParams) {
        return productDao.countProducts(productQueryParams);
    }
}
