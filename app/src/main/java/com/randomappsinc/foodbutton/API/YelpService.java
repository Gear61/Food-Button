package com.randomappsinc.foodbutton.API;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by alexanderchiou on 3/26/16.
 */
public interface YelpService {
    @GET("search")
    Call<IgnoredResponse> doSearch(@QueryMap Map<String, String> parameters);
}
