package com.randomappsinc.foodbutton.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.foodbutton.Adapters.RestaurantsAdapter;
import com.randomappsinc.foodbutton.Models.Restaurant;
import com.randomappsinc.foodbutton.Persistence.DatabaseManager;
import com.randomappsinc.foodbutton.Persistence.PreferencesManager;
import com.randomappsinc.foodbutton.R;
import com.randomappsinc.foodbutton.Utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnPageChange;

/**
 * Created by alexanderchiou on 3/27/16.
 */
public class SuggestionsActivity extends StandardActivity {
    @Bind(R.id.restaurant_pager) ViewPager restaurantPager;

    private RestaurantsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_container);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new RestaurantsAdapter(getFragmentManager());
        restaurantPager.setAdapter(adapter);

        if (PreferencesManager.get().shouldShowInstructions()) {
            new MaterialDialog.Builder(this)
                    .title(R.string.instructions_title)
                    .content(R.string.instructions)
                    .positiveText(android.R.string.yes)
                    .show();
        }
    }

    @OnPageChange(R.id.restaurant_pager)
    public void onRestaurantChanged() {
        invalidateOptionsMenu();
    }

    @OnClick(R.id.view_on_yelp)
    public void viewOnYelp() {
        int currentPosition = restaurantPager.getCurrentItem();
        Restaurant currentRestaurant = adapter.getRestaurant(currentPosition);
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(currentRestaurant.getMobileUrl())));
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
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, currentRestaurant.getShareText());
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_with)));
                return true;
            case R.id.load_random_restaurant:
                loadRandomRestaurant();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
