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
  private static final Pattern ACTIVITY_DETAIL_PATH = Pattern.compile("^/activities/\\d+$");

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
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

    if (PUBLIC_PATHS.contains(path)) {
      return true;
    }

    // GET /activities 活動列表、GET /activities/{id} 活動詳情 為公開
    if ("GET".equalsIgnoreCase(request.getMethod())) {
      return path.equals("/activities") || ACTIVITY_DETAIL_PATH.matcher(path).matches();
    }

    return false;
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
}
