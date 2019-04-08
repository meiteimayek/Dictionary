package me.gyanendrokh.meiteimayek.dictionary.api;

import io.reactivex.Observable;
import me.gyanendrokh.meiteimayek.dictionary.api.model.User;
import retrofit2.http.GET;

public interface UserApi {

  @GET("user")
  Observable<User> getUser();

}
