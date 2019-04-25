package me.gyanendrokh.meiteimayek.dictionary.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.paging.PagedList;

import io.reactivex.Observable;
import me.gyanendrokh.meiteimayek.dictionary.database.WordDao;
import me.gyanendrokh.meiteimayek.dictionary.database.WordEntity;
import me.gyanendrokh.meiteimayek.dictionary.repository.FavoriteRepository;

public class FavoriteViewModel extends AndroidViewModel {
  
  private FavoriteRepository mRepo;
  
  public FavoriteViewModel(@NonNull Application app) {
    super(app);
    
    mRepo = FavoriteRepository.getInstance(app);
  }
  
  public WordDao getDao() {
    return mRepo.getDao();
  }
  
  public Observable<PagedList<WordEntity>> getFavList() {
    return mRepo.getFavList();
  }
  
}
