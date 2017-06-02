package com.randomappsinc.foodbutton.API.OAuth;

import android.util.Base64;
import android.view.View;

import com.randomappsinc.foodbutton.API.ApiConstants;
import com.randomappsinc.foodbutton.API.Models.SearchResponse;
import com.randomappsinc.foodbutton.API.RestClient;
import com.randomappsinc.foodbutton.Models.Filter;
import com.randomappsinc.foodbutton.R;

import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;

/**
 * Created by alexanderchiou on 3/26/16.
 */
public class ApiUtils {
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
