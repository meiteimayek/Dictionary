package me.gyanendrokh.meiteimayek.dictionary.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import me.gyanendrokh.meiteimayek.dictionary.R;
import me.gyanendrokh.meiteimayek.dictionary.adapter.WordPagedAdapter;
import me.gyanendrokh.meiteimayek.dictionary.viewmodel.FavoriteViewModel;

public class FavoriteFragment extends Fragment {
  
  public static final String TAG = FavoriteFragment.class.getSimpleName();
  
  public static FavoriteFragment newInstance() {
    return new FavoriteFragment();
  }
  
  private FavoriteViewModel mViewModel;
  private CompositeDisposable mDisposable;
  private WordPagedAdapter mAdapter;
  
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mViewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);
    mDisposable = new CompositeDisposable();
    mAdapter = new WordPagedAdapter();
    
    mAdapter.setBtnIcon(e -> Objects.requireNonNull(getActivity()).getDrawable(R.drawable.ic_delete_forever_24dp));
    mAdapter.setOnClickListener((v, p) -> Log.d(TAG, "onClick: " + p));
    mAdapter.setBtnClickListener((v, e) -> {
      e.setIsFav(false);
      Observable.just(mViewModel.getDao())
        .subscribeOn(Schedulers.io())
        .map(d -> {
          d.update(e);
          return e;
        }).subscribe();
    });
  }
  
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_favorite, container, false);
    final TextView noFav = view.findViewById(R.id.fav_no_fav);
    final RecyclerView list = view.findViewById(R.id.fav_list);
  
    list.setLayoutManager(new LinearLayoutManager(view.getContext()));
    list.setAdapter(mAdapter);
  
    mDisposable.add(mViewModel.getFavList()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .doOnNext(l -> {
        if(l.size() != 0) {
          list.setVisibility(View.VISIBLE);
          noFav.setVisibility(View.GONE);
        }else {
          list.setVisibility(View.GONE);
          noFav.setVisibility(View.VISIBLE);
        }
      }).subscribe(mAdapter::submitList, Throwable::printStackTrace));
    return view;
  }
  
  @Override
  public void onDestroy() {
    super.onDestroy();
    mDisposable.clear();
  }
  
}
