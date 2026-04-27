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
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
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

    OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
    OAuth2User oauth2User = oauthToken.getPrincipal();
    String provider = oauthToken.getAuthorizedClientRegistrationId(); // "google" or "line"

    String providerUserId;
    String email;
    String picture = null;
    String displayName = null;

    if ("google".equals(provider)) {
      providerUserId = oauth2User.getAttribute("sub");
      email = oauth2User.getAttribute("email");
      picture = oauth2User.getAttribute("picture");
      displayName = oauth2User.getAttribute("name");
    } else {
      // LINE 回傳 userId，且預設不提供 email
      providerUserId = oauth2User.getAttribute("userId");
      email = oauth2User.getAttribute("email");
      if (email == null) {
        // LINE 沒有 email 時用 placeholder，確保 user 表 email 欄位不為空
        email = "line_" + providerUserId + "@line.placeholder";
      }
      picture = oauth2User.getAttribute("pictureUrl");
      displayName = oauth2User.getAttribute("displayName");
    }

    Integer userId = userService.findOrCreateOauthUser(email, provider, providerUserId);
    User user = userService.getUserByEmail(email);

    String token = jwtUtil.generateToken(userId, email, user.getRole(), picture, displayName);

    String targetUrl =
        UriComponentsBuilder.fromUriString(redirectUri)
            .queryParam("token", token)
            .build()
            .toUriString();

    getRedirectStrategy().sendRedirect(request, response, targetUrl);
  }
}
