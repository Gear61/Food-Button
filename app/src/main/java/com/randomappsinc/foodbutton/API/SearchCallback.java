package com.randomappsinc.foodbutton.API;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alexanderchiou on 3/26/16.
 */
public class SearchCallback implements Callback<IgnoredResponse> {
    @Override
    public void onResponse(Call<IgnoredResponse> call, Response<IgnoredResponse> response) {
        System.out.println(response.raw().toString());
    }

    @Override
    public void onFailure(Call<IgnoredResponse> call, Throwable t) {
        System.out.println(t.getMessage());
    }
}
