package me.gyanendrokh.meiteimayek.dictionary.api;

import java.util.List;

import io.reactivex.Observable;
import me.gyanendrokh.meiteimayek.dictionary.api.model.Count;
import me.gyanendrokh.meiteimayek.dictionary.api.model.Word;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SelectApi {

  @GET("g/c/{lang}")
  Observable<Count> count(@Path("lang") String lang);

  @GET("g/w/{lang}/{word}")
  Observable<Word> getWord(@Path("lang") String lang, @Path("word") String word);

  @GET("g/w-i/{lang}/{id}")
  Observable<Word> getWord(@Path("lang") String lang, @Path("id") int id);

  @GET("g/a/{lang}")
  Observable<List<Word>> getWords(@Path("lang") String lang);

  @GET("g/a/{lang}")
  Observable<List<Word>> getWords(@Path("lang") String lang, @Query("pag") int pag, @Query("limit") int limit);

  @GET("g/p-s/{lang}/{letter}")
  Observable<Word> getStart(@Path("lang") String lang, @Path("letter") String letter);

  @GET("g/a-p/{lang}")
  Observable<List<Word>> getPositionalWords(@Path("lang") String lang, @Query("start") int start, @Query("size") int size);

  @GET("g/s/{lang}/{keyword}")
  Observable<List<Word>> search(@Path("lang") String lang, @Path("keyword") String keyword);

  @GET("g/s/{lang}/{keyword}")
  Observable<List<Word>> search(@Path("lang") String lang, @Path("keyword") String keyword, @Query("pag") int pag, @Query("limit") int limit);

}
