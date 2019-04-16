package me.gyanendrokh.meiteimayek.dictionary.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import me.gyanendrokh.meiteimayek.dictionary.R;
import me.gyanendrokh.meiteimayek.dictionary.util.Downloader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.NoSuchElementException;

public class SplashActivity extends AppCompatActivity {

  private static final String TAG = SplashActivity.class.getSimpleName();

  public static final String SHARED_PREF = "DownloadCompleted";
  public static final String SHARED_PREF_COMPLETED = "completed";

  private Downloader mDownloader;
  private CompositeDisposable mDisposable = new CompositeDisposable();

  private AppCompatTextView mTitle;
  private ProgressBar mProgress;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    mDownloader = new Downloader(this);

    mTitle = findViewById(R.id.splash_title);
    mProgress = findViewById(R.id.splash_progress);

    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);

    mDisposable.add(mDownloader.needDownload()
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(b -> {
        Log.d(TAG, "onStart: Download Starting...");
        mProgress.setIndeterminate(false);
        mTitle.setText(R.string.downloading);
        Toast.makeText(this, "This might take a while...", Toast.LENGTH_LONG).show();
        Toast.makeText(this, "...Have Patience...", Toast.LENGTH_SHORT).show();
        mDisposable.add(
          mDownloader.getDownloader()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
              s -> {
                String title = getString(R.string.downloading) + "(" + s.getLang() + ")";
                mTitle.setText(title);
                mProgress.setMax(s.getMax());
                mProgress.setProgress(s.getProgress());
              }, t -> {
                t.printStackTrace();
                Toast.makeText(SplashActivity.this, "Download Failed...", Toast.LENGTH_SHORT).show();
                finish();
              }, () -> {
                sharedPreferences.edit().putBoolean(SHARED_PREF_COMPLETED, true).apply();
                openHome();
              }
            ));
      }, t -> {
        Log.d(TAG, "onCreate: Exception - " + t.getClass().getName());
        Log.d(TAG, "onCreate: Exception - " + t.getLocalizedMessage());
        if(!sharedPreferences.getBoolean(SHARED_PREF_COMPLETED, false)) {
          if(t instanceof NoSuchElementException) {
            sharedPreferences.edit().putBoolean(SHARED_PREF_COMPLETED, true).apply();
          }else {
            t.printStackTrace();

            Toast.makeText(SplashActivity.this, "Download Failed...", Toast.LENGTH_SHORT).show();
            finish();
            return;
          }
        }

        Log.d(TAG, "onStart: Download Completed...");
        openHome();
      }));
  }

  private void openHome() {
    startActivity(new Intent(this, HomeActivity.class));
    finish();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mDisposable.clear();
  }

}
