package com.randomappsinc.foodbutton.API;

import android.os.AsyncTask;

import com.randomappsinc.foodbutton.Activities.MainActivity;
import com.randomappsinc.foodbutton.Models.Restaurant;
import com.randomappsinc.foodbutton.R;
import com.randomappsinc.foodbutton.Utils.MyApplication;
import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.SearchResponse;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alexanderchiou on 3/26/16.
 */
public class RestClient {
    public static final int HTTP_STATUS_OK = 200;
    public static final String SEARCH_FAIL = "searchFail";

    private static final String APP_ID = "Y6mN70GyUV5fdqOvceOrVQ";
    private static final String APP_SECRET = "I9mmDM1JqyqijjY1JR9i0XYMPEeiUZQCQIc0vSY0iaoah2rm90mfisHYV1oPVtwl";

    private static RestClient restClient;

    public static RestClient get() {
        if (restClient == null) {
            restClient = new RestClient();
        }
        return restClient;
    }

    private YelpFusionApi yelpFusionApi;

    private RestClient() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    yelpFusionApi = new YelpFusionApiFactory().createAPI(APP_ID, APP_SECRET);
                } catch (IOException ignored) {}
            }
        });
    }

    public void doSearch(String location, MainActivity activity) {
        if (yelpFusionApi != null) {
            Call<SearchResponse> call = yelpFusionApi.getBusinessSearch(APIUtils.getQueryParams(location));
            call.enqueue(new Callback<SearchResponse>() {
                @Override
                public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                    if (response.code() == HTTP_STATUS_OK) {
                        ArrayList<Restaurant> restaurants = new ArrayList<>();
                        for (Business business : response.body().getBusinesses()) {
                            restaurants.add(new Restaurant(business));
                        }
                        EventBus.getDefault().post(restaurants);
                    } else {
                        EventBus.getDefault().post(SEARCH_FAIL);
                    }
                }

                @Override
                public void onFailure(Call<SearchResponse> call, Throwable t) {
                    EventBus.getDefault().post(SEARCH_FAIL);
                }
            });
        } else {
            activity.showSnackbar(MyApplication.getAppContext().getString(R.string.client_fail));
        }
    }
}
