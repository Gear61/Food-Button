package com.randomappsinc.foodbutton;

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

    private List<Restaurant> unchosenRestaurants;
    private List<Restaurant> chosenRestaurants;

    private RestaurantServer () {
        unchosenRestaurants = new ArrayList<>();
        chosenRestaurants = new ArrayList<>();
    }

    public void setRestaurantList(List<Restaurant> restaurants) {
        unchosenRestaurants = restaurants;
        Collections.shuffle(unchosenRestaurants);
    }

    public Restaurant getRestaurant() {
        if (unchosenRestaurants.isEmpty()) {
            if (chosenRestaurants.isEmpty()) {
                return null;
            }
            else {
                resetList();
            }
        }
        Restaurant chosenOne = unchosenRestaurants.get(0);
        unchosenRestaurants.remove(0);
        chosenRestaurants.add(chosenOne);
        return chosenOne;
    }

    private void resetList() {
        unchosenRestaurants.addAll(chosenRestaurants);
        Collections.shuffle(unchosenRestaurants);
        chosenRestaurants.clear();
    }
}
