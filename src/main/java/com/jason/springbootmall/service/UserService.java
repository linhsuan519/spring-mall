package com.jason.springbootmall.service;

import com.jason.springbootmall.model.User;

public interface UserService {

  User getUserByEmail(String email);

  // 帳號密碼註冊
  Integer register(String email, String password);

  // OAuth2 登入（Google / LINE）：找到或建立使用者，回傳 userId
  Integer findOrCreateOauthUser(String email, String provider, String providerUserId);
}
