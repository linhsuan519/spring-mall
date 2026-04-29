package com.jason.springbootmall.dao;

import com.jason.springbootmall.dto.UserRegisterRequest;
import com.jason.springbootmall.model.User;

public interface UserDao {

  Integer createUser(UserRegisterRequest userRegisterRequest);

  User getUserById(Integer userId);

  User getUserByEmail(String email);
}
