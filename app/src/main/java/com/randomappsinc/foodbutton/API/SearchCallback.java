package com.randomappsinc.foodbutton.API;

import android.os.Handler;

import com.randomappsinc.foodbutton.API.Models.SearchResponse;
import com.randomappsinc.foodbutton.API.OAuth.ApiUtils;
import com.randomappsinc.foodbutton.Models.Restaurant;
import com.randomappsinc.foodbutton.RestaurantServer;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alexanderchiou on 3/26/16.
 */
public class SearchCallback implements Callback<SearchResponse> {
    public static final String RESTAURANTS_FETCHED = "restaurantsFetched";
    public static final String NO_RESTAURANTS = "noRestaurants";
    public static final String SEARCH_FAIL = "searchFail";
    private static final int FINAL_TRY_COUNT = 5;

    private int tryCount;
    private String location;

    public SearchCallback(int tryCount, String location) {
        this.tryCount = tryCount;
        this.location = location;
    }

    @Override
    public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
        if (response.code() == ApiConstants.HTTP_STATUS_OK) {
            List<Restaurant> restaurants = response.body().getRestaurants();
            if (!restaurants.isEmpty()) {
                RestaurantServer.get().setRestaurantList(restaurants);
                EventBus.getDefault().post(RESTAURANTS_FETCHED);
            }
            else {
                EventBus.getDefault().post(NO_RESTAURANTS);
            }
        }
        else {
            processFailure();
        }
    }

    @Override
    public void onFailure(Call<SearchResponse> call, Throwable t) {
        processFailure();
    }

    public void processFailure() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (tryCount == FINAL_TRY_COUNT) {
                    EventBus.getDefault().post(SEARCH_FAIL);
                }
                else {
                    RestClient.get().getYelpService()
                            .doSearch(ApiUtils.getSearchQueryMap(location))
                            .enqueue(new SearchCallback(tryCount + 1, location));
                }
            }
        }, (long) Math.pow(2, tryCount) * 1000L);
    }
}
