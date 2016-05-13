package com.randomappsinc.foodbutton.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.randomappsinc.foodbutton.API.Models.Business;
import com.randomappsinc.foodbutton.Models.Restaurant;
import com.randomappsinc.foodbutton.R;
import com.randomappsinc.foodbutton.Utils.RestaurantUtils;
import com.randomappsinc.foodbutton.Utils.UIUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by alexanderchiou on 4/9/16.
 */
public class RestaurantFragment extends Fragment {
    public static final String RESTAURANT_KEY = "restaurant";

    public static RestaurantFragment create(Restaurant restaurant) {
        RestaurantFragment restaurantFragment = new RestaurantFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(RESTAURANT_KEY, restaurant);
        restaurantFragment.setArguments(bundle);

        return restaurantFragment;
    }

    @Bind(R.id.restaurant_picture) ImageView restaurantPicture;
    @Bind(R.id.restaurant_name) TextView restaurantName;
    @Bind(R.id.categories) TextView categories;
    @Bind({R.id.first_star, R.id.second_star, R.id.third_star,
           R.id.fourth_star, R.id.fifth_star}) List<ImageView> starViews;
    @Bind(R.id.num_reviews) TextView numReviews;
    @Bind(R.id.address) TextView address;
    @Bind(R.id.phone_number) TextView phoneNumber;
    @Bind(R.id.what_people_saying) TextView whatPeopleSaying;
    @Bind(R.id.snippet_text) TextView snippetText;

    private Restaurant currentRestaurant;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.restaurant, container, false);
        ButterKnife.bind(this, rootView);

        currentRestaurant = getArguments().getParcelable(RESTAURANT_KEY);
        loadRestaurant();

        return rootView;
    }

    private void loadRestaurant() {
        Picasso.with(getActivity()).load(currentRestaurant.getImageUrl()).into(restaurantPicture);
        restaurantName.setText(currentRestaurant.getName());
        categories.setText(currentRestaurant.getCategories());

        RestaurantUtils.loadStarImages(starViews, currentRestaurant.getRating());
        String numReviewsText = String.format(getString(R.string.num_reviews), currentRestaurant.getNumReviews());
        numReviews.setText(numReviewsText);

        address.setText(currentRestaurant.getAddressWithDistance());
        phoneNumber.setText(UIUtils.humanizePhoneNumber(currentRestaurant.getPhoneNumber()));

        if (currentRestaurant.getSnippetText().isEmpty()) {
            whatPeopleSaying.setVisibility(View.GONE);
            snippetText.setVisibility(View.GONE);
        } else {
            snippetText.setText(currentRestaurant.getSnippetText());
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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
