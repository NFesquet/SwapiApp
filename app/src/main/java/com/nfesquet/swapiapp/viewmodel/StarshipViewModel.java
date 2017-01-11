package com.nfesquet.swapiapp.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.nfesquet.swapiapp.adapter.ItemViewModel;
import com.nfesquet.swapiapp.fragment.ListItemFragment;
import com.nfesquet.swapiapp.model.ApiResult;
import com.nfesquet.swapiapp.model.Starship;
import com.nfesquet.swapiapp.model.SwapiModel;

import retrofit2.Call;

public class StarshipViewModel extends Starship implements ItemViewModel {

    private static StarshipViewModel mInstance;

    private StarshipViewModel() { }

    protected StarshipViewModel(Parcel in) {
    }

    public static final Creator<StarshipViewModel> CREATOR = new Creator<StarshipViewModel>() {
        @Override
        public StarshipViewModel createFromParcel(Parcel in) {
            return new StarshipViewModel(in);
        }

        @Override
        public StarshipViewModel[] newArray(int size) {
            return new StarshipViewModel[size];
        }
    };

    @Override
    public int layoutId() {
        return 0;
    }

    @Override
    public Call<ApiResult<? extends SwapiModel>> getService(int page) {
        return ListItemFragment.getService().listStarships(page);
    }

    @Override
    public ItemViewModel getInstance() {
        if (mInstance == null) mInstance = new StarshipViewModel();
        return mInstance;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
