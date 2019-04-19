package me.gyanendrokh.meiteimayek.dictionary.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lapism.searchview.Search;
import com.lapism.searchview.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import me.gyanendrokh.meiteimayek.dictionary.R;
import me.gyanendrokh.meiteimayek.dictionary.adapter.WordPagedAdapter;
import me.gyanendrokh.meiteimayek.dictionary.database.WordEntity;
import me.gyanendrokh.meiteimayek.dictionary.viewmodel.SearchViewModel;

public class SearchActivity extends AppCompatActivity {
  
  public static final String QUERY = "query";
  public static final String LANG = "lang";
  
  private CompositeDisposable mDisposable;
  
  private SearchViewModel mModel;
  
  private SearchView mSearchView;
  private RecyclerView mSearchList;
  private ProgressBar mSearchProgress;
  private TextView mSearchNoResult;
  
  private WordPagedAdapter mAdapter;
  
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
    
    mAdapter.setOnClickListener((v, p) -> {
      WordEntity e = mAdapter.getItem(p);
      if(e == null) return;
  
      Toast.makeText(this, e.getWord(), Toast.LENGTH_SHORT).show();
    });
    
    mAdapter.setBtnClickListener((v, p) -> {
      WordEntity e = mAdapter.getItem(p);
      if(e == null) return;
      Observable.just(mModel.getDao())
        .map(d -> {
          e.setIsFav(!e.getIsFav());
          d.update(e);
          return e;
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext(w -> {
          if(w.getIsFav())
            Toast.makeText(this, String.format("'%s' added to favorite.", w.getWord()), Toast.LENGTH_SHORT).show();
          else
            Toast.makeText(this, String.format("'%s' removed from favorite.", w.getWord()), Toast.LENGTH_SHORT).show();
          v.setFav(e.getIsFav());
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
  
  public static void open(Context context, String lang, String query) {
    Intent i = new Intent(context, SearchActivity.class);
    i.putExtra(SearchActivity.LANG, lang);
    i.putExtra(SearchActivity.QUERY, query);
    context.startActivity(i);
  }
  
}
