package me.gyanendrokh.meiteimayek.dictionary.util;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.gyanendrokh.meiteimayek.dictionary.api.SelectApi;
import me.gyanendrokh.meiteimayek.dictionary.api.service.SelectService;
import me.gyanendrokh.meiteimayek.dictionary.database.WordDao;
import me.gyanendrokh.meiteimayek.dictionary.database.WordDatabase;
import me.gyanendrokh.meiteimayek.dictionary.database.WordEntity;

public class Downloader {
  
  private static final String TAG = Downloader.class.getSimpleName();
  
  private final int mBufferSize = 1000;
  private int mCount = 0;
  private String mLang;
  
  private WordDao mDao;
  private SelectApi mApi;
  
  public Downloader(Context context) {
    mApi = SelectService.getInstance().getApi();
    mDao = WordDatabase.getInstance(context).getWordDao();
  }
  
  public Single<Boolean> needDownload() {
    return Observable.fromArray(Language.ENGLISH, Language.BENGALI, Language.MEITEI_MAYEK)
      .concatMap((Function<String, ObservableSource<Boolean>>) s -> mApi.count(s)
        .concatMap(count -> Observable.just(mDao.getAll(s).size() == count.getCount())))
      .filter(b -> !b)
      .firstOrError().subscribeOn(Schedulers.io());
  }
  
  public Observable<DownloadStatus> getDownloader() {
    return Observable.fromArray(Language.ENGLISH, Language.MEITEI_MAYEK, Language.BENGALI)
      .concatMap((Function<String, ObservableSource<List<WordEntity>>>) lang -> mApi.count(lang)
        .map(c -> {
          setCount(c.getCount());
          setLang(lang);
          return mCount;
        }).concatMap((Function<Integer, ObservableSource<List<WordEntity>>>) integer -> {
          List<WordEntity> l = mDao.getAll(mLang);
          
          Log.d(TAG, "getDownloader: Lang - " + mLang);
          Log.d(TAG, "getDownloader: Server Count - " + mCount);
          Log.d(TAG, "getDownloader: Local Count - " + l.size());
          return Observable.just(l);
        })).concatMap((Function<List<WordEntity>, ObservableSource<List<WordEntity>>>) list ->
        Observable.create((ObservableOnSubscribe<List<DownloadPosition>>) emitter -> {
          if(list.size() == mCount) {
            Log.d(TAG, "getDownloader: Completed...");
            emitter.onComplete();
          }else {
            List<DownloadPosition> l = new ArrayList<>();
            
            for(int i = 0; i < (mCount / mBufferSize); i++) {
              final int start = i * mBufferSize + 1;
              l.add(new DownloadPosition() {
                @Override
                public int getStart() {
                  return start;
                }
                
                @Override
                public int getSize() {
                  return mBufferSize;
                }
              });
            }
            
            l.add(new DownloadPosition() {
              @Override
              public int getStart() {
                return (mCount / mBufferSize) * mBufferSize + 1;
              }
              
              @Override
              public int getSize() {
                return (mCount % mBufferSize);
              }
            });
            
            emitter.onNext(l);
          }
        }).concatMap(l -> Observable.fromIterable(l)
          .concatMap(pos -> {
            Log.d(TAG, "Downloader: Size - " + pos.getSize());
            return mApi.getPositionalWords(mLang, pos.getStart(), pos.getSize())
              .concatMap(words -> Observable.fromIterable(words)
                .map(word -> WordMapper.from(word, mLang, false))
                .buffer(pos.getSize()));
          }))).map(l -> {
        mDao.insert(l.toArray(new WordEntity[]{}));
        
        int progress;
        
        if(l.size() == 0) {
          progress = 0;
        }else {
          Log.d(TAG, String.format("getDownloader: Downloaded from %d - %d",
            l.get(0).getWordId(), l.get(l.size() - 1).getWordId()));
          progress = l.get(l.size() - 1).getWordId();
        }
        
        return new DownloadStatus() {
          @Override
          public String getLang() {
            return mLang;
          }
          
          @Override
          public int getMax() {
            return mCount;
          }
          
          @Override
          public int getProgress() {
            return progress;
          }
        };
      });
  }
  
  private void setCount(int count) {
    mCount = count;
  }
  
  public void setLang(String lang) {
    this.mLang = lang;
  }
  
  public interface DownloadStatus {
    String getLang();
    
    int getMax();
    
    int getProgress();
  }
  
  public interface DownloadPosition {
    int getStart();
    
    int getSize();
  }
  
}
