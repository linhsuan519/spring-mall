package com.jason.springbootmall.model;

public class UserOauth {

  private Integer oauthId;
  private Integer userId;
  private String provider;
  private String providerUserId;

  public Integer getOauthId() {
    return oauthId;
  }

  public void setOauthId(Integer oauthId) {
    this.oauthId = oauthId;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public String getProvider() {
    return provider;
  }

  public void setProvider(String provider) {
    this.provider = provider;
  }

  public String getProviderUserId() {
    return providerUserId;
  }

  public void setProviderUserId(String providerUserId) {
    this.providerUserId = providerUserId;
  }
}
