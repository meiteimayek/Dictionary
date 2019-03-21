package me.gyanendrokh.meiteimayek.dictionary.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import me.gyanendrokh.meiteimayek.dictionary.R;

public class PlaceholderFragment extends Fragment {

  private static final String ARG_SECTION_NUMBER = "section_number";

  public PlaceholderFragment() {
  }

  public static Fragment newInstance(int sectionNumber) {
    PlaceholderFragment fragment = new PlaceholderFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_SECTION_NUMBER, sectionNumber);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_home, container, false);
    TextView textView = rootView.findViewById(R.id.section_label);
    if (getArguments() != null) {
      textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
    }
    return rootView;
  }

}
