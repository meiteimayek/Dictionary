package me.gyanendrokh.meiteimayek.dictionary.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lapism.searchview.Search;
import com.lapism.searchview.widget.SearchView;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import me.gyanendrokh.meiteimayek.dictionary.R;
import me.gyanendrokh.meiteimayek.dictionary.adapter.WordPagedAdapter;
import me.gyanendrokh.meiteimayek.dictionary.viewmodel.SearchViewModel;

public class SearchActivity extends AppCompatActivity {
  
  public static final String QUERY = "query";
  public static final String LANG = "lang";
  
  public static void open(Context context, String lang, String query) {
    Intent i = new Intent(context, SearchActivity.class);
    i.putExtra(SearchActivity.LANG, lang);
    i.putExtra(SearchActivity.QUERY, query);
    context.startActivity(i);
  }
  
  private CompositeDisposable mDisposable;
  private SearchViewModel mModel;
  private WordPagedAdapter mAdapter;
  
  private SearchView mSearchView;
  private RecyclerView mSearchList;
  private ProgressBar mSearchProgress;
  private TextView mSearchNoResult;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);
    
    Bundle extras = getIntent().getExtras();
    
    if(extras == null) throw new IllegalArgumentException();
    
    mDisposable = new CompositeDisposable();
    mModel = ViewModelProviders.of(this).get(SearchViewModel.class);
    
    mModel.setLang(extras.getString(LANG, ""));
    mModel.setQuery(extras.getString(QUERY, ""));
    
    mModel.init();
    initView();
  }
  
  @Override
  protected void onStart() {
    super.onStart();
    mModel.getWordList().observe(this, l -> mDisposable.add(l
      .doOnNext(list -> {
        mSearchProgress.setVisibility(View.GONE);
        if(list.size() == 0) {
          mSearchNoResult.setVisibility(View.VISIBLE);
          mSearchList.setVisibility(View.GONE);
        }else {
          mSearchNoResult.setVisibility(View.GONE);
          mSearchList.setVisibility(View.VISIBLE);
        }
      }).subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(mAdapter::submitList, Throwable::printStackTrace)));
  }
  
  private void initView() {
    mSearchView = findViewById(R.id.search_view);
    mSearchList = findViewById(R.id.search_list);
    mSearchProgress = findViewById(R.id.search_progress);
    mSearchNoResult = findViewById(R.id.search_no_result);
    
    setUpSearchView();
    setUpSearchList();
  }
  
  private void setUpSearchView() {
    mSearchView.setOnLogoClickListener(this::finish);
    mSearchView.setText(mModel.getQuery());
    mSearchView.setOnQueryTextListener(new Search.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(CharSequence query) {
        mModel.setQuery(query.toString());
        mModel.init();
        return true;
      }
      
      @Override
      public void onQueryTextChange(CharSequence newText) {
      
      }
    });
  }
  
  private void setUpSearchList() {
    mModel.setLang(mModel.getLang());
    mAdapter = new WordPagedAdapter();
    mAdapter.setBtnIcon(e -> {
      if(e.getIsFav()) return getDrawable(R.drawable.ic_favorite_red_24dp);
      return getDrawable(R.drawable.ic_favorite_border_red_24dp);
    });
    
    mAdapter.setOnClickListener((v, e) -> Toast.makeText(this, e.getWord(), Toast.LENGTH_SHORT).show());
    
    mAdapter.setBtnClickListener((v, e) -> {
      if(e == null) return;
      Observable.just(mModel.getDao())
        .map(d -> {
          e.setIsFav(!e.getIsFav());
          d.update(e);
          return e;
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext(w -> {
          String s;
          if(w.getIsFav()) {
            v.setBtnIcon(getDrawable(R.drawable.ic_favorite_red_24dp));
            s = String.format("'%s' added to favorite.", w.getWord());
          }else {
            v.setBtnIcon(getDrawable(R.drawable.ic_favorite_border_red_24dp));
            s = String.format("'%s' removed from favorite.", w.getWord());
          }
  
          Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        }).subscribe();
    });
    
    mSearchList.setLayoutManager(new LinearLayoutManager(this));
    mSearchList.setAdapter(mAdapter);
  }
  
  @Override
  protected void onStop() {
    super.onStop();
    mDisposable.clear();
  }
  
}
