package me.gyanendrokh.meiteimayek.dictionary.api.service;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class BaseService<S> {

  private OkHttpClient.Builder mClientBuilder;
  private Retrofit.Builder mBuilder;

  public BaseService() {
    mClientBuilder = new OkHttpClient.Builder();
    mClientBuilder.callTimeout(2, TimeUnit.MINUTES);

    mBuilder = new Retrofit.Builder()
      .baseUrl(getBaseUrl())
      .addConverterFactory(GsonConverterFactory.create())
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
  }

  public BaseService(String token) {
    this();
    mClientBuilder.addInterceptor(i -> i.proceed(
      i.request().newBuilder()
        .header(
          "Authorization",
          "Bearer " + token.replaceAll(" ", "")
        ).build()
    ));
  }

  public abstract String getBaseUrl();

  public S createService(Class<S> serviceClass) {
    return mBuilder.client(mClientBuilder.build())
      .build().create(serviceClass);
  }

  public abstract S getApi();

}
