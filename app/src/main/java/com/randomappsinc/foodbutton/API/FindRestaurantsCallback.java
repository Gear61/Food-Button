package com.randomappsinc.foodbutton.API;

import android.support.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindRestaurantsCallback implements Callback<RestaurantSearchResults> {

    @Override
    public void onResponse(@NonNull Call<RestaurantSearchResults> call, @NonNull Response<RestaurantSearchResults> response) {
        if (response.code() == ApiConstants.HTTP_STATUS_OK) {
            RestClient.getInstance().processRestaurants(response.body().getRestaurants());
        }
        // TODO: Process failure here
    }

    @Override
    public void onFailure(@NonNull Call<RestaurantSearchResults> call, @NonNull Throwable t) {
        // TODO: Deal with the place search failing case
    }
}
