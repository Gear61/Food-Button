package com.randomappsinc.foodbutton.API.Models;

import com.randomappsinc.foodbutton.Models.Restaurant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexanderchiou on 3/27/16.
 */
public class SearchResponse {
    private List<Business> businesses = new ArrayList<>();

    public ArrayList<Restaurant> getRestaurants() {
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        for (Business business : businesses) {
            if (!business.isClosed()) {
                restaurants.add(business.toRestaurant());
            }
        }
        return restaurants;
    }
}
