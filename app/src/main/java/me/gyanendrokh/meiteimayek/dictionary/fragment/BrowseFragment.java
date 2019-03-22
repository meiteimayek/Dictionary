package me.gyanendrokh.meiteimayek.dictionary.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.gyanendrokh.meiteimayek.dictionary.R;

public class BrowseFragment extends Fragment {

  public static BrowseFragment newInstance() {
    return new BrowseFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_browse, container, false);
  }

}
