package me.gyanendrokh.meiteimayek.dictionary.util;

import me.gyanendrokh.meiteimayek.dictionary.api.model.Word;
import me.gyanendrokh.meiteimayek.dictionary.database.WordEntity;

public class WordMapper {

  public static WordEntity from(Word w, String lang, Boolean isFav) {
    WordEntity e = new WordEntity();

    e.setWordId(w.getWordId());
    e.setWord(w.getWord());
    e.setDesc(w.getDesc());
    e.setReadAs(w.getReadAs());
    e.setLang(lang);
    e.setIsFav(isFav);

    return e;
  }

}
