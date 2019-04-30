package me.gyanendrokh.meiteimayek.dictionary.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import me.gyanendrokh.meiteimayek.dictionary.R;
import me.gyanendrokh.meiteimayek.dictionary.adapter.WordPagedAdapter;
import me.gyanendrokh.meiteimayek.dictionary.database.WordEntity;
import me.gyanendrokh.meiteimayek.dictionary.fragment.BrowseSelectPopup;
import me.gyanendrokh.meiteimayek.dictionary.fragment.WordBottomSheet;
import me.gyanendrokh.meiteimayek.dictionary.util.Language;
import me.gyanendrokh.meiteimayek.dictionary.viewmodel.BottomSheetViewModel;
import me.gyanendrokh.meiteimayek.dictionary.viewmodel.BrowseViewModel;

public class BrowseActivity extends AppCompatActivity {
  
  public static final String LANG = "lang";
  private static final String TAG = BrowseActivity.class.getSimpleName();
  
  public static void open(Context context, String lang) {
    Intent intent = new Intent(context, BrowseActivity.class);
    intent.putExtra(LANG, lang);
    context.startActivity(intent);
  }
  
  private TextView mSelectedLetter;
  private AppCompatImageButton mSelectLetterBtn;
  private RecyclerView mListView;
  private TextView mNoResult;
  private ProgressBar mProgress;
  
  private WordPagedAdapter mAdapter;
  private CompositeDisposable mDisposable;
  private BrowseViewModel mModel;
  private BottomSheetViewModel mBottomSheetModel;
  
  private PagedList<WordEntity> mList;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_browse);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    
    Bundle b = getIntent().getExtras();
    if(b == null) throw new RuntimeException("Invalid Parameter.");
    
    mDisposable = new CompositeDisposable();
    mModel = ViewModelProviders.of(this).get(BrowseViewModel.class);
    mModel.setLang(b.getString(LANG, Language.ENGLISH));
    mModel.init();
    
    mBottomSheetModel = ViewModelProviders.of(this).get(BottomSheetViewModel.class);
    
    if(getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setTitle(Language.getLang(mModel.getLang()));
    }
    
    initView();
  }
  
  @Override
  protected void onStart() {
    super.onStart();
    mModel.getPagedList().observe(this, l -> mDisposable.add(
      l.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext(list -> {
          mProgress.setVisibility(View.GONE);
          if(list.size() != 0) {
            mListView.setVisibility(View.VISIBLE);
            mNoResult.setVisibility(View.GONE);
          }else {
            mListView.setVisibility(View.GONE);
            mNoResult.setVisibility(View.VISIBLE);
          }
          
          mList = list;
        }).subscribe(mAdapter::submitList, Throwable::printStackTrace)));
    
    mDisposable.add(Observable.create((ObservableOnSubscribe<Integer>) emitter ->
      mListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
          super.onScrolled(recyclerView, dx, dy);
          
          View v = recyclerView.findChildViewUnder(dx, dy);
          
          if(v != null) {
            int p = recyclerView.getChildAdapterPosition(v);
            if(p != -1) emitter.onNext(p);
          }
        }
      })).debounce(150, TimeUnit.MILLISECONDS)
      .subscribeOn(Schedulers.io())
      .map(p -> mList.get(p))
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(e -> {
        String letter = String.valueOf(String.valueOf(e.getWord().charAt(0)).toUpperCase().charAt(0));
        
        Log.d(TAG, "onScrolled: Selecting " + letter);
        mSelectedLetter.setText(letter);
      }));
    
    mModel.getSelectedLetter().observe(this, s -> {
      if(s == null) return;
      Log.d(TAG, "onLetterSelected: " + s);
      
      mDisposable.add(Observable.just(mModel.getDao())
        .map(d -> d.getStart(mModel.getLang(), String.format("%s%%", s)))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .map(e -> {
          LinearLayoutManager layoutManager = (LinearLayoutManager) mListView.getLayoutManager();
          
          if(layoutManager == null) return -1;
          
          int cnt = layoutManager.getChildCount() - 1;
          return e.getWordId() + cnt;
        }).subscribe(mListView::scrollToPosition, Throwable::printStackTrace));
    });
  }
  
  private void initView() {
    mSelectedLetter = findViewById(R.id.selected_letter);
    mSelectLetterBtn = findViewById(R.id.select_letter_btn);
    mListView = findViewById(R.id.browse_list);
    mNoResult = findViewById(R.id.browse_no_result);
    mProgress = findViewById(R.id.browse_progress);
    
    setUpPagedListView();
    setUpDialog();
  }
  
  private void setUpDialog() {
    mSelectLetterBtn.setOnClickListener(v -> {
      BrowseSelectPopup dialog = new BrowseSelectPopup();
      dialog.show(getSupportFragmentManager(), this.getClass().getSimpleName());
    });
  }
  
  private void setUpPagedListView() {
    mAdapter = new WordPagedAdapter();
    mAdapter.setBtnIcon(e -> {
      if(e.getIsFav()) return getDrawable(R.drawable.ic_favorite_red_24dp);
      return getDrawable(R.drawable.ic_favorite_border_red_24dp);
    });
    
    mAdapter.setOnClickListener((v, e) -> {
      mBottomSheetModel.setEntity(e);
      WordBottomSheet.getInstance().show(getSupportFragmentManager(), TAG);
    });
    
    mAdapter.setBtnClickListener((v, e) -> Observable.just(mModel.getDao())
      .map(d -> {
        e.setIsFav(!e.getIsFav());
        d.update(e);
        return e;
      }).subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .doOnNext(w -> {
        String s;
        if(w.getIsFav()) {
          s = String.format("'%s' added to favorite.", w.getWord());
          v.setBtnIcon(getDrawable(R.drawable.ic_favorite_red_24dp));
        }else {
          s = String.format("'%s' removed from favorite.", w.getWord());
          v.setBtnIcon(getDrawable(R.drawable.ic_favorite_border_red_24dp));
        }
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
      }).subscribe());
    
    LinearLayoutManager manager = new LinearLayoutManager(this);
    mListView.setLayoutManager(manager);
    mListView.setAdapter(mAdapter);
  }
  
  @Override
  protected void onStop() {
    super.onStop();
    mDisposable.clear();
  }
  
}
