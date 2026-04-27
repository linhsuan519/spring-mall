package com.jason.springbootmall.controller;

import com.jason.springbootmall.dto.LoginResponse;
import com.jason.springbootmall.dto.UserLoginRequest;
import com.jason.springbootmall.dto.UserRegisterRequest;
import com.jason.springbootmall.model.User;
import com.jason.springbootmall.service.UserService;
import com.jason.springbootmall.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired private UserService userService;
  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private JwtUtil jwtUtil;

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody @Valid UserRegisterRequest request) {
    String encodedPassword = passwordEncoder.encode(request.getPassword());
    Integer userId = userService.register(request.getEmail(), encodedPassword);

    User user = userService.getUserByEmail(request.getEmail());
    String token = jwtUtil.generateToken(userId, user.getEmail(), user.getRole());

    return ResponseEntity.status(HttpStatus.CREATED).body(new LoginResponse(token));
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody @Valid UserLoginRequest request) {
    User user = userService.getUserByEmail(request.getEmail());

    if (user == null
        || user.getPassword() == null
        || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email 或密碼錯誤");
    }

    String token = jwtUtil.generateToken(user.getUserId(), user.getEmail(), user.getRole());
    return ResponseEntity.ok(new LoginResponse(token));
  }
}
