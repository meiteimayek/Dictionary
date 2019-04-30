package me.gyanendrokh.meiteimayek.dictionary.fragment;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;

import me.gyanendrokh.meiteimayek.dictionary.util.Language;
import me.gyanendrokh.meiteimayek.dictionary.viewmodel.BrowseViewModel;

public class BrowseSelectPopup extends AppCompatDialogFragment {
  
  private BrowseViewModel mModel;
  
  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    if(getActivity() == null) throw new IllegalStateException();
    
    mModel = ViewModelProviders.of(getActivity()).get(BrowseViewModel.class);
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
  
    List<CharSequence> letters = new ArrayList<>();
    
    if(mModel.getLang().equals(Language.ENGLISH)) {
      for(char i = 'A'; i <= 'Z'; i++) letters.add(String.valueOf(i));
    }else if(mModel.getLang().equals(Language.BENGALI)) {
      String[] bengCodes = ("2437, 2438, 2439, 2440, 2441, 2442, 2443, 2447, 2448, 2451, 2452, 2453, 2454, 2455,"
        + "2456, 2457, 2458, 2459, 2460, 2461, 2462, 2463, 2464, 2465, 2466, 2468, 2469, 2470, 2471, 2472,"
        + "2474, 2475, 2476, 2477, 2478, 2479, 2480, 2482, 2486, 2487, 2488, 2489, 2527, 2545").split(",");
      for(String c : bengCodes) {
        letters.add(String.valueOf((char)Integer.valueOf(c.trim()).intValue()));
      }
    }
    
    builder.setItems(letters.toArray(new CharSequence[]{}), (dialog, which) ->
      mModel.setSelectedLetter(letters.get(which).toString()));
    
    return builder.create();
  }
  
}
