package me.gyanendrokh.meiteimayek.dictionary.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import me.gyanendrokh.meiteimayek.dictionary.R;

public class AboutMeActivity extends AppCompatActivity implements View.OnClickListener {
  
  private AppCompatButton mAuthorBtn;
  private AppCompatButton mWebsiteBtn;
  private AppCompatButton mGithubBtn;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_about_me);
    
    init();
  }
  
  private void init() {
    mAuthorBtn = findViewById(R.id.about_link_author);
    mWebsiteBtn = findViewById(R.id.about_link_website);
    mGithubBtn = findViewById(R.id.about_link_github);
    
    handleClick();
  }
  
  private void handleClick() {
    mAuthorBtn.setOnClickListener(this);
    mWebsiteBtn.setOnClickListener(this);
    mGithubBtn.setOnClickListener(this);
  }
  
  @Override
  public void onClick(View v) {
    int id = v.getId();
    
    Intent link = new Intent(Intent.ACTION_VIEW);
    
    switch(id) {
      case R.id.about_link_author:
        link.setData(Uri.parse(getString(R.string.author_link)));
        break;
      case R.id.about_link_website:
        link.setData(Uri.parse(getString(R.string.website_linK)));
        break;
      case R.id.about_link_github:
        link.setData(Uri.parse(getString(R.string.github_link)));
        break;
      default:
        return;
    }
    
    if(link.resolveActivity(getPackageManager()) == null) return;
    startActivity(link);
  }
  
}
