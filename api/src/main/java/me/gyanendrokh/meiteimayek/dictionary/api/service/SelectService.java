package me.gyanendrokh.meiteimayek.dictionary.api.service;

import me.gyanendrokh.meiteimayek.dictionary.api.SelectApi;

public class SelectService extends BaseService<SelectApi> {

  private static SelectService mSelf = null;

  public static SelectService getInstance() {
    if(mSelf == null) {
      mSelf = new SelectService();
    }

    return mSelf;
  }

  private SelectApi mApi;

  private SelectService() {
    super();

    mApi = createService(SelectApi.class);
  }

  @Override
  public String getBaseUrl() {
    return Endpoints.DICTIONARY_API;
  }

  @Override
  public SelectApi getApi() {
    return mApi;
  }

}
