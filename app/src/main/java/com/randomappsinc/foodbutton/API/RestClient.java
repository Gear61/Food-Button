package com.randomappsinc.foodbutton.API;

import com.randomappsinc.foodbutton.API.OAuth.DecodeInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alexanderchiou on 3/26/16.
 */
public class RestClient {
    private static RestClient restClient;

    public static RestClient get() {
        if (restClient == null) {
            restClient = new RestClient();
        }
        return restClient;
    }

    private YelpService yelpService;

    private RestClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getHttpClient())
                .build();

        yelpService = retrofit.create(YelpService.class);
    }

    private OkHttpClient getHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new DecodeInterceptor())
                .build();
    }

    public YelpService getYelpService() {
        return yelpService;
    }
}
