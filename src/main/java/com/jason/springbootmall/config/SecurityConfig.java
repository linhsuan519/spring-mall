package com.jason.springbootmall.config;

import com.jason.springbootmall.filter.JwtAuthenticationFilter;
import com.jason.springbootmall.security.OAuth2LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

  @Autowired private JwtAuthenticationFilter jwtAuthenticationFilter;
  @Autowired private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        // OAuth2 流程需要 session 儲存 state，登入完成後仍維持無狀態
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
        .authorizeHttpRequests(
            auth ->
                auth
                    // 註冊、登入公開
                    .requestMatchers(HttpMethod.POST, "/users/register", "/users/login")
                    .permitAll()
                    // 商品查詢公開
                    .requestMatchers(HttpMethod.GET, "/products", "/products/**")
                    .permitAll()
                    // 商品管理需要 ADMIN
                    .requestMatchers(HttpMethod.POST, "/products")
                    .hasAuthority("ROLE_ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/products/**")
                    .hasAuthority("ROLE_ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/products/**")
                    .hasAuthority("ROLE_ADMIN")
                    // 其餘需要登入
                    .anyRequest()
                    .authenticated())
        .oauth2Login(oauth2 -> oauth2.successHandler(oAuth2LoginSuccessHandler))
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
