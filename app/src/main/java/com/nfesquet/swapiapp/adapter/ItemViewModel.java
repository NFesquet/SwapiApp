package com.nfesquet.swapiapp.adapter;

import android.os.Parcelable;

import com.nfesquet.swapiapp.model.ApiResult;
import com.nfesquet.swapiapp.model.SwapiModel;

import retrofit2.Call;

public interface ItemViewModel extends Parcelable {
    int layoutId();
    String getUrl();
    Call<ApiResult<? extends SwapiModel>> getService(int page);
    ItemViewModel getInstance();
}
