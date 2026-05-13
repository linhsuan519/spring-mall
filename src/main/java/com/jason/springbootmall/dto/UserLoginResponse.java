package com.jason.springbootmall.dto;

import com.jason.springbootmall.model.User;
import java.util.Date;

public class UserLoginResponse {
  private Integer userId;
  private String email;
  private String role;
  private Date createdDate;
  private Date lastModifiedDate;
  private String token;
  private Date loginTime;
  private Date expiresAt;
  private long expiresInSeconds;

  public UserLoginResponse(
      User user, String token, Date loginTime, Date expiresAt, long expiresInSeconds) {
    this.userId = user.getUserId();
    this.email = user.getEmail();
    this.role = user.getRole();
    this.createdDate = user.getCreatedDate();
    this.lastModifiedDate = user.getLastModifiedDate();
    this.token = token;
    this.loginTime = loginTime;
    this.expiresAt = expiresAt;
    this.expiresInSeconds = expiresInSeconds;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public Date getLastModifiedDate() {
    return lastModifiedDate;
  }

  public void setLastModifiedDate(Date lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Date getLoginTime() {
    return loginTime;
  }

  public void setLoginTime(Date loginTime) {
    this.loginTime = loginTime;
  }

  public Date getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(Date expiresAt) {
    this.expiresAt = expiresAt;
  }

  public long getExpiresInSeconds() {
    return expiresInSeconds;
  }

  public void setExpiresInSeconds(long expiresInSeconds) {
    this.expiresInSeconds = expiresInSeconds;
  }
}
