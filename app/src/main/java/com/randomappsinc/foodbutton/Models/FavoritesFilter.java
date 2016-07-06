package com.randomappsinc.foodbutton.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexanderchiou on 7/3/16.
 */
public class FavoritesFilter implements Parcelable {
    private List<String> categories;
    private List<String> cities;

    public FavoritesFilter() {
        this.categories = new ArrayList<>();
        this.cities = new ArrayList<>();
    }

    public FavoritesFilter(List<String> categories, List<String> cities) {
        this.categories = categories;
        this.cities = cities;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }

    public void clear() {
        categories.clear();
        cities.clear();
    }

    protected FavoritesFilter(Parcel in) {
        if (in.readByte() == 0x01) {
            categories = new ArrayList<>();
            in.readList(categories, String.class.getClassLoader());
        } else {
            categories = null;
        }
        if (in.readByte() == 0x01) {
            cities = new ArrayList<>();
            in.readList(cities, String.class.getClassLoader());
        } else {
            cities = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (categories == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(categories);
        }
        if (cities == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(cities);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FavoritesFilter> CREATOR = new Parcelable.Creator<FavoritesFilter>() {
        @Override
        public FavoritesFilter createFromParcel(Parcel in) {
            return new FavoritesFilter(in);
        }

        @Override
        public FavoritesFilter[] newArray(int size) {
            return new FavoritesFilter[size];
        }
    };
}
