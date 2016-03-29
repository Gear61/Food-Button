package com.randomappsinc.foodbutton.Utils;

import android.widget.ImageView;

import com.randomappsinc.foodbutton.R;

import java.util.List;

/**
 * Created by alexanderchiou on 3/28/16.
 */
public class RatingUtils {
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
}
