package com.randomappsinc.foodbutton.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.foodbutton.Adapters.FavoritesAdapter;
import com.randomappsinc.foodbutton.Fragments.RestaurantFragment;
import com.randomappsinc.foodbutton.R;
import com.randomappsinc.foodbutton.Restaurant.Restaurant;
import com.randomappsinc.foodbutton.Utils.UIUtils;

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
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorites_menu, menu);
        if (favorites.getCount() == 0) {
            menu.findItem(R.id.load_random_restaurant).setVisible(false);
        } else {
            UIUtils.loadMenuIcon(menu, R.id.load_random_restaurant, IoniconsIcons.ion_shuffle);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.load_random_restaurant:
                Intent intent = new Intent(this, RestaurantActivity.class);
                intent.putExtra(RestaurantFragment.RESTAURANT_KEY, favoritesAdapter.getRandomRestaurant());
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
