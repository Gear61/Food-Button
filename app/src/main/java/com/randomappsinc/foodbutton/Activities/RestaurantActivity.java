package com.randomappsinc.foodbutton.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.foodbutton.API.Models.Business;
import com.randomappsinc.foodbutton.Fragments.RestaurantFragment;
import com.randomappsinc.foodbutton.Persistence.DatabaseManager;
import com.randomappsinc.foodbutton.R;
import com.randomappsinc.foodbutton.Restaurant.Restaurant;
import com.randomappsinc.foodbutton.Restaurant.RestaurantUtils;
import com.randomappsinc.foodbutton.Utils.UIUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by alexanderchiou on 4/15/16.
 */
public class RestaurantActivity extends StandardActivity {
    @Bind(R.id.restaurant_picture) ImageView restaurantPicture;
    @Bind(R.id.restaurant_name) TextView restaurantName;
    @Bind(R.id.categories) TextView categories;
    @Bind({R.id.first_star, R.id.second_star, R.id.third_star,
           R.id.fourth_star, R.id.fifth_star}) List<ImageView> starViews;
    @Bind(R.id.num_reviews) TextView numReviews;
    @Bind(R.id.address) TextView address;
    @Bind(R.id.phone_number) TextView phoneNumber;

    private Restaurant currentRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_restaurant);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        currentRestaurant = getIntent().getParcelableExtra(RestaurantFragment.RESTAURANT_KEY);
        loadRestaurant();
    }

    private void loadRestaurant() {
        Picasso.with(this).load(currentRestaurant.getImageUrl()).into(restaurantPicture);
        restaurantName.setText(currentRestaurant.getName());
        categories.setText(currentRestaurant.getCategories());

        RestaurantUtils.loadStarImages(starViews, currentRestaurant.getRating());
        String numReviewsText = String.format(getString(R.string.num_reviews), currentRestaurant.getNumReviews());
        numReviews.setText(numReviewsText);

        address.setText(currentRestaurant.getAddress());
        phoneNumber.setText(UIUtils.humanizePhoneNumber(currentRestaurant.getPhoneNumber()));
    }

    @OnClick(R.id.address_container)
    public void goToRestaurant() {
        if (!currentRestaurant.getAddress().equals(Business.NO_ADDRESS)) {
            String mapUri = "google.navigation:q=" + currentRestaurant.getAddress()
                    + " " + currentRestaurant.getName();
            startActivity(Intent.createChooser(
                    new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(mapUri)),
                    getString(R.string.navigate_with)));
        }
    }

    @OnClick(R.id.phone_number_container)
    public void callRestaurant() {
        if (!currentRestaurant.getPhoneNumber().equals(Business.NO_PHONE_NUMBER)) {
            String phoneUri = "tel:" + phoneNumber.getText().toString();
            startActivity(Intent.createChooser(
                    new Intent(Intent.ACTION_DIAL, Uri.parse(phoneUri)),
                    getString(R.string.call_with)));
        }
    }

    @OnClick(R.id.view_on_yelp)
    public void viewOnYelp() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(currentRestaurant.getMobileUrl())));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_restaurant_menu, menu);

        if (DatabaseManager.get().isRestaurantFavorited(currentRestaurant)) {
            menu.findItem(R.id.favorite_restaurant).setTitle(R.string.unfavorite_restaurant);
            UIUtils.loadMenuIcon(menu, R.id.favorite_restaurant, IoniconsIcons.ion_android_star);
        } else {
            UIUtils.loadMenuIcon(menu, R.id.favorite_restaurant, IoniconsIcons.ion_android_star_outline);
        }

        UIUtils.loadMenuIcon(menu, R.id.share_restaurant, IoniconsIcons.ion_android_share_alt);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        }
        return super.onOptionsItemSelected(item);
    }
}
