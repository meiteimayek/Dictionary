package me.gyanendrokh.meiteimayek.dictionary.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import me.gyanendrokh.meiteimayek.dictionary.database.WordEntity;

public class BottomSheetViewModel extends ViewModel {
  
  private MutableLiveData<WordEntity> mEntity = new MutableLiveData<>();
  
  public void setEntity(WordEntity e) {
    mEntity.setValue(e);
  }
  
  public LiveData<WordEntity> getEntity() {
    return mEntity;
  }
  
}
