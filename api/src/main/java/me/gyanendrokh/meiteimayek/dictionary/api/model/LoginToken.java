package me.gyanendrokh.meiteimayek.dictionary.api.model;

import com.google.gson.annotations.SerializedName;

public class LoginToken {

  @SerializedName("token")
  private String mToken;

  public String getToken() {
    return mToken;
  }

  public void setToken(String token) {
    mToken = token;
  }
}
