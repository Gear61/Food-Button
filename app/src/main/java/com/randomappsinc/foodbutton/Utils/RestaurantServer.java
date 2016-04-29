package com.randomappsinc.foodbutton.Utils;

import com.randomappsinc.foodbutton.Models.Restaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by alexanderchiou on 3/27/16.
 */
public class RestaurantServer {
    private static RestaurantServer instance;

    public static RestaurantServer get() {
        if (instance == null) {
            instance = new RestaurantServer();
        }
        return instance;
    }

    private List<Restaurant> restaurants;

    private RestaurantServer () {
        restaurants = new ArrayList<>();
    }

    public void setRestaurantList(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
        Collections.shuffle(restaurants);
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }
}
