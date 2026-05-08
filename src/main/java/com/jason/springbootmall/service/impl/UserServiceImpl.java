package com.jason.springbootmall.service.impl;

import com.jason.springbootmall.dao.UserDao;
import com.jason.springbootmall.dto.UserLoginRequest;
import com.jason.springbootmall.dto.UserRegisterRequest;
import com.jason.springbootmall.model.User;
import com.jason.springbootmall.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UserServiceImpl implements UserService {

  private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

  @Autowired private UserDao userDao;

  @Autowired private PasswordEncoder passwordEncoder;

  @Override
  public Integer register(UserRegisterRequest userRegisterRequest) {
    User user = userDao.getUserByEmail(userRegisterRequest.getEmail());

    if (user != null) {
      log.warn("email : {} already exists", userRegisterRequest.getEmail());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    // hash 原始密碼
    userRegisterRequest.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword()));

    return userDao.createUser(userRegisterRequest);
  }

  @Override
  public User getUserById(Integer userId) {
    return userDao.getUserById(userId);
  }

  @Override
  public User getUserByEmail(String email) {
    return userDao.getUserByEmail(email);
  }

  @Override
  public User login(UserLoginRequest userLoginRequest) {
    User user = userDao.getUserByEmail(userLoginRequest.getEmail());

    if (user == null) {
      log.warn("email : {} is not registered", userLoginRequest.getEmail());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    if (passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassWord())) {
      return user;
    } else {
      log.warn("email : {} password is incorrect", userLoginRequest.getEmail());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }
}
