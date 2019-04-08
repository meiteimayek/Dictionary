package me.gyanendrokh.meiteimayek.dictionary.api.model;

import com.google.gson.annotations.SerializedName;

public class User {

  @SerializedName("username")
  private String mUsername;
  @SerializedName("email")
  private String mEmail;
  @SerializedName("first_name")
  private String mFirstName;
  @SerializedName("last_name")
  private String mLastName;

  public String getUsername() {
    return mUsername;
  }

  public void setUsername(String username) {
    mUsername = username;
  }

  public String getEmail() {
    return mEmail;
  }

  public void setEmail(String email) {
    mEmail = email;
  }

  public String getFirstName() {
    return mFirstName;
  }

  public void setFirstName(String firstName) {
    mFirstName = firstName;
  }

  public String getLastName() {
    return mLastName;
  }

  public void setLastName(String lastName) {
    mLastName = lastName;
  }

}
