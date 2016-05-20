package com.randomappsinc.foodbutton.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by alexanderchiou on 4/28/16.
 */
public class Filter implements Parcelable {
    public static final int METERS_IN_A_MILE = 1610;
    public static final int VERY_CLOSE = 1;
    public static final int CLOSE = 3;
    public static final int FAR = 5;
    public static final int VERY_FAR = 24;

    private String searchTerm;
    private ArrayList<String> categories;
    private int radius;

    public Filter() {
        this.searchTerm = "";
        this.categories = new ArrayList<>();
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public boolean processCategory(String category) {
        if (!categories.contains(category)) {
            categories.add(category);
            return true;
        } else {
            categories.remove(category);
            return false;
        }
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void clear() {
        this.searchTerm = "";
        this.categories.clear();
        this.radius = 0;
    }

    public String getCategoriesString() {
        StringBuilder list = new StringBuilder();
        for (int i = 0; i < categories.size(); i++) {
            if (i != 0) {
                list.append(",");
            }
            list.append(categories.get(i));
        }
        return list.toString();
    }

    protected Filter(Parcel in) {
        searchTerm = in.readString();
        if (in.readByte() == 0x01) {
            categories = new ArrayList<>();
            in.readList(categories, String.class.getClassLoader());
        } else {
            categories = null;
        }
        radius = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(searchTerm);
        if (categories == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(categories);
        }
        dest.writeInt(radius);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Filter> CREATOR = new Parcelable.Creator<Filter>() {
        @Override
        public Filter createFromParcel(Parcel in) {
            return new Filter(in);
        }

        @Override
        public Filter[] newArray(int size) {
            return new Filter[size];
        }
    };
}
