package me.gyanendrokh.meiteimayek.dictionary.repository;

import android.app.Application;

import androidx.paging.PagedList;
import androidx.paging.RxPagedListBuilder;

import io.reactivex.Observable;

import me.gyanendrokh.meiteimayek.dictionary.database.WordDao;
import me.gyanendrokh.meiteimayek.dictionary.database.WordDatabase;
import me.gyanendrokh.meiteimayek.dictionary.database.WordEntity;
import me.gyanendrokh.meiteimayek.dictionary.util.Language;

public class WordRepository {
  
  private static WordRepository mSelf;

  public static WordRepository getInstance(Application app) {
    if(mSelf == null) {
      mSelf = new WordRepository(app);
    }

    return mSelf;
  }
  
  private WordDao mDao;
  private PagedList.Config mConfig;
  
  private String mLang = Language.ENGLISH;
  
  private Observable<PagedList<WordEntity>> mPagedList;

  private WordRepository(Application app) {
    mDao = WordDatabase.getInstance(app).getWordDao();

    mConfig = (new PagedList.Config.Builder())
      .setPageSize(30)
      .setInitialLoadSizeHint(30)
      .setEnablePlaceholders(true)
      .build();
  }
  
  public void init() {
    RxPagedListBuilder<Integer, WordEntity> listBuilder = new RxPagedListBuilder<>(
      mDao.getAllPaged(mLang), mConfig);
    
    mPagedList = listBuilder.buildObservable();
  }

  public String getLang() {
    return mLang;
  }
  
  public void setLang(String lang) {
    mLang = lang;
  }
  
  public Observable<PagedList<WordEntity>> getPagedList() {
    return mPagedList;
  }

  public WordDao getDao() {
    return mDao;
  }
  
}
