package com.randomappsinc.foodbutton.Restaurant;

import com.randomappsinc.foodbutton.API.Models.Business;
import com.randomappsinc.foodbutton.R;
import com.randomappsinc.foodbutton.Utils.MyApplication;
import com.randomappsinc.foodbutton.Utils.UIUtils;

import java.util.List;

/**
 * Created by alexanderchiou on 3/27/16.
 */
public class Restaurant {
    private String mobileUrl;
    private String name;
    private List<String> categories;
    private String phoneNumber;
    private String imageUrl;
    private String address;
    private float rating;
    private int numReviews;

    public String getMobileUrl() {
        return mobileUrl;
    }

    public void setMobileUrl(String mobileUrl) {
        this.mobileUrl = mobileUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategories() {
        StringBuilder categoriesString = new StringBuilder();
        for (int i = 0; i < categories.size(); i++) {
            if (i != 0) {
                categoriesString.append(", ");
            }
            categoriesString.append(categories.get(i));
        }
        return categoriesString.toString();
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getNumReviews() {
        return numReviews;
    }

    public void setNumReviews(int numReviews) {
        this.numReviews = numReviews;
    }

    public String getShareText() {
        StringBuilder shareText = new StringBuilder();

        shareText.append(MyApplication.getAppContext().getString(R.string.name_prefix));
        shareText.append(name);
        shareText.append("\n\n");

        shareText.append(MyApplication.getAppContext().getString(R.string.address_prefix));
        shareText.append(address);

        if (!phoneNumber.equals(Business.NO_PHONE_NUMBER)) {
            shareText.append("\n\n");
            shareText.append(MyApplication.getAppContext().getString(R.string.phone_prefix));
            shareText.append(UIUtils.humanizePhoneNumber(phoneNumber));
        }

        return shareText.toString();
    }
}