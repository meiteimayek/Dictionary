package me.gyanendrokh.meiteimayek.dictionary.api.model;

import com.google.gson.annotations.SerializedName;

public class Count {

  @SerializedName("count")
  private int mCount;

  public int getCount() {
    return mCount;
  }

  public void setCount(int count) {
    mCount = count;
  }

}
