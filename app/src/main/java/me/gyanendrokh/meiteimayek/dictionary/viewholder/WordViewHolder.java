package me.gyanendrokh.meiteimayek.dictionary.viewholder;

import android.graphics.drawable.Drawable;
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
  private AppCompatImageButton mImgBtn;

  public WordViewHolder(@NonNull View itemView) {
    super(itemView);

    mRoot = itemView;
    mTitle = itemView.findViewById(R.id.browse_list_item_title);
    mImgBtn = itemView.findViewById(R.id.browse_list_item_fav);
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
    mImgBtn.setOnClickListener(l);
    return this;
  }

  public WordViewHolder setBtnIcon(Drawable icon) {
    mImgBtn.setImageDrawable(icon);
    return this;
  }

  public void bindTo(WordEntity e, View.OnClickListener onClickListener, View.OnClickListener onBtnClickListener) {
    setTitle(e.getWord());
    setOnClickListener(onClickListener);
    setBtnClickListener(onBtnClickListener);
  }

}
