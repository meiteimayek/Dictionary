package me.gyanendrokh.meiteimayek.dictionary.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import me.gyanendrokh.meiteimayek.dictionary.R;
import me.gyanendrokh.meiteimayek.dictionary.database.WordEntity;

public class WordViewHolder extends RecyclerView.ViewHolder {

  private View mRoot;
  private AppCompatTextView mTitle;
  private AppCompatImageButton mFavBtn;

  public WordViewHolder(@NonNull View itemView) {
    super(itemView);

    mRoot = itemView;
    mTitle = itemView.findViewById(R.id.browse_list_item_title);
    mFavBtn = itemView.findViewById(R.id.browse_list_item_fav);
  }

  public WordViewHolder setTitle(String title) {
    mTitle.setText(title);
    return this;
  }

  public WordViewHolder setOnClickListener(View.OnClickListener l) {
    mRoot.setOnClickListener(l);
    return this;
  }

  public WordViewHolder setBtnClickListener(View.OnClickListener l) {
    mFavBtn.setOnClickListener(l);
    return this;
  }

  public WordViewHolder setFav(boolean isFav) {
    if(isFav) {
      mFavBtn.setImageDrawable(mRoot.getContext().getDrawable(R.drawable.ic_favorite_red_24dp));
    }else {
      mFavBtn.setImageDrawable(mRoot.getContext().getDrawable(R.drawable.ic_favorite_border_red_24dp));
    }
    return this;
  }

  public void bindTo(WordEntity e, View.OnClickListener onClickListener, View.OnClickListener onBtnClickListener) {
    setTitle(e.getWord());
    setFav(e.getIsFav());
    setOnClickListener(onClickListener);
    setBtnClickListener(onBtnClickListener);
  }

}
