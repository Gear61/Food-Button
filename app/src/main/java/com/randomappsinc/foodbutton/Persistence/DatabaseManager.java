package com.randomappsinc.foodbutton.Persistence;

import android.content.Context;

import com.randomappsinc.foodbutton.Restaurant.Restaurant;
import com.randomappsinc.foodbutton.Restaurant.RestaurantUtils;
import com.randomappsinc.foodbutton.Utils.MyApplication;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by alexanderchiou on 4/14/16.
 */
public class DatabaseManager {
    private static DatabaseManager instance;

    public static DatabaseManager get() {
        if (instance == null) {
            instance = getSync();
        }
        return instance;
    }

    private static synchronized DatabaseManager getSync() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private Realm realm;

    private DatabaseManager() {
        Context context = MyApplication.getAppContext();
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(context).build();
        realm = Realm.getInstance(realmConfig);
    }

    public void addFavorite(final Restaurant restaurant) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(restaurant.toRestaurantDO());
            }
        });
    }

    public void removeFavorite(final Restaurant restaurant) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(RestaurantDO.class)
                        .equalTo("yelpId", restaurant.getYelpId())
                        .findFirst()
                        .removeFromRealm();
            }
        });
    }

    public boolean isRestaurantFavorited(final Restaurant restaurant) {
        return realm.where(RestaurantDO.class)
                .equalTo("yelpId", restaurant.getYelpId())
                .findFirst() != null;
    }

    public List<Restaurant> getFavorites() {
        List<Restaurant> restaurants = new ArrayList<>();

        List<RestaurantDO> restaurantDOs = realm.where(RestaurantDO.class).findAll();
        for (RestaurantDO restaurantDO : restaurantDOs) {
            restaurants.add(RestaurantUtils.extractFromDO(restaurantDO));
        }

        return restaurants;
    }
}
