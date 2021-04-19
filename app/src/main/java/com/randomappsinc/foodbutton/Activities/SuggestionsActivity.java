package com.randomappsinc.foodbutton.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.viewpager.widget.ViewPager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.foodbutton.Adapters.RestaurantsAdapter;
import com.randomappsinc.foodbutton.Models.Restaurant;
import com.randomappsinc.foodbutton.Persistence.DatabaseManager;
import com.randomappsinc.foodbutton.Persistence.PreferencesManager;
import com.randomappsinc.foodbutton.R;
import com.randomappsinc.foodbutton.Utils.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnPageChange;

public class SuggestionsActivity extends StandardActivity {

    @BindView(R.id.restaurant_pager) ViewPager restaurantPager;

    private RestaurantsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_container);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        List<Restaurant> restaurants = getIntent().getParcelableArrayListExtra(Restaurant.RESTAURANTS_KEY);
        adapter = new RestaurantsAdapter(getSupportFragmentManager(), restaurants);
        restaurantPager.setAdapter(adapter);

        if (PreferencesManager.get().shouldShowInstructions()) {
            new MaterialDialog.Builder(this)
                    .customView(R.layout.suggestions_instructions, false)
                    .positiveText(R.string.got_it)
                    .show();
        }
    }

    @OnPageChange(R.id.restaurant_pager)
    public void onRestaurantChanged() {
        invalidateOptionsMenu();
    }

    @OnClick(R.id.start_navigation)
    public void startNavigation() {
        int currentPosition = restaurantPager.getCurrentItem();
        Restaurant currentRestaurant = adapter.getRestaurant(currentPosition);
        if (!currentRestaurant.getAddress().equals(Restaurant.NO_ADDRESS)) {
            String mapUri = "google.navigation:q=" + currentRestaurant.getAddress()
                    + " " + currentRestaurant.getName();
            startActivity(Intent.createChooser(
                    new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(mapUri)),
                    getString(R.string.navigate_with)));
        }
    }

    private void loadRandomRestaurant() {
        int currentPosition = restaurantPager.getCurrentItem();
        int newIndex = adapter.getRandomIndex(currentPosition);
        restaurantPager.setCurrentItem(newIndex, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.restaurant_menu, menu);

        int currentPosition = restaurantPager.getCurrentItem();
        Restaurant currentRestaurant = adapter.getRestaurant(currentPosition);
        if (DatabaseManager.get().isRestaurantFavorited(currentRestaurant)) {
            menu.findItem(R.id.favorite_restaurant).setTitle(R.string.unfavorite_restaurant);
            UIUtils.loadMenuIcon(menu, R.id.favorite_restaurant, IoniconsIcons.ion_android_star);
        } else {
            UIUtils.loadMenuIcon(menu, R.id.favorite_restaurant, IoniconsIcons.ion_android_star_outline);
        }

        UIUtils.loadMenuIcon(menu, R.id.share_restaurant, IoniconsIcons.ion_android_share_alt);
        UIUtils.loadMenuIcon(menu, R.id.load_random_restaurant, IoniconsIcons.ion_shuffle);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int currentPosition = restaurantPager.getCurrentItem();
        Restaurant currentRestaurant = adapter.getRestaurant(currentPosition);

        switch (item.getItemId()) {
            case R.id.favorite_restaurant:
                if (DatabaseManager.get().isRestaurantFavorited(currentRestaurant)) {
                    DatabaseManager.get().removeFavorite(currentRestaurant);
                } else {
                    DatabaseManager.get().addFavorite(currentRestaurant);
                }
                invalidateOptionsMenu();
                return true;
            case R.id.share_restaurant:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.restaurant_info));
                sharingIntent.putExtra(Intent.EXTRA_TEXT, currentRestaurant.getShareText());
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_with)));
                return true;
            case R.id.load_random_restaurant:
                loadRandomRestaurant();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
