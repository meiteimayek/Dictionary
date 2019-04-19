package me.gyanendrokh.meiteimayek.dictionary.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;
import io.reactivex.Observable;
import me.gyanendrokh.meiteimayek.dictionary.database.WordDao;
import me.gyanendrokh.meiteimayek.dictionary.database.WordEntity;
import me.gyanendrokh.meiteimayek.dictionary.repository.SearchRepository;

public class SearchViewModel extends AndroidViewModel {
  
  private SearchRepository mRepo;
  
  private MutableLiveData<Observable<PagedList<WordEntity>>> mList;
  
  public SearchViewModel(@NonNull Application app) {
    super(app);
    mRepo = SearchRepository.getInstance(app);
    mList = new MutableLiveData<>();
    init();
  }
  
  public String getLang() {
    return mRepo.getLang();
  }
  
  public void setLang(String lang) {
    mRepo.setLang(lang);
  }
  
  public String getQuery() {
    return mRepo.getQuery();
  }
  
  public void setQuery(String query) {
    mRepo.setQuery(query);
  }
  
  public void init() {
    mRepo.init();
    mList.setValue(mRepo.getWordList());
  }
  
  public WordDao getDao() {
    return mRepo.getDao();
  }
  
  public LiveData<Observable<PagedList<WordEntity>>> getWordList() {
    return mList;
  }
  
}
