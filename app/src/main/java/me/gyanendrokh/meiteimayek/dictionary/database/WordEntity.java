package me.gyanendrokh.meiteimayek.dictionary.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "word")
public class WordEntity {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id")
  private int id;

  @ColumnInfo(name = "word_id")
  private int wordId;

  @ColumnInfo(name = "word")
  private String word;

  @ColumnInfo(name = "lang")
  private String lang;

  @ColumnInfo(name = "description")
  private String desc;

  @ColumnInfo(name = "read_as")
  private String readAs;

  @ColumnInfo(name = "is_fav")
  private Boolean isFav;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getWordId() {
    return wordId;
  }

  public void setWordId(int wordId) {
    this.wordId = wordId;
  }

  public String getWord() {
    return word;
  }

  public void setWord(String word) {
    this.word = word;
  }

  public String getLang() {
    return lang;
  }

  public void setLang(String lang) {
    this.lang = lang;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public String getReadAs() {
    return readAs;
  }

  public void setReadAs(String readAs) {
    this.readAs = readAs;
  }

  public Boolean getIsFav() {
    return isFav;
  }

  public void setIsFav(Boolean isFav) {
    this.isFav = isFav;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if(obj instanceof WordEntity) {
      WordEntity e = (WordEntity) obj;

      return e.toString().equals(toString());
    }
    return false;
  }

  @NonNull
  @Override
  public String toString() {
    return "Word { " +
      "id: " + getWordId() + ", " +
      "word: " + getWord() + ", " +
      "lang: " + getLang() + ", " +
      "desc: " + getDesc() + ", " +
      "read_as: " + getReadAs() + ", " +
      "isFav: " + getIsFav() +
      " }";
  }

}
