package me.gyanendrokh.meiteimayek.dictionary.api.service;

import me.gyanendrokh.meiteimayek.dictionary.api.AuthApi;

public class AuthService extends BaseService<AuthApi> {

  private static AuthService mSelf = null;

  public static AuthService getInstance() {
    if(mSelf == null) {
      mSelf = new AuthService();
    }

    return mSelf;
  }

  private AuthApi mApi;

  private AuthService() {
    super();
    mApi = createService(AuthApi.class);
  }

  @Override
  public String getBaseUrl() {
    return Endpoints.AUTH_API;
  }

  @Override
  public AuthApi getApi() {
    return mApi;
  }

}
