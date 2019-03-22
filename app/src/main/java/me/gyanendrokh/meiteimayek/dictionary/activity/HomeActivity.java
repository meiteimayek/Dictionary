package me.gyanendrokh.meiteimayek.dictionary.activity;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lapism.searchview.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.PopupMenu;
import android.widget.Toast;

import me.gyanendrokh.meiteimayek.dictionary.R;
import me.gyanendrokh.meiteimayek.dictionary.adapter.HomePagerAdapter;

public class HomeActivity extends AppCompatActivity {

  private PopupMenu mMenu;

  private SearchView mSearchView;
  private TabLayout mTabs;
  private ViewPager mViewPager;
  private FloatingActionButton mFab;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    setSupportActionBar(findViewById(R.id.toolbar));

    initViews();
  }

  private void initViews() {
    mSearchView = findViewById(R.id.searchView);
    mTabs = findViewById(R.id.tabs);
    mViewPager = findViewById(R.id.container);
    mFab = findViewById(R.id.fab);

    setUpViews();
  }

  public void setUpViews() {
    setUpMenu();
    setUpSearchView();
    setUpTabs();

    mFab.setOnClickListener(v -> Toast.makeText(this, "Clicked!", Toast.LENGTH_SHORT).show());
  }

  private void setUpMenu() {
    mMenu = new PopupMenu(this,
      mSearchView.findViewById(com.lapism.searchview.R.id.search_imageView_menu));
    mMenu.getMenuInflater().inflate(R.menu.menu_main, mMenu.getMenu());
    mMenu.setOnMenuItemClickListener(item -> {
      if(!item.isChecked()) {
        item.setChecked(true);
        return true;
      }
      return false;
    });
  }

  private void setUpSearchView() {
    mSearchView.setOnMenuClickListener(() -> mMenu.show());
  }

  private void setUpTabs() {
    mViewPager.setAdapter(new HomePagerAdapter(getSupportFragmentManager()));

    mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabs));
    mTabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
  }

}
