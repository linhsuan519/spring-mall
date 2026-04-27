package com.jason.springbootmall.security;

import com.jason.springbootmall.model.User;
import com.jason.springbootmall.service.UserService;
import com.jason.springbootmall.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  @Autowired private UserService userService;
  @Autowired private JwtUtil jwtUtil;

  @Value("${app.oauth2.redirect-uri}")
  private String redirectUri;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException {

    OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

    String email = oauth2User.getAttribute("email");
    String providerUserId = oauth2User.getAttribute("sub"); // Google 的用戶唯一 ID
    String provider = "google";

    Integer userId = userService.findOrCreateOauthUser(email, provider, providerUserId);
    User user = userService.getUserByEmail(email);

    String token = jwtUtil.generateToken(userId, email, user.getRole());

    String targetUrl =
        UriComponentsBuilder.fromUriString(redirectUri)
            .queryParam("token", token)
            .build()
            .toUriString();

    getRedirectStrategy().sendRedirect(request, response, targetUrl);
  }
}
