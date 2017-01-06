package com.nfesquet.swapiapp.service;

import com.nfesquet.swapiapp.model.ApiResult;
import com.nfesquet.swapiapp.model.Starship;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SwapiService {
    @GET("starships")
    Call<ApiResult<Starship>> listStarships(@Query("page") int page);

    @GET("starships/{starship_id}/")
    Call<Starship> starship(@Path("starship_id") String id);
}
