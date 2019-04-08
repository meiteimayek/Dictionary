package me.gyanendrokh.meiteimayek.dictionary.util;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.gyanendrokh.meiteimayek.dictionary.api.SelectApi;
import me.gyanendrokh.meiteimayek.dictionary.api.model.Count;
import me.gyanendrokh.meiteimayek.dictionary.api.model.Word;
import me.gyanendrokh.meiteimayek.dictionary.api.service.SelectService;
import me.gyanendrokh.meiteimayek.dictionary.data.WordMapper;
import me.gyanendrokh.meiteimayek.dictionary.database.WordDao;
import me.gyanendrokh.meiteimayek.dictionary.database.WordDatabase;
import me.gyanendrokh.meiteimayek.dictionary.database.WordEntity;

public class Downloader {

  private static final String TAG = Downloader.class.getSimpleName();

  private int mStart = 1;
  private final int mSize = 1000;
  private int mReturnedSize = -1;
  private int mCount = 0;

  private CompositeDisposable mDisposable;
  private WordDao mDao;
  private SelectApi mApi;

  public Downloader(Context context, CompositeDisposable disposable) {
    mDisposable = disposable;
    mApi = SelectService.getInstance().getApi();
    mDao = WordDatabase.getInstance(context).getWordDao();
  }

  public Single<Boolean> needDownload() {
    return Observable.fromArray(Language.ENGLISH, Language.MEITEI_MAYEK, Language.BENGALI)
      .concatMap((Function<String, ObservableSource<Boolean>>) s -> mApi.count(s)
        .map(count -> count.getCount() == mDao.count(s))).filter(b -> !b)
      .firstOrError().subscribeOn(Schedulers.io());
  }

  public Observable<DownloadStatus> getDownloader() {
    return Observable.fromArray(Language.ENGLISH, Language.MEITEI_MAYEK, Language.BENGALI)
      .concatMap((Function<String, ObservableSource<List<WordEntity>>>) lang -> {
        Log.d(TAG, "Downloading : Lang - " + lang);
        return downloadAllWords(lang);
      }).map(l -> new DownloadStatus() {
        @Override
        public int getMax() {
          return mCount;
        }

        @Override
        public int getProgress() {
          return l.get(l.size() - 1).getWordId();
        }
      });
  }

  private Observable<List<WordEntity>> downloadAllWords(String lang) {
    mStart = 1;
    mReturnedSize = -1;

    return mApi.count(lang)
      .concatMap((Function<Count, ObservableSource<List<WordEntity>>>) count -> {
        setCount(count.getCount());
        int c = mDao.count(lang);
        if(count.getCount() == c) return Observable.fromIterable(new ArrayList<>());
        setStart(c + 1);

        Log.d(TAG, "downloadAllWords: Start - " + mStart);
        return Observable.create(emitter -> {
          try {
            downloadWords(lang, emitter);
          }catch(Exception e) {
            emitter.onError(e);
          }
        });
      });
  }

  private void downloadWords(String lang, ObservableEmitter<List<WordEntity>> emitter)
    throws RuntimeException {
    mDisposable.add(mApi.getPositionalWords(lang, mStart, mSize).flatMap(
      (Function<List<Word>, ObservableSource<WordEntity>>) words -> {
        setReturnedSize(words.size());
        Log.d(TAG, "downloadWords: Returned Size - " + mReturnedSize);
        return Observable.fromIterable(words)
          .map(word -> WordMapper.from(word, lang, false));
      }).buffer(mSize / 2).map(l -> {
        mDao.insert((WordEntity[]) l.toArray());
        Log.d(TAG, String.format("downloadWords: Downloaded from %d to %d.",
          l.get(0).getWordId(), l.get(l.size() - 1).getWordId()));
        return l;
      }).subscribeOn(Schedulers.io())
      .subscribe(emitter::onNext, emitter::onError, () -> {
        if(mReturnedSize != mSize) emitter.onComplete();
        mStart += mReturnedSize;

        downloadWords(lang, emitter);
      }));
  }

  private void setCount(int count) {
    mCount = count;
  }

  private void setStart(int start) {
    mStart = start;
  }

  private void setReturnedSize(int size) {
    mReturnedSize = size;
  }

  public interface DownloadStatus {
    int getMax();

    int getProgress();
  }

}
