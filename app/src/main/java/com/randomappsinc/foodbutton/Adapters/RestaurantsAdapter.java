package com.randomappsinc.foodbutton.Adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.randomappsinc.foodbutton.Fragments.RestaurantFragment;
import com.randomappsinc.foodbutton.Models.Restaurant;
import com.randomappsinc.foodbutton.Utils.RestaurantServer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by alexanderchiou on 4/9/16.
 */
public class RestaurantsAdapter extends FragmentStatePagerAdapter {
    private List<Restaurant> restaurants;

    public RestaurantsAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        this.restaurants = RestaurantServer.get().getRestaurants();
    }

    public Restaurant getRestaurant(int position) {
        return restaurants.get(position);
    }

    public int getRandomIndex(int currentIndex) {
        if (getCount() == 1) {
            return 0;
        }
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < getCount(); i++) {
            if (i != currentIndex) {
                indexes.add(i);
            }
        }
        Collections.shuffle(indexes);
        return indexes.get(0);
    }

    @Override
    public Fragment getItem(int position) {
        return RestaurantFragment.create(restaurants.get(position));
    }

    @Override
    public int getCount() {
        return restaurants.size();
    }
}
