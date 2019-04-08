package me.gyanendrokh.meiteimayek.dictionary.api.model;

import com.google.gson.annotations.SerializedName;

public class UserCredential {

  @SerializedName("username")
  private String mUsername;
  @SerializedName("password")
  private String mPassword;

  public String getUsername() {
    return mUsername;
  }

  public void setUsername(String username) {
    mUsername = username;
  }

  public String getPassword() {
    return mPassword;
  }

  public void setPassword(String password) {
    mPassword = password;
  }

}
