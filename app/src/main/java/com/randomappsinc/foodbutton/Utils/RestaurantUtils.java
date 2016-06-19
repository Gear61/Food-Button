package com.randomappsinc.foodbutton.Utils;

import android.widget.ImageView;

import com.randomappsinc.foodbutton.API.Models.Business;
import com.randomappsinc.foodbutton.Models.Restaurant;
import com.randomappsinc.foodbutton.Persistence.CategoryDO;
import com.randomappsinc.foodbutton.Persistence.RestaurantDO;
import com.randomappsinc.foodbutton.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexanderchiou on 3/28/16.
 */
public class RestaurantUtils {
    public static void loadStarImages(List<ImageView> starPictures, float rating) {
        int[] starIds = getStarIds(rating);
        for (int i = 0; i < 5; i++) {
            starPictures.get(i).setImageResource(starIds[i]);
        }
    }

    public static int[] getStarIds(float rating) {
        int[] starIds = new int[5];

        boolean needsHalfStar = rating % 1 != 0;
        for (int i = 0; i < 5; i++) {
            if (needsHalfStar) {
                if (i + 1 > rating) {
                    starIds[i] = getHalfStar(rating);
                    needsHalfStar = false;
                }
                else {
                    starIds[i] = getFilledStar(rating);
                }
            }
            else {
                if (i < rating) {
                    starIds[i] = getFilledStar(rating);
                }
                else {
                    starIds[i] = R.drawable.empty_star;
                }
            }
        }
        return starIds;
    }

    public static int getFilledStar(float rating) {
        if (rating < 2) {
            return R.drawable.one_star;
        }
        if (rating < 3) {
            return R.drawable.two_star;
        }
        if (rating < 4) {
            return R.drawable.three_star;
        }
        if (rating < 5) {
            return R.drawable.four_star;
        }
        return R.drawable.five_star;
    }

    public static int getHalfStar(float rating) {
        if (rating < 2) {
            return R.drawable.half_star_1;
        }
        if (rating < 3) {
            return R.drawable.half_star_2;
        }
        if (rating < 4) {
            return R.drawable.half_star_3;
        }
        return R.drawable.half_star_4;
    }

    public static Restaurant extractFromDO(RestaurantDO restaurantDO) {
        Restaurant restaurant = new Restaurant();

        restaurant.setYelpId(restaurantDO.getYelpId());
        restaurant.setMobileUrl(restaurantDO.getMobileUrl());
        restaurant.setName(restaurantDO.getName());

        List<String> categories = new ArrayList<>();
        for (CategoryDO categoryDO : restaurantDO.getCategories()) {
            categories.add(categoryDO.getCategory());
        }
        restaurant.setCategories(categories);

        restaurant.setPhoneNumber(restaurantDO.getPhoneNumber());
        restaurant.setImageUrl(restaurantDO.getImageUrl());
        restaurant.setCity(restaurantDO.getCity());
        restaurant.setAddress(restaurantDO.getAddress());
        restaurant.setRating(restaurantDO.getRating());
        restaurant.setNumReviews(restaurantDO.getNumReviews());
        restaurant.setSnippetText(restaurantDO.getSnippetText());

        return restaurant;
    }

    public static String getNumberDisplay(Restaurant restaurant) {
        String restaurantNumber = restaurant.getPhoneNumber();
        if (!restaurantNumber.equals(Business.NO_PHONE_NUMBER)) {
            restaurantNumber = MyApplication.getAppContext().getString(R.string.call)
                    + UIUtils.humanizePhoneNumber(restaurantNumber);
        }
        return restaurantNumber;
    }
}
