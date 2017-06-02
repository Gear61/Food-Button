package com.randomappsinc.foodbutton.API.Models;

import com.randomappsinc.foodbutton.Models.Restaurant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexanderchiou on 3/26/16.
 */

public class Business {
    public static final String NO_PHONE_NUMBER = "No phone number provided";
    public static final String NO_ADDRESS = "No address provided";

    private String yelpId;
    private String mobileUrl;
    private String name;
    private List<List<String>> categories = new ArrayList<>();
    private String phoneNumber;
    private String imageUrl;
    private Location location;
    private float rating;
    private int numReviews;
    private boolean isClosed;
    private String snippetText;
    private double distance;
    private List<Deal> deals = new ArrayList<>();

    public class Deal {
        private String title;

        public String getTitle() {
            return title;
        }
    }

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
        restaurant.setSnippetText(snippetText);
        restaurant.setDistance(distance * 0.000621371);

        if (deals != null && !deals.isEmpty()) {
            restaurant.setCurrentDeal(deals.get(0).getTitle());
        }

        return restaurant;
    }

    public class Location {
        private String city;
        private List<String> displayAddress = new ArrayList<>();

        public String getCity() {
            return city;
        }

        public List<String> getDisplayAddress() {
            return displayAddress;
        }
    }
}