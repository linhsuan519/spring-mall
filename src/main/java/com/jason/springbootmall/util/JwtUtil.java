package com.jason.springbootmall.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;

public final class JwtUtil {

  private static final String SECRET_KEY = "mysecretkeymysecretkeymysecretkey";
  public static final long TOKEN_VALIDITY_MILLISECONDS = 15 * 60 * 1000L;
  public static final long TOKEN_VALIDITY_SECONDS = TOKEN_VALIDITY_MILLISECONDS / 1000L;

  private static final SecretKey KEY =
      Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

  private JwtUtil() {}

  public static String generateToken(String username) {
    Date now = new Date();
    Date expiration = new Date(now.getTime() + TOKEN_VALIDITY_MILLISECONDS);

    return Jwts.builder()
        .subject(username)
        .issuedAt(now)
        .expiration(expiration)
        .signWith(KEY, Jwts.SIG.HS256)
        .compact();
  }

  public static String getUsername(String token) {
    return getClaims(token).getSubject();
  }

  public static Date getExpiration(String token) {
    return getClaims(token).getExpiration();
  }

  private static Claims getClaims(String token) {
    return Jwts.parser().verifyWith(KEY).build().parseSignedClaims(token).getPayload();
  }
}
