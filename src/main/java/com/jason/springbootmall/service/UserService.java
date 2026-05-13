package com.jason.springbootmall.service;

import com.jason.springbootmall.dto.UserLoginRequest;
import com.jason.springbootmall.dto.UserRegisterRequest;
import com.jason.springbootmall.model.User;

public interface UserService {
  //
  User getUserByEmail(String email);

  Integer register(UserRegisterRequest userRegisterRequest);

  User getUserById(Integer userId);

  User login(UserLoginRequest userLoginRequest);
}
