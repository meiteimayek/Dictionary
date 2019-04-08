package me.gyanendrokh.meiteimayek.dictionary.api.model;

import com.google.gson.annotations.SerializedName;

public class Word {

  @SerializedName("id")
  private int mId;
  @SerializedName("word")
  private String mWord;
  @SerializedName("description")
  private String mDesc;
  @SerializedName("read_as")
  private String mReadAs;

  public int getWordId() {
    return mId;
  }

  public void setWordId(int id) {
    mId = id;
  }

  public String getWord() {
    return mWord;
  }

  public void setWord(String word) {
    mWord = word;
  }

  public String getDesc() {
    return mDesc;
  }

  public void setDesc(String description) {
    mDesc = description;
  }

  public String getReadAs() {
    return mReadAs;
  }

  public void setReadAs(String readAs) {
    mReadAs = readAs;
  }

}
