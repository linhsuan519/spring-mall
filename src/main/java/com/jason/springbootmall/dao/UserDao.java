package com.jason.springbootmall.dao;

import com.jason.springbootmall.model.User;
import com.jason.springbootmall.model.UserOauth;

public interface UserDao {

  User getUserById(Integer userId);

  User getUserByEmail(String email);

  Integer createUser(String email, String encodedPassword);

  UserOauth getOauthByProvider(String provider, String providerUserId);

  void createOauth(Integer userId, String provider, String providerUserId);
}
