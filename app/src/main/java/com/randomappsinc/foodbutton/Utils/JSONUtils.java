package com.randomappsinc.foodbutton.Utils;

import com.randomappsinc.foodbutton.Models.Filter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alexanderchiou on 6/27/16.
 */
public class JSONUtils {
    public static final String SEARCH_TERM_KEY = "searchTerm";
    public static final String CATEGORIES_KEY = "categories";
    public static final String RADIUS_KEY = "radius";
    public static final String DEALS_ONLY_KEY = "dealsOnly";
    public static final String RANDOMIZE_RESULTS_KEY = "randomizeResults";
    public static final String SORT_OPTION_KEY = "sortOption";

    // Given a filter, converts it to a JSON and stringifies it
    public static String serializeFilter(Filter filter) {
        try {
            JSONObject filterJson = new JSONObject();
            filterJson.put(SEARCH_TERM_KEY, filter.getSearchTerm());

            // Categories
            JSONArray categoriesArray = new JSONArray();
            for (String category : filter.getCategories()) {
                categoriesArray.put(category);
            }
            filterJson.put(CATEGORIES_KEY, categoriesArray);

            filterJson.put(RADIUS_KEY, filter.getRadius());
            filterJson.put(DEALS_ONLY_KEY, filter.isDealsOnly());
            filterJson.put(RANDOMIZE_RESULTS_KEY, filter.isRandomizeResults());
            filterJson.put(SORT_OPTION_KEY, filter.getSortOption());
            return filterJson.toString();
        } catch (JSONException e) {
            return "";
        }
    }

    // Given a serialized JSON "cache" string of filter, extracts it into an object
    public static Filter extractFilter(String cachedList) {
        Filter filter = new Filter();
        try {
            JSONObject filterJson = new JSONObject(cachedList);

            filter.setSearchTerm(filterJson.getString(SEARCH_TERM_KEY));

            JSONArray categoriesArray = filterJson.getJSONArray(CATEGORIES_KEY);
            for (int i = 0; i < categoriesArray.length(); i++) {
                filter.processCategory(categoriesArray.getString(i));
            }

            filter.setRadius(filterJson.getInt(RADIUS_KEY));
            filter.setDealsOnly(filterJson.getBoolean(DEALS_ONLY_KEY));
            filter.setRandomizeResults(filterJson.getBoolean(RANDOMIZE_RESULTS_KEY));
            filter.setSortOption(filterJson.getInt(SORT_OPTION_KEY));
        } catch (JSONException ignored) {}
        return filter;
    }
}
