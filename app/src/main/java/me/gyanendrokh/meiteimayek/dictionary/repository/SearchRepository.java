package me.gyanendrokh.meiteimayek.dictionary.repository;

import android.app.Application;

import androidx.paging.PagedList;
import androidx.paging.RxPagedListBuilder;
import io.reactivex.Observable;
import me.gyanendrokh.meiteimayek.dictionary.database.WordDao;
import me.gyanendrokh.meiteimayek.dictionary.database.WordDatabase;
import me.gyanendrokh.meiteimayek.dictionary.database.WordEntity;
import me.gyanendrokh.meiteimayek.dictionary.util.Language;

public class SearchRepository {
  
  private static SearchRepository mSelf = null;
  
  public static SearchRepository getInstance(Application app) {
    if(mSelf == null) mSelf = new SearchRepository(app);
    return mSelf;
  }
  
  private WordDao mDao;
  private PagedList.Config mConfig;
  private Observable<PagedList<WordEntity>> mList;
  private String mLang = Language.ENGLISH;
  private String mQuery = "";
  
  private SearchRepository(Application app) {
    mDao = WordDatabase.getInstance(app).getWordDao();
    mConfig = (new PagedList.Config.Builder())
      .setPageSize(20)
      .setInitialLoadSizeHint(20)
      .build();
  }
  
  public String getLang() {
    return mLang;
  }
  
  public void setLang(String lang) {
    mLang = lang;
  }
  
  public String getQuery() {
    return mQuery;
  }
  
  public void setQuery(String query) {
    mQuery = query;
  }
  
  public void init() {
    RxPagedListBuilder<Integer, WordEntity> listBuilder = new RxPagedListBuilder<>(
      mDao.search(mLang, String.format("%s%%", mQuery)), mConfig);
  
    mList = listBuilder.buildObservable();
  }
  
  public WordDao getDao() {
    return mDao;
  }
  
  public Observable<PagedList<WordEntity>> getWordList() {
    return mList;
  }
  
}
