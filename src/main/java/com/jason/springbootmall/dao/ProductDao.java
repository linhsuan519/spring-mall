package com.jason.springbootmall.dao;

import com.jason.springbootmall.dto.ProductRequest;
import com.jason.springbootmall.model.Product;

public interface ProductDao {

    Product getProductById(Integer productId);
    Integer createProduct(ProductRequest productRequest);
    void updateProduct(Integer productId, ProductRequest productRequest);
    void deleteProductById(Integer productId);
}
