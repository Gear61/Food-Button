package com.randomappsinc.foodbutton.API;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface YelpService {

    @GET("v3/businesses/search")
    Call<RestaurantSearchResults> findRestaurants(@QueryMap Map<String, String> params);
}
