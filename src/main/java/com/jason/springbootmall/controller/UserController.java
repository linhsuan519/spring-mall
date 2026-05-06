package com.jason.springbootmall.controller;

import com.jason.springbootmall.dto.UserLoginRequest;
import com.jason.springbootmall.dto.UserRegisterRequest;
import com.jason.springbootmall.model.User;
import com.jason.springbootmall.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  @Autowired private UserService userService;

  @Autowired private UserDetailsService userDetailsService;

  private final SecurityContextRepository securityContextRepository =
      new HttpSessionSecurityContextRepository();

  @PostMapping("/users/register")
  public ResponseEntity<User> register(
      @RequestBody @Valid UserRegisterRequest userRegisterRequest) {
    Integer userId = userService.register(userRegisterRequest);

    User user = userService.getUserById(userId);

    return ResponseEntity.status(HttpStatus.CREATED).body(user);
  }

  @PostMapping("/users/login")
  public ResponseEntity<User> login(
      @RequestBody @Valid UserLoginRequest userLoginRequest,
      HttpServletRequest request,
      HttpServletResponse response) {
    User user = userService.login(userLoginRequest);
    UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

    Authentication authentication =
        UsernamePasswordAuthenticationToken.authenticated(
            userDetails, null, userDetails.getAuthorities());
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(authentication);
    SecurityContextHolder.setContext(securityContext);
    request.getSession(true);
    request.changeSessionId();
    securityContextRepository.saveContext(securityContext, request, response);

    return ResponseEntity.status(HttpStatus.OK).body(user);
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
