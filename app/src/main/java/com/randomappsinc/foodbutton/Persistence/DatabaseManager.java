package com.randomappsinc.foodbutton.Persistence;

import com.randomappsinc.foodbutton.Models.FavoritesFilter;
import com.randomappsinc.foodbutton.Models.Restaurant;
import com.randomappsinc.foodbutton.Utils.MyApplication;
import com.randomappsinc.foodbutton.Utils.RestaurantUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmQuery;
import io.realm.RealmSchema;

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
        Realm.init(MyApplication.getAppContext());
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .schemaVersion(CURRENT_REALM_VERSION)
                .migration(migration)
                .build();
        Realm.setDefaultConfiguration(realmConfig);
        realm = Realm.getInstance(realmConfig);
    }

    RealmMigration migration = (realm, oldVersion, newVersion) -> {
        RealmSchema schema = realm.getSchema();
        if (oldVersion == 0) {
            schema.get("RestaurantDO").addField("snippetText", String.class);
        }
    };

    public void addFavorite(final Restaurant restaurant) {
        realm.executeTransaction(realm -> realm.copyToRealm(restaurant.toRestaurantDO()));
    }

    public void removeFavorite(final Restaurant restaurant) {
        realm.executeTransaction(realm -> {
            RestaurantDO restaurantDO = realm.where(RestaurantDO.class)
                    .equalTo("yelpId", restaurant.getYelpId())
                    .findFirst();
            if (restaurantDO != null) {
                restaurantDO.deleteFromRealm();
            }
        });
    }

    public boolean isRestaurantFavorited(final Restaurant restaurant) {
        return realm.where(RestaurantDO.class)
                .equalTo("yelpId", restaurant.getYelpId())
                .findFirst() != null;
    }

    public List<String> getCategories() {
        List<CategoryDO> categoryDOs = realm.where(CategoryDO.class).findAll();
        HashSet<String> categoriesSet = new HashSet<>();
        for (CategoryDO categoryDO : categoryDOs) {
            categoriesSet.add(categoryDO.getCategory());
        }
        List<String> categories = new ArrayList<>(categoriesSet);
        Collections.sort(categories);
        return categories;
    }

    public List<String> getCities() {
        List<Restaurant> restaurants = getFavorites(new FavoritesFilter());
        HashSet<String> categoriesSet = new HashSet<>();
        for (Restaurant restaurant : restaurants) {
            categoriesSet.add(restaurant.getCity());
        }
        List<String> categories = new ArrayList<>(categoriesSet);
        Collections.sort(categories);
        return categories;
    }

    public List<Restaurant> getFavorites(FavoritesFilter filter) {
        RealmQuery<RestaurantDO> query = realm.where(RestaurantDO.class);

        for (int i = 0; i < filter.getCategories().size(); i++) {
            if (i != 0) {
                query = query.or();
            } else {
                query = query.beginGroup();
            }
            query = query.equalTo("categories.category", filter.getCategories().get(i));
            if (i == filter.getCategories().size() - 1) {
                query = query.endGroup();
            }
        }

        for (int i = 0; i < filter.getCities().size(); i++) {
            if (i != 0) {
                query = query.or();
            } else {
                query = query.beginGroup();
            }
            query = query.equalTo("city", filter.getCities().get(i));
            if (i == filter.getCities().size() - 1) {
                query = query.endGroup();
            }
        }

        List<Restaurant> restaurants = new ArrayList<>();
        List<RestaurantDO> restaurantDOs = query.findAll();
        for (RestaurantDO restaurantDO : restaurantDOs) {
            restaurants.add(RestaurantUtils.extractFromDO(restaurantDO));
        }
        return restaurants;
    }
}
