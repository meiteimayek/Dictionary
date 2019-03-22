package me.gyanendrokh.meiteimayek.dictionary.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import me.gyanendrokh.meiteimayek.dictionary.fragment.BrowseFragment;
import me.gyanendrokh.meiteimayek.dictionary.fragment.FavoriteFragment;

public class HomePagerAdapter extends FragmentPagerAdapter {

  public HomePagerAdapter(@NonNull FragmentManager fm) {
    super(fm);
  }

  @Override
  public int getCount() {
    return 2;
  }

  @NonNull
  @Override
  public Fragment getItem(int position) {
    if(position == 0) {
      return FavoriteFragment.newInstance();
    }else {
      return BrowseFragment.newInstance();
    }
  }

}
