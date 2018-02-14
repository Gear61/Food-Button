package com.randomappsinc.foodbutton.API;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YelpService {

    @GET("v3/businesses/search")
    Call<RestaurantSearchResults> findRestaurants(@Query("term") String term,
                                                  @Query("location") String location,
                                                  @Query("limit") int limit,
                                                  @Query("open_now") boolean openNow);
}
