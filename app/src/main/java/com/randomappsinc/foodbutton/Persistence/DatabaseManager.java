package com.randomappsinc.foodbutton.Persistence;

import android.content.Context;

import com.randomappsinc.foodbutton.Models.Restaurant;
import com.randomappsinc.foodbutton.Utils.MyApplication;
import com.randomappsinc.foodbutton.Utils.RestaurantUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by alexanderchiou on 4/14/16.
 */
public class DatabaseManager {
    private static final long CURRENT_REALM_VERSION = 1;
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
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(context)
                .schemaVersion(CURRENT_REALM_VERSION)
                .migration(migration)
                .build();
        realm = Realm.getInstance(realmConfig);
    }

    RealmMigration migration = new RealmMigration() {
        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
            // DynamicRealm exposes an editable schema
            RealmSchema schema = realm.getSchema();

            if (oldVersion == 0) {
                schema.get("RestaurantDO")
                        .addField("snippetText", String.class);
            }
        }
    };

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
