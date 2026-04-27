package com.jason.springbootmall.service.impl;

import com.jason.springbootmall.dao.UserDao;
import com.jason.springbootmall.model.User;
import com.jason.springbootmall.model.UserOauth;
import com.jason.springbootmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UserServiceImpl implements UserService {

  @Autowired private UserDao userDao;

  @Override
  public User getUserByEmail(String email) {
    return userDao.getUserByEmail(email);
  }

  @Override
  public Integer register(String email, String encodedPassword) {
    User existing = userDao.getUserByEmail(email);
    if (existing != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email 已被註冊");
    }
    return userDao.createUser(email, encodedPassword);
  }

  @Override
  public Integer findOrCreateOauthUser(String email, String provider, String providerUserId) {
    // 先查 oauth 綁定紀錄
    UserOauth oauth = userDao.getOauthByProvider(provider, providerUserId);
    if (oauth != null) {
      return oauth.getUserId();
    }

    // 以 email 查現有帳號（合併）
    User user = userDao.getUserByEmail(email);
    Integer userId;
    if (user != null) {
      userId = user.getUserId();
    } else {
      userId = userDao.createUser(email, null);
    }

    userDao.createOauth(userId, provider, providerUserId);
    return userId;
  }
}
