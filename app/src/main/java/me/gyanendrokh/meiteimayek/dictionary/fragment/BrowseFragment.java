package me.gyanendrokh.meiteimayek.dictionary.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.gyanendrokh.meiteimayek.dictionary.R;
import me.gyanendrokh.meiteimayek.dictionary.activity.BrowseActivity;
import me.gyanendrokh.meiteimayek.dictionary.util.Language;

public class BrowseFragment extends Fragment {

  public static BrowseFragment newInstance() {
    return new BrowseFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_browse, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    view.findViewById(R.id.browse_eng).setOnClickListener(
      v -> BrowseActivity.open(getActivity(), Language.ENGLISH)
    );
    view.findViewById(R.id.browse_mmk).setOnClickListener(
      v -> BrowseActivity.open(getActivity(), Language.MEITEI_MAYEK)
    );
    view.findViewById(R.id.browse_beng).setOnClickListener(
      v -> BrowseActivity.open(getActivity(), Language.BENGALI)
    );
  }

}
