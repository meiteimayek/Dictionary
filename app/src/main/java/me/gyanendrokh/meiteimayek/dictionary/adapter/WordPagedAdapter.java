package me.gyanendrokh.meiteimayek.dictionary.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;

import me.gyanendrokh.meiteimayek.dictionary.R;
import me.gyanendrokh.meiteimayek.dictionary.database.WordEntity;
import me.gyanendrokh.meiteimayek.dictionary.viewholder.WordViewHolder;

public class WordPagedAdapter extends PagedListAdapter<WordEntity, WordViewHolder> {
  
  @Override
  public void submitList(@Nullable PagedList<WordEntity> pagedList, @Nullable Runnable commitCallback) {
    super.submitList(pagedList, commitCallback);
  }
  
  private static DiffUtil.ItemCallback<WordEntity> DIFF_CALLBACK = new DiffUtil.ItemCallback<WordEntity>() {
    @Override
    public boolean areItemsTheSame(@NonNull WordEntity oldItem, @NonNull WordEntity newItem) {
      return oldItem.getWordId() == newItem.getWordId();
    }
    
    @Override
    public boolean areContentsTheSame(@NonNull WordEntity oldItem, @NonNull WordEntity newItem) {
      return oldItem.equals(newItem);
    }
  };
  
  private OnSetBtnIcon mIcon;
  private OnClickListener mClickListener;
  private OnClickListener mBtnClickListener;
  
  public WordPagedAdapter() {
    super(DIFF_CALLBACK);
  }
  
  @NonNull
  @Override
  public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext())
      .inflate(R.layout.word_list_item, parent, false);
    return new WordViewHolder(v);
  }
  
  @Override
  public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
    WordEntity e = getItem(position);
    
    if(e != null) {
      if(mIcon != null) {
        holder.setBtnIcon(mIcon.getIcon(e));
      }
      
      holder.bindTo(
        e,
        v -> {
          if(mClickListener != null) mClickListener.onClick(holder, e);
        },
        v -> {
          if(mBtnClickListener != null) mBtnClickListener.onClick(holder, e);
        }
      );
    }
  }
  
  public void setBtnIcon(OnSetBtnIcon icon) {
    mIcon = icon;
  }
  
  public void setOnClickListener(OnClickListener l) {
    mClickListener = l;
  }
  
  public void setBtnClickListener(OnClickListener l) {
    mBtnClickListener = l;
  }
  
  public interface OnSetBtnIcon {
    Drawable getIcon(WordEntity e);
  }
  
  public interface OnClickListener {
    void onClick(WordViewHolder holder, WordEntity entity);
  }
  
}
