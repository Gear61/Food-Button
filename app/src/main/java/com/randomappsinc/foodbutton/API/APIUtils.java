package com.randomappsinc.foodbutton.API;

import android.view.View;

import com.randomappsinc.foodbutton.Models.Filter;
import com.randomappsinc.foodbutton.Persistence.PreferencesManager;
import com.randomappsinc.foodbutton.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alexanderchiou on 3/26/16.
 */
public class APIUtils {
    public static Map<String, String> getQueryParams(String location) {
        Map<String, String> params = new HashMap<>();

        params.put("location", location);
        params.put("open_now", "true");

        Filter filter = PreferencesManager.get().getFilter();
        String searchTerm = filter.getSearchTerm().isEmpty() ? "Food" : filter.getSearchTerm();
        params.put("term", searchTerm);

        if (!filter.getCategories().isEmpty()) {
            params.put("categories", filter.getCategoriesString());
        }
        if (filter.getRadius() > 0) {
            String radius = String.valueOf(filter.getRadius() * Filter.METERS_IN_A_MILE);
            params.put("radius", radius);
        }
        if (filter.isDealsOnly()) {
            params.put("attributes", "deals");
        }
        if (!filter.isRandomizeResults()) {
            params.put("sort_by", getSort(filter.getSortOption()));
        }

        return params;
    }

    public static String getSort(int sortIndex) {
        switch (sortIndex) {
            case 1:
                return "distance";
            case 2:
                return "rating";
            default:
                return "best_match";
        }
    }

    public static String getCategoryFromId(int categoryId) {
        switch (categoryId) {
            case R.id.american:
            case R.id.american_toggle:
                return "newamerican,tradamerican";

            case R.id.chinese:
            case R.id.chinese_toggle:
                return "chinese";

            case R.id.fast_food:
            case R.id.fast_food_toggle:
                return "hotdogs";

            case R.id.french:
            case R.id.french_toggle:
                return "french";

            case R.id.indian:
            case R.id.indian_toggle:
                return "indpak";

            case R.id.italian:
            case R.id.italian_toggle:
                return "italian";

            case R.id.japanese:
            case R.id.japanese_toggle:
                return "japanese";

            case R.id.korean:
            case R.id.korean_toggle:
                return "korean";

            case R.id.mediterranean:
            case R.id.mediterranean_toggle:
                return "mediterranean";

            case R.id.middle_eastern:
            case R.id.middle_eastern_toggle:
                return "mideastern";

            case R.id.mexican:
            case R.id.mexican_toggle:
                return "mexican";

            case R.id.pizza:
            case R.id.pizza_toggle:
                return "pizza";

            case R.id.thai:
            case R.id.thai_toggle:
                return "thai";

            default:
                return "";
        }
    }

    public static int getCheckboxId(int categoryId) {
        switch (categoryId) {
            case R.id.american:
                return R.id.american_toggle;
            case R.id.chinese:
                return R.id.chinese_toggle;
            case R.id.fast_food:
                return R.id.fast_food_toggle;
            case R.id.french:
                return R.id.french_toggle;
            case R.id.indian:
                return R.id.indian_toggle;
            case R.id.italian:
                return R.id.italian_toggle;
            case R.id.japanese:
                return R.id.japanese_toggle;
            case R.id.korean:
                return R.id.korean_toggle;
            case R.id.mediterranean:
                return R.id.mediterranean_toggle;
            case R.id.middle_eastern:
                return R.id.middle_eastern_toggle;
            case R.id.mexican:
                return R.id.mexican_toggle;
            case R.id.pizza:
                return R.id.pizza_toggle;
            case R.id.thai:
                return R.id.thai_toggle;
            default:
                return 0;
        }
    }

    public static int getDistance(View view) {
        switch (view.getId()) {
            case R.id.very_close:
            case R.id.very_close_toggle:
                return Filter.VERY_CLOSE;

            case R.id.close:
            case R.id.close_toggle:
                return Filter.CLOSE;

            case R.id.far:
            case R.id.far_toggle:
                return Filter.FAR;

            case R.id.very_far:
            case R.id.very_far_toggle:
                return Filter.VERY_FAR;

            default:
                return 0;
        }
    }
}
