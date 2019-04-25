package me.gyanendrokh.meiteimayek.dictionary.repository;

import android.app.Application;

import androidx.paging.PagedList;
import androidx.paging.RxPagedListBuilder;

import io.reactivex.Observable;
import me.gyanendrokh.meiteimayek.dictionary.database.WordDao;
import me.gyanendrokh.meiteimayek.dictionary.database.WordDatabase;
import me.gyanendrokh.meiteimayek.dictionary.database.WordEntity;

public class FavoriteRepository {
  
  private static FavoriteRepository mSelf = null;
  
  public static FavoriteRepository getInstance(Application app) {
    if(mSelf == null) {
      mSelf = new FavoriteRepository(app);
    }
    return mSelf;
  }
  
  private WordDao mDao;
  private Observable<PagedList<WordEntity>> mFavList;
  
  private FavoriteRepository(Application app) {
    mDao = WordDatabase.getInstance(app).getWordDao();
  
    PagedList.Config config = new PagedList.Config.Builder()
      .setEnablePlaceholders(false)
      .setPageSize(20)
      .build();
  
    RxPagedListBuilder<Integer, WordEntity> builder = new RxPagedListBuilder<>(mDao.getFavs(), config);
    
    mFavList = builder.buildObservable();
  }
  
  public WordDao getDao() {
    return mDao;
  }
  
  public Observable<PagedList<WordEntity>> getFavList() {
    return mFavList;
  }
  
}
