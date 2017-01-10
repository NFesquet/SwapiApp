package com.nfesquet.swapiapp.service;

import com.nfesquet.swapiapp.model.ApiResult;
import com.nfesquet.swapiapp.model.Film;
import com.nfesquet.swapiapp.model.People;
import com.nfesquet.swapiapp.model.Planet;
import com.nfesquet.swapiapp.model.Starship;
import com.nfesquet.swapiapp.model.Vehicle;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SwapiService {
    @GET("starships")
    Call<ApiResult<Starship>> listStarships(@Query("page") int page);

    @GET("people")
    Call<ApiResult<People>> listPeople(@Query("page") int page);

    @GET("vehicles")
    Call<ApiResult<Vehicle>> listVehicles(@Query("page") int page);

    @GET("planets")
    Call<ApiResult<Planet>> listPlanets(@Query("page") int page);

    @GET("films")
    Call<ApiResult<Film>> listFilms(@Query("page") int page);

    @GET("starships/{item_id}/")
    Call<Starship> starship(@Path("item_id") String id);

    @GET("people/{item_id}/")
    Call<People> people(@Path("item_id") String id);

    @GET("vehicle/{item_id}/")
    Call<Vehicle> vehicle(@Path("item_id") String id);

    @GET("planet/{item_id}/")
    Call<Planet> planet(@Path("item_id") String id);

    @GET("film/{item_id}/")
    Call<Film> film(@Path("item_id") String id);
}
