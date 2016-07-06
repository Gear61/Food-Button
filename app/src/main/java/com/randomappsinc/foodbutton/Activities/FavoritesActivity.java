package com.randomappsinc.foodbutton.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.foodbutton.Adapters.FavoritesAdapter;
import com.randomappsinc.foodbutton.Fragments.RestaurantFragment;
import com.randomappsinc.foodbutton.Models.FavoritesFilter;
import com.randomappsinc.foodbutton.Models.Restaurant;
import com.randomappsinc.foodbutton.Persistence.DatabaseManager;
import com.randomappsinc.foodbutton.R;
import com.randomappsinc.foodbutton.Utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by alexanderchiou on 4/14/16.
 */
public class FavoritesActivity extends StandardActivity {
    @Bind(R.id.favorites) ListView favorites;
    @Bind(R.id.no_favorites) TextView noFavorites;

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            FavoritesFilter filter = data.getParcelableExtra(FilterActivity.FILTER_KEY);
            favoritesAdapter.applyFilter(filter);
        }
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
            // Only hide filters if they legitimately have no favorites
            if (DatabaseManager.get().getFavorites().isEmpty()) {
                menu.findItem(R.id.filters).setVisible(false);
            } else {
                UIUtils.loadMenuIcon(menu, R.id.filters, IoniconsIcons.ion_android_options);
            }
        } else {
            UIUtils.loadMenuIcon(menu, R.id.filters, IoniconsIcons.ion_android_options);
            UIUtils.loadMenuIcon(menu, R.id.load_random_restaurant, IoniconsIcons.ion_shuffle);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filters:
                Intent setFilterIntent = new Intent(this, FavoritesFilterActivity.class);
                setFilterIntent.putExtra(FilterActivity.FILTER_KEY, favoritesAdapter.getFilter());
                startActivity(setFilterIntent);
                return true;
            case R.id.load_random_restaurant:
                Intent intent = new Intent(this, RestaurantActivity.class);
                intent.putExtra(RestaurantFragment.RESTAURANT_KEY, favoritesAdapter.getRandomRestaurant());
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
