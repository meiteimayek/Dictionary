package me.gyanendrokh.meiteimayek.dictionary.api.service;

import me.gyanendrokh.meiteimayek.dictionary.api.UserApi;

public class UserService extends BaseService<UserApi> {

  private static String mToken = "";
  private static UserService mSelf = null;

  public static UserService getInstance(String token) {
    if(mSelf == null) {
      mToken = token;
      mSelf = new UserService(token);
    }else {
      if(!mToken.equals(token)) {
        mSelf = new UserService(token);
      }
    }

    return mSelf;
  }

  private UserApi mApi;

  private UserService(String token) {
    super(token);

    mApi = createService(UserApi.class);
  }

  @Override
  public String getBaseUrl() {
    return Endpoints.DICTIONARY_API;
  }

  @Override
  public UserApi getApi() {
    return mApi;
  }

}
