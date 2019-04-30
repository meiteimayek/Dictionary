package me.gyanendrokh.meiteimayek.dictionary.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;
import io.reactivex.Observable;
import me.gyanendrokh.meiteimayek.dictionary.database.WordDao;
import me.gyanendrokh.meiteimayek.dictionary.database.WordEntity;
import me.gyanendrokh.meiteimayek.dictionary.repository.WordRepository;

public class BrowseViewModel extends AndroidViewModel {
  
  private final WordRepository mRepo;
  private final MutableLiveData<Observable<PagedList<WordEntity>>> mPagedList;
  private final MutableLiveData<String> mSelectedLetter;

  public BrowseViewModel(Application app) {
    super(app);
    mRepo = WordRepository.getInstance(app);
    mPagedList = new MutableLiveData<>();
    mSelectedLetter = new MutableLiveData<>();
    
    init();
  }
  
  public String getLang() {
    return mRepo.getLang();
  }
  
  public void setLang(String lang) {
    mRepo.setLang(lang);
  }
  
  public LiveData<String> getSelectedLetter() {
    return mSelectedLetter;
  }
  
  public void setSelectedLetter(String letter) {
    mSelectedLetter.setValue(letter);
  }
  
  public void init() {
    mRepo.init();
    
    mPagedList.setValue(mRepo.getPagedList());
  }
  
  public LiveData<Observable<PagedList<WordEntity>>> getPagedList() {
    return mPagedList;
  }

  public WordDao getDao() {
    return mRepo.getDao();
  }
  
}
