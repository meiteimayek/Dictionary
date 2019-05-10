package me.gyanendrokh.meiteimayek.dictionary.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import me.gyanendrokh.meiteimayek.dictionary.R;
import me.gyanendrokh.meiteimayek.dictionary.database.WordEntity;
import me.gyanendrokh.meiteimayek.dictionary.viewholder.Placeholder;
import me.gyanendrokh.meiteimayek.dictionary.viewholder.WordViewHolder;

public class WordPagedAdapter extends PagedListAdapter<WordEntity, RecyclerView.ViewHolder> {
  
  protected static final int WORD_VIEW_HOLDER = 1;
  protected static final int PLACE_HOLDER = 0;
  
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
  
  @Override
  public int getItemViewType(int position) {
    WordEntity e = getItem(position);
    
    if(e == null) return PLACE_HOLDER;
    return WORD_VIEW_HOLDER;
  }
  
  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    
    if(viewType == PLACE_HOLDER) return new Placeholder(inflater.inflate(R.layout.word_placeholder, parent, false));
    else return new WordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.word_list_item, parent, false));
  }
  
  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    WordEntity e = getItem(position);
    
    if(e != null) {
      if(mIcon != null) {
        ((WordViewHolder)holder).setBtnIcon(mIcon.getIcon(e));
      }
  
      ((WordViewHolder)holder).bindTo(
        e,
        v -> {
          if(mClickListener != null) mClickListener.onClick(((WordViewHolder)holder), e);
        },
        v -> {
          if(mBtnClickListener != null) mBtnClickListener.onClick(((WordViewHolder)holder), e);
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
