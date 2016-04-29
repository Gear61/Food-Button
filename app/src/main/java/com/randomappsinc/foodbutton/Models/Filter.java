package com.randomappsinc.foodbutton.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by alexanderchiou on 4/28/16.
 */
public class Filter implements Parcelable {
    private String searchTerm;
    private ArrayList<String> categories;

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

    public void processCategory(String category, boolean isChecked) {
        if (isChecked) {
            categories.add(category);
        } else {
            categories.remove(category);
        }
    }

    protected Filter(Parcel in) {
        searchTerm = in.readString();
        if (in.readByte() == 0x01) {
            categories = new ArrayList<>();
            in.readList(categories, String.class.getClassLoader());
        } else {
            categories = null;
        }
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
