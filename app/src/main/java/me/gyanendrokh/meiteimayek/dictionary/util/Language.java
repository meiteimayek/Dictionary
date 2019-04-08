package me.gyanendrokh.meiteimayek.dictionary.util;

public class Language {

  public static final String MEITEI_MAYEK = "mmk";
  public static final String ENGLISH = "eng";
  public static final String BENGALI = "beng";

  public static String[] getLangs() {
    return new String[]{
      "English", "Meitei Mayek", "Bengali"
    };
  }

  public static String getLang(String code) {
    String[] langs = getLangs();
    switch(code) {
      case ENGLISH:
        return langs[0];
      case MEITEI_MAYEK:
        return langs[1];
      case BENGALI:
        return langs[2];
      default:
        return null;
    }
  }

}
