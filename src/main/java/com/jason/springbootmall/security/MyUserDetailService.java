package com.jason.springbootmall.security;

import com.jason.springbootmall.dao.UserDao;
import com.jason.springbootmall.model.User;
import java.util.List;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MyUserDetailService implements UserDetailsService {

  private final UserDao userDao;

  public MyUserDetailService(UserDao userDao) {
    this.userDao = userDao;
  }

  @Override
  // Spring Security 7 uses Jspecify nullness, so the override also marks non-null values.
  public @NonNull UserDetails loadUserByUsername(@NonNull String username) {
    User user = userDao.getUserByEmail(username);

    if (user == null) {
      throw new UsernameNotFoundException("User not found: " + username);
    }

    return new org.springframework.security.core.userdetails.User(
        user.getEmail(), user.getPassWord(), List.of(new SimpleGrantedAuthority(user.getRole())));
  }
}
