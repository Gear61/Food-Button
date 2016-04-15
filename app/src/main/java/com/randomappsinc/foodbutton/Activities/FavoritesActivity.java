package com.randomappsinc.foodbutton.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.randomappsinc.foodbutton.Adapters.FavoritesAdapter;
import com.randomappsinc.foodbutton.Fragments.RestaurantFragment;
import com.randomappsinc.foodbutton.R;
import com.randomappsinc.foodbutton.Restaurant.Restaurant;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by alexanderchiou on 4/14/16.
 */
public class FavoritesActivity extends StandardActivity {
    @Bind(R.id.favorites) ListView favorites;
    @Bind(R.id.no_favorites) View noFavorites;

    private FavoritesAdapter favoritesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        favoritesAdapter = new FavoritesAdapter(this, noFavorites);
        favorites.setAdapter(favoritesAdapter);
    }

    @OnItemClick(R.id.favorites)
    public void viewRestaurant(int position) {
        Restaurant restaurant = favoritesAdapter.getItem(position);
        Intent intent = new Intent(this, RestaurantActivity.class);
        intent.putExtra(RestaurantFragment.RESTAURANT_KEY, restaurant);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        favoritesAdapter.syncWithDb();
    }
}
