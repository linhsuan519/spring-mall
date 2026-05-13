package com.jason.springbootmall.controller;

import com.jason.springbootmall.dto.UserLoginRequest;
import com.jason.springbootmall.dto.UserLoginResponse;
import com.jason.springbootmall.dto.UserRegisterRequest;
import com.jason.springbootmall.model.User;
import com.jason.springbootmall.service.UserService;
import com.jason.springbootmall.util.JwtUtil;
import jakarta.validation.Valid;
import java.util.Collection;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  @Autowired private UserService userService;

  @GetMapping("/users/me")
  public ResponseEntity<User> getCurrentUser(Authentication authentication) {
    String email = authentication.getName();

    User user = userService.getUserByEmail(email);

    if (user == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    } else {
      return ResponseEntity.status(HttpStatus.OK).body(user);
    }
  }

  @PostMapping("/users/register")
  public ResponseEntity<User> register(
      @RequestBody @Valid UserRegisterRequest userRegisterRequest) {
    Integer userId = userService.register(userRegisterRequest);

    User user = userService.getUserById(userId);

    return ResponseEntity.status(HttpStatus.CREATED).body(user);
  }

  @PostMapping("/users/login")
  public ResponseEntity<UserLoginResponse> login(
      @RequestBody @Valid UserLoginRequest userLoginRequest) {
    User user = userService.login(userLoginRequest);

    String token = JwtUtil.generateToken(user.getEmail());
    Date loginTime = new Date();
    Date expiresAt = JwtUtil.getExpiration(token);

    UserLoginResponse userLoginResponse =
        new UserLoginResponse(
            user, token, loginTime, expiresAt, JwtUtil.TOKEN_VALIDITY_SECONDS);

    return ResponseEntity.status(HttpStatus.OK).body(userLoginResponse);
  }

  @GetMapping("/testuser")
  public String testuser(Authentication authentication) {
    // 取得使用者的帳號
    String username = authentication.getName();
    // 取得使用者的權限
    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

    return "test " + username + " : " + authorities;
  }
}
