package com.jason.springbootmall.service.impl;

import com.jason.springbootmall.dao.UserDao;
import com.jason.springbootmall.dto.UserRegisterRequest;
import com.jason.springbootmall.model.User;
import com.jason.springbootmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {
  @Autowired private UserDao userDao;

  @Override
  public Integer register(UserRegisterRequest userRegisterRequest) {
    return userDao.createUser(userRegisterRequest);
  }

  @Override
  public User getUserById(Integer userId) {
    return userDao.getUserById(userId);
  }
}
