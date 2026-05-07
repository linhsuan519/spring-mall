package com.jason.springbootmall.security;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Set;
import java.util.regex.Pattern;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class MySecurityConfig {

  private static final Set<String> PUBLIC_PATHS = Set.of("/users/register", "/users/login");
  private static final Pattern USER_ORDERS_PATH = Pattern.compile("^/users/\\d+/orders$");

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) {
    http.sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
        .cors(Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(MySecurityConfig::isPublicEndpoint)
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .httpBasic(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable);

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  private static boolean isPublicEndpoint(HttpServletRequest request) {
    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
      return true;
    }

    String path = getPathWithoutContextPath(request);

    return PUBLIC_PATHS.contains(path) || USER_ORDERS_PATH.matcher(path).matches();
  }

  private static String getPathWithoutContextPath(HttpServletRequest request) {
    String path = request.getRequestURI();
    String contextPath = request.getContextPath();

    if (contextPath != null && !contextPath.isBlank() && path.startsWith(contextPath)) {
      path = path.substring(contextPath.length());
    }

    if (path.length() > 1 && path.endsWith("/")) {
      path = path.substring(0, path.length() - 1);
    }

    return path;
  }

  // 測試用 in-memory users 範例：
  // 目前正式登入走 MyUserDetailService 查資料庫，所以這段先保留註解，不啟用。
  // 如果之後要同時支援 DB users 和 in-memory users，建議另外做一個合併版 UserDetailsService，
  // 不要直接打開這個 Bean，避免 Spring Security 同時找到兩個 UserDetailsService 時設定變混亂。
  //
  //  @Bean
  //  public InMemoryUserDetailsManager userDetailsManager(PasswordEncoder passwordEncoder) {
  //    UserDetails userTest1 =
  //        User.withUsername("test1")
  //            .password(passwordEncoder.encode("111"))
  //            .roles("ADMIN")
  //            .build();
  //    UserDetails userTest2 =
  //        User.withUsername("user")
  //            .password(passwordEncoder.encode("user"))
  //            .roles("ADMIN", "USER")
  //            .build();
  //
  //    return new InMemoryUserDetailsManager(userTest1, userTest2);
  //  }
}
