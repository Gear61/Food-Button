package com.randomappsinc.foodbutton.API.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.randomappsinc.foodbutton.Models.Restaurant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexanderchiou on 3/26/16.
 */

public class Business {
    public static final String NO_PHONE_NUMBER = "No phone number provided";
    public static final String NO_ADDRESS = "No address provided";

    @SerializedName("id")
    @Expose
    private String yelpId;

    @SerializedName("mobile_url")
    @Expose
    private String mobileUrl;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("categories")
    @Expose
    private List<List<String>> categories = new ArrayList<>();

    @SerializedName("phone")
    @Expose
    private String phoneNumber;

    @SerializedName("image_url")
    @Expose
    private String imageUrl;

    @SerializedName("location")
    @Expose
    private Location location;

    @SerializedName("rating")
    @Expose
    private float rating;

    @SerializedName("review_count")
    @Expose
    private int numReviews;

    @SerializedName("is_closed")
    @Expose
    private boolean isClosed;

    public boolean isClosed() {
        return isClosed;
    }

    public Restaurant toRestaurant() {
        Restaurant restaurant = new Restaurant();
        restaurant.setYelpId(yelpId);
        restaurant.setMobileUrl(mobileUrl);
        restaurant.setName(name);

        List<String> parsedCategories = new ArrayList<>();
        if (categories != null) {
            for (List<String> category : categories) {
                if (!category.isEmpty()) {
                    parsedCategories.add(category.get(0));
                }
            }
        }
        restaurant.setCategories(parsedCategories);

        restaurant.setPhoneNumber(phoneNumber == null ? NO_PHONE_NUMBER : phoneNumber);
        restaurant.setImageUrl(imageUrl);

        restaurant.setCity(location.getCity());
        List<String> displayAddress = location.getDisplayAddress();
        if (displayAddress != null) {
            StringBuilder addressText = new StringBuilder();
            for (int i = 0; i < displayAddress.size(); i++) {
                if (i != 0) {
                    addressText.append(", ");
                }
                addressText.append(displayAddress.get(i));
            }
            String apiAddress = addressText.toString();
            if (!apiAddress.isEmpty()) {
                restaurant.setAddress(apiAddress);
            }
            else {
                restaurant.setAddress(NO_ADDRESS);
            }
        }

        restaurant.setRating(rating);
        restaurant.setNumReviews(numReviews);

        return restaurant;
    }

    public class Location {
        @SerializedName("city")
        @Expose
        private String city;

        @SerializedName("display_address")
        @Expose
        private List<String> displayAddress = new ArrayList<>();

        public String getCity() {
            return city;
        }

        public List<String> getDisplayAddress() {
            return displayAddress;
        }
    }
}