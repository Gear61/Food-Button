package com.randomappsinc.foodbutton.API;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.randomappsinc.foodbutton.Models.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class RestaurantSearchResults {

    @SerializedName("businesses")
    @Expose
    private List<Business> businesses;

    public class Business {
        @SerializedName("id")
        @Expose
        private String id;

        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("image_url")
        @Expose
        private String imageUrl;

        @SerializedName("url")
        @Expose
        private String url;

        @SerializedName("rating")
        @Expose
        private double rating;

        @SerializedName("review_count")
        @Expose
        private int reviewCount;

        @SerializedName("display_phone")
        @Expose
        private String phoneNumber;

        @SerializedName("price")
        @Expose
        private String price;

        @SerializedName("coordinates")
        @Expose
        private Coordinates coordinates;

        class Coordinates {
            @SerializedName("latitude")
            @Expose
            private double latitude;

            @SerializedName("longitude")
            @Expose
            private double longitude;

            double getLatitude() {
                return latitude;
            }

            double getLongitude() {
                return longitude;
            }
        }

        @SerializedName("location")
        @Expose
        private Location location;

        class Location {
            @SerializedName("address1")
            @Expose
            private String address1;

            @SerializedName("city")
            @Expose
            private String city;

            String getCity() {
                return city;
            }

            String getAddress() {
                StringBuilder address = new StringBuilder();
                if (!TextUtils.isEmpty(address1)) {
                    address.append(address1).append(", ");
                }
                address.append(city);
                return address.toString();
            }
        }

        // Distance in meters from the place location
        @SerializedName("distance")
        @Expose
        private double distance;

        @SerializedName("categories")
        @Expose
        private List<Category> categories;

        class Category {
            @SerializedName("title")
            @Expose
            private String title;

            String getTitle() {
                return title;
            }
        }

        Restaurant toRestaurant() {
            Restaurant restaurant = new Restaurant();
            restaurant.setYelpId(id);
            restaurant.setName(name);
            restaurant.setImageUrl(imageUrl);
            restaurant.setMobileUrl(url);
            restaurant.setRating((float) rating);
            restaurant.setNumReviews(reviewCount);
            restaurant.setPhoneNumber(phoneNumber);
            restaurant.setCity(location.getCity());
            restaurant.setAddress(location.getAddress());
            restaurant.setAddress(location.getAddress());
            restaurant.setDistance(distance * 0.000621371);
            List<String> placeCategories = new ArrayList<>();
            for (Category category : categories) {
                placeCategories.add(category.getTitle());
            }
            restaurant.setCategories(placeCategories);
            return restaurant;
        }
    }

    public List<Restaurant> getRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>();
        for (Business business : businesses) {
            restaurants.add(business.toRestaurant());
        }
        return restaurants;
    }
}
