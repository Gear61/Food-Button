package com.randomappsinc.foodbutton.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.SimpleShowcaseEventListener;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.foodbutton.API.Models.Business;
import com.randomappsinc.foodbutton.R;
import com.randomappsinc.foodbutton.Restaurant.Restaurant;
import com.randomappsinc.foodbutton.Restaurant.RestaurantServer;
import com.randomappsinc.foodbutton.Restaurant.RestaurantUtils;
import com.randomappsinc.foodbutton.Utils.PreferencesManager;
import com.randomappsinc.foodbutton.Utils.ToolbarActionItemTarget;
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
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.restaurant_picture) ImageView restaurantPicture;
    @Bind(R.id.restaurant_name) TextView restaurantName;
    @Bind(R.id.categories) TextView categories;

    @Bind({R.id.first_star, R.id.second_star, R.id.third_star,
           R.id.fourth_star, R.id.fifth_star}) List<ImageView> starViews;
    @Bind(R.id.num_reviews) TextView numReviews;

    @Bind(R.id.address) TextView address;
    @Bind(R.id.phone_number) TextView phoneNumber;

    private Restaurant currentRestaurant;
    private boolean hasShownShare;
    private boolean hasShownAddress;
    private boolean hasShownPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadNewRestaurant();
        if (PreferencesManager.get().shouldShowInstructions()) {
            startTutorial();
        }
    }

    private void loadNewRestaurant() {
        currentRestaurant = RestaurantServer.get().getRestaurant();
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

    private void startTutorial() {
        // Refresh button
        new ShowcaseView.Builder(this)
                .withMaterialShowcase()
                .setContentText(getString(R.string.refresh_info))
                .setTarget(new ToolbarActionItemTarget(toolbar, R.id.load_new_restaurant))
                .setShowcaseEventListener(
                        new SimpleShowcaseEventListener() {
                            @Override
                            public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                                showShare();
                            }
                        }
                )
                .setStyle(R.style.showcase_theme)
                .build();
    }

    private void showShare() {
        if (!hasShownShare) {
            hasShownShare = true;
            new ShowcaseView.Builder(this)
                    .withMaterialShowcase()
                    .setContentText(getString(R.string.share_info))
                    .setTarget(new ToolbarActionItemTarget(toolbar, R.id.share_restaurant))
                    .setShowcaseEventListener(
                            new SimpleShowcaseEventListener() {
                                @Override
                                public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                                    showMapInfo();
                                }
                            }
                    )
                    .setStyle(R.style.showcase_theme)
                    .build();
        }
    }

    private void showMapInfo() {
        if (!hasShownAddress) {
            hasShownAddress = true;
            new ShowcaseView.Builder(this)
                    .withMaterialShowcase()
                    .setContentText(getString(R.string.map_info))
                    .setTarget(new ViewTarget(R.id.address_icon, this))
                    .setShowcaseEventListener(
                            new SimpleShowcaseEventListener() {
                                @Override
                                public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                                    showPhoneNumberInfo();
                                }
                            }
                    )
                    .setStyle(R.style.showcase_theme)
                    .build();
        }
    }

    private void showPhoneNumberInfo() {
        if (!hasShownPhone) {
            hasShownPhone = true;
            new ShowcaseView.Builder(this)
                    .withMaterialShowcase()
                    .setContentText(getString(R.string.phone_info))
                    .setTarget(new ViewTarget(R.id.phone_icon, this))
                    .setStyle(R.style.showcase_theme)
                    .build();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.restaurant_menu, menu);
        UIUtils.loadMenuIcon(menu, R.id.share_restaurant, IoniconsIcons.ion_android_share_alt);
        UIUtils.loadMenuIcon(menu, R.id.load_new_restaurant, IoniconsIcons.ion_android_refresh);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share_restaurant:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, currentRestaurant.getShareText());
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_with)));
                return true;
            case R.id.load_new_restaurant:
                loadNewRestaurant();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
