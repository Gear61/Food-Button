package com.randomappsinc.foodbutton.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.foodbutton.API.Models.Business;
import com.randomappsinc.foodbutton.Models.Restaurant;
import com.randomappsinc.foodbutton.R;
import com.randomappsinc.foodbutton.RestaurantServer;
import com.randomappsinc.foodbutton.Utils.PreferencesManager;
import com.randomappsinc.foodbutton.Utils.RatingUtils;
import com.randomappsinc.foodbutton.Utils.UIUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by alexanderchiou on 3/27/16.
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
        setContentView(R.layout.restaurant);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        loadNewRestaurant();
        if (PreferencesManager.get().shouldShowInstructions()) {
            showInstructions();
        }
    }

    private void loadNewRestaurant() {
        currentRestaurant = RestaurantServer.get().getRestaurant();
        Picasso.with(this).load(currentRestaurant.getImageUrl()).into(restaurantPicture);
        restaurantName.setText(currentRestaurant.getName());
        categories.setText(currentRestaurant.getCategories());

        RatingUtils.loadStarImages(starViews, currentRestaurant.getRating());
        String numReviewsText = String.format(getString(R.string.num_reviews), currentRestaurant.getNumReviews());
        numReviews.setText(numReviewsText);

        String addressText = getString(R.string.map_icon) + "  " + currentRestaurant.getAddress();
        address.setText(addressText);

        String phoneNumberText = getString(R.string.phone_icon) + "  " +
                UIUtils.humanizePhoneNumber(currentRestaurant.getPhoneNumber());
        phoneNumber.setText(phoneNumberText);
    }

    @OnClick(R.id.address)
    public void goToRestaurant() {
        if (!currentRestaurant.getAddress().equals(Business.NO_ADDRESS)) {
            String mapUri = "google.navigation:q=" + currentRestaurant.getAddress();
            startActivity(Intent.createChooser(
                    new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(mapUri)),
                    getString(R.string.navigate_with)));
        }
    }

    @OnClick(R.id.phone_number)
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

    private void showInstructions() {
        new MaterialDialog.Builder(this)
                .title(R.string.instructions)
                .content(R.string.restaurant_instructions)
                .positiveText(android.R.string.yes)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.restaurant_menu, menu);
        UIUtils.loadMenuIcon(menu, R.id.view_instructions, IoniconsIcons.ion_information_circled);
        UIUtils.loadMenuIcon(menu, R.id.load_new_restaurant, IoniconsIcons.ion_android_refresh);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.view_instructions:
                showInstructions();
                return true;
            case R.id.load_new_restaurant:
                loadNewRestaurant();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
