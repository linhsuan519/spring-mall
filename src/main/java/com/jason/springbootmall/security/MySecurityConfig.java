package com.jason.springbootmall.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class MySecurityConfig {

  private static final Set<String> PUBLIC_PATHS =
      Set.of("/users/register", "/users/login", "/error", "/swagger-ui.html");
  private static final Pattern PRODUCTS_PATH = Pattern.compile("^/products(?:/\\d+)?$");
  private static final Pattern SWAGGER_PATH =
      Pattern.compile("^/(?:swagger-ui(?:/.*)?|v3/api-docs(?:\\.yaml|/.*)?)$");

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) {
    http.sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .cors(Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(MySecurityConfig::isPublicEndpoint)
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .exceptionHandling(
            exception ->
                exception.authenticationEntryPoint(
                    (request, response, authException) ->
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")))
        .httpBasic(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .logout(AbstractHttpConfigurer::disable)
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  static boolean isPublicEndpoint(HttpServletRequest request) {
    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
      return true;
    }

    String path = getPathWithoutContextPath(request);

    return PUBLIC_PATHS.contains(path)
        || SWAGGER_PATH.matcher(path).matches()
        || isPublicProductRead(request.getMethod(), path);
  }

  private static boolean isPublicProductRead(String method, String path) {
    return "GET".equalsIgnoreCase(method) && PRODUCTS_PATH.matcher(path).matches();
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
