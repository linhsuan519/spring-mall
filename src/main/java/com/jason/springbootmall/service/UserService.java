package com.jason.springbootmall.service;

import com.jason.springbootmall.dto.ProductQueryParams;
import com.jason.springbootmall.dto.UserRegisterRequest;
import com.jason.springbootmall.model.Product;
import com.jason.springbootmall.model.User;

public interface UserService {

    Integer register(UserRegisterRequest userRegisterRequest);
    User getUserById(Integer userId);
}
