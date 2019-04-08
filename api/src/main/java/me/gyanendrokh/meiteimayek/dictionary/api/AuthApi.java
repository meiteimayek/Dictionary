package me.gyanendrokh.meiteimayek.dictionary.api;

import io.reactivex.Observable;
import me.gyanendrokh.meiteimayek.dictionary.api.model.LoginToken;
import me.gyanendrokh.meiteimayek.dictionary.api.model.UserCredential;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {

  @POST("auth/login")
  Observable<LoginToken> login(@Body UserCredential user);

}
