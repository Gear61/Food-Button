package com.randomappsinc.foodbutton.Models;

import java.util.ArrayList;

/**
 * Created by alexanderchiou on 4/28/16.
 */
public class Filter {
    // Distance options
    public static final int METERS_IN_A_MILE = 1610;
    public static final int VERY_CLOSE = 1;
    public static final int CLOSE = 3;
    public static final int FAR = 5;
    public static final int VERY_FAR = 24;

    // Sort options
    public static final int RELEVANCE = 0;
    public static final int DISTANCE = 1;
    public static final int RATING = 2;

    private String searchTerm;
    private ArrayList<String> categories;
    private int radius;
    private boolean dealsOnly;
    private boolean randomizeResults;
    private int sortOption;

    public Filter() {
        this.searchTerm = "";
        this.categories = new ArrayList<>();
        this.randomizeResults = true;
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
        this.dealsOnly = false;
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

    public boolean isDealsOnly() {
        return dealsOnly;
    }

    public void setDealsOnly(boolean dealsOnly) {
        this.dealsOnly = dealsOnly;
    }

    public boolean isRandomizeResults() {
        return randomizeResults;
    }

    public void setRandomizeResults(boolean randomizeResults) {
        this.randomizeResults = randomizeResults;
    }

    public int getSortOption() {
        return sortOption;
    }

    public void setSortOption(int sortOption) {
        this.sortOption = sortOption;
    }
}
