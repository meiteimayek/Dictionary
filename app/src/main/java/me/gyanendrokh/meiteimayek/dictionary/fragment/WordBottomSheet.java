package me.gyanendrokh.meiteimayek.dictionary.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import java.util.Objects;

import me.gyanendrokh.meiteimayek.dictionary.R;
import me.gyanendrokh.meiteimayek.dictionary.util.Language;
import me.gyanendrokh.meiteimayek.dictionary.viewmodel.BottomSheetViewModel;

public class WordBottomSheet extends BottomSheetDialogFragment {

  public static WordBottomSheet getInstance() {
    return new WordBottomSheet();
  }
  
  private BottomSheetViewModel mModel;
  
  private TextView mWord;
  private TextView mLang;
  private TextView mDesc;
  private TextView mReadAs;
  
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    mModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(BottomSheetViewModel.class);
  }
  
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
    
    mWord = v.findViewById(R.id.word_desc_word);
    mLang = v.findViewById(R.id.word_desc_lang);
    mDesc = v.findViewById(R.id.word_desc_desc);
    mReadAs = v.findViewById(R.id.word_desc_read_as);
    
    mModel.getEntity().observe(this, e -> {
      if(e == null) return;
      
      mWord.setText(e.getWord());
      mLang.setText(Language.getLang(e.getLang()));
      mDesc.setText(e.getDesc());
      mReadAs.setText(e.getReadAs());
    });
    
    return v;
  }

}
