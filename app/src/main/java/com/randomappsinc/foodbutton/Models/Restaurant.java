package com.randomappsinc.foodbutton.Models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.randomappsinc.foodbutton.Persistence.CategoryDO;
import com.randomappsinc.foodbutton.Persistence.RestaurantDO;
import com.randomappsinc.foodbutton.R;
import com.randomappsinc.foodbutton.Utils.MyApplication;
import com.randomappsinc.foodbutton.Utils.RestaurantUtils;
import com.randomappsinc.foodbutton.Utils.UIUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

public class Restaurant implements Parcelable {

    public static final String RESTAURANTS_KEY = "restaurants";
    public static final String NO_PHONE_NUMBER = "No phone number provided";
    public static final String NO_ADDRESS = "No address provided";

    private String yelpId;
    private String mobileUrl;
    private String name;
    private List<String> categories;
    private String phoneNumber;
    private String imageUrl;
    private String city;
    private String address;
    private float rating;
    private int numReviews;

    // Distance from the search location in miles
    private double distance;

    public Restaurant() {}

    public String getYelpId() {
        return yelpId;
    }

    public void setYelpId(String yelpId) {
        this.yelpId = yelpId;
    }

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
        return RestaurantUtils.getListString(categories);
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

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public String getAddressWithDistance() {
        if (distance > 0) {
            DecimalFormat formatter = new DecimalFormat("#.##");
            String formattedDistance = formatter.format(distance);
            return address + " (" + formattedDistance + MyApplication.getAppContext().getString(R.string.miles_away);
        }
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
        Context context = MyApplication.getAppContext();

        shareText.append(context.getString(R.string.name_prefix));
        shareText.append(name);
        shareText.append("\n\n");

        shareText.append(context.getString(R.string.categories_prefix));
        shareText.append(RestaurantUtils.getListString(categories));
        shareText.append("\n\n");

        shareText.append(context.getString(R.string.rating_prefix));
        shareText.append(String.format(context.getString(R.string.rating_template), rating, numReviews));
        shareText.append("\n\n");

        shareText.append(context.getString(R.string.address_prefix));
        shareText.append(address);
        shareText.append("\n\n");

        if (!phoneNumber.equals(NO_PHONE_NUMBER)) {
            shareText.append(context.getString(R.string.phone_prefix));
            shareText.append(UIUtils.humanizePhoneNumber(phoneNumber));
            shareText.append("\n\n");
        }

        shareText.append(context.getString(R.string.view_on_yelp_prefix));
        shareText.append(mobileUrl);

        return shareText.toString();
    }

    public String getCity() {
        return city;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public RestaurantDO toRestaurantDO() {
        RestaurantDO restaurantDO = new RestaurantDO();
        restaurantDO.setYelpId(yelpId);
        restaurantDO.setMobileUrl(mobileUrl);
        restaurantDO.setName(name);

        RealmList<CategoryDO> categoryDOs = new RealmList<>();
        for (String category : categories) {
            CategoryDO categoryDO = new CategoryDO();
            categoryDO.setCategory(category);
            categoryDOs.add(categoryDO);
        }
        restaurantDO.setCategories(categoryDOs);

        restaurantDO.setPhoneNumber(phoneNumber);
        restaurantDO.setImageUrl(imageUrl);
        restaurantDO.setCity(city);
        restaurantDO.setAddress(address);
        restaurantDO.setRating(rating);
        restaurantDO.setNumReviews(numReviews);
        return restaurantDO;
    }

    protected Restaurant(Parcel in) {
        yelpId = in.readString();
        mobileUrl = in.readString();
        name = in.readString();
        if (in.readByte() == 0x01) {
            categories = new ArrayList<>();
            in.readList(categories, String.class.getClassLoader());
        } else {
            categories = null;
        }
        phoneNumber = in.readString();
        imageUrl = in.readString();
        city = in.readString();
        address = in.readString();
        rating = in.readFloat();
        numReviews = in.readInt();
        distance = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(yelpId);
        dest.writeString(mobileUrl);
        dest.writeString(name);
        if (categories == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(categories);
        }
        dest.writeString(phoneNumber);
        dest.writeString(imageUrl);
        dest.writeString(city);
        dest.writeString(address);
        dest.writeFloat(rating);
        dest.writeInt(numReviews);
        dest.writeDouble(distance);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Restaurant> CREATOR = new Parcelable.Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };
}
