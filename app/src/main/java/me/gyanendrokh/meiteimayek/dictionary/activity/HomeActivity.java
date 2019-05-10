package me.gyanendrokh.meiteimayek.dictionary.activity;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lapism.searchview.Search;
import com.lapism.searchview.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.PopupMenu;
import android.widget.Toast;

import me.gyanendrokh.meiteimayek.dictionary.R;
import me.gyanendrokh.meiteimayek.dictionary.adapter.HomePagerAdapter;
import me.gyanendrokh.meiteimayek.dictionary.util.Language;

public class HomeActivity extends AppCompatActivity {

  private PopupMenu mMenu;

  private DrawerLayout mDrawer;
  private NavigationView mNavView;
  private SearchView mSearchView;
  private TabLayout mTabs;
  private ViewPager mViewPager;
  private FloatingActionButton mFab;

  private String mLang = Language.ENGLISH;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    setSupportActionBar(findViewById(R.id.toolbar));

    initViews();
  }

  private void initViews() {
    mDrawer = findViewById(R.id.drawer);
    mNavView = findViewById(R.id.nav_view);
    mSearchView = findViewById(R.id.searchView);
    mTabs = findViewById(R.id.tabs);
    mViewPager = findViewById(R.id.container);
    mFab = findViewById(R.id.fab);

    setUpViews();
  }

  private void setUpViews() {
    setUpNavDrawer();
    setUpMenu();
    setUpSearchView();
    setUpTabs();

    mFab.setOnClickListener(v -> Toast.makeText(this, "Clicked!", Toast.LENGTH_SHORT).show());
  }

  private void setUpNavDrawer() {
    mNavView.setNavigationItemSelectedListener(i -> {
      new Handler().postDelayed(
        () -> runOnUiThread(() -> mDrawer.closeDrawers()),
        350
      );
      switch(i.getItemId()) {
        case R.id.nav_home:
          return true;
        case R.id.nav_about:
          startActivity(new Intent(this, AboutMeActivity.class));
          return false;
      }
      return false;
    });
  }

  private void setUpMenu() {
    mMenu = new PopupMenu(this,
      mSearchView.findViewById(com.lapism.searchview.R.id.search_imageView_menu));
    mMenu.getMenuInflater().inflate(R.menu.menu_main, mMenu.getMenu());
    mMenu.setOnMenuItemClickListener(item -> {
      if(!item.isChecked()) {
        int id = item.getItemId();
        switch(id) {
          case R.id.menu_lang_eng:
            mLang = Language.ENGLISH;
            break;
          case R.id.menu_lang_mmk:
            mLang = Language.MEITEI_MAYEK;
            break;
          case R.id.menu_lang_beng:
            mLang = Language.BENGALI;
        }
        item.setChecked(true);
        return true;
      }
      return false;
    });
  }

  private void setUpSearchView() {
    mSearchView.setOnMenuClickListener(() -> mMenu.show());
    mSearchView.setOnLogoClickListener(() -> mDrawer.openDrawer(GravityCompat.START));
    mSearchView.setOnQueryTextListener(new Search.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(CharSequence query) {
        SearchActivity.open(HomeActivity.this, mLang, query.toString());
        mSearchView.setText("");
        mSearchView.close();
        return true;
      }

      @Override
      public void onQueryTextChange(CharSequence newText) {

      }
    });
  }

  private void setUpTabs() {
    mViewPager.setAdapter(new HomePagerAdapter(getSupportFragmentManager()));

    mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabs));
    mTabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
  }

  @Override
  public void onBackPressed() {
    if(mDrawer.isDrawerOpen(GravityCompat.START)) {
      mDrawer.closeDrawers();
    }else {
      super.onBackPressed();
    }
  }

}
