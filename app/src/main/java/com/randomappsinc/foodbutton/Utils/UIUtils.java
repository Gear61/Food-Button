package com.randomappsinc.foodbutton.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.joanzapata.iconify.Icon;
import com.joanzapata.iconify.IconDrawable;
import com.randomappsinc.foodbutton.Persistence.PreferencesManager;
import com.randomappsinc.foodbutton.R;

/**
 * Created by alexanderchiou on 3/27/16.
 */
public class UIUtils {
    public static void showSnackbar(View parent, String message) {
        Context context = MyApplication.getAppContext();
        Snackbar snackbar = Snackbar.make(parent, message, Snackbar.LENGTH_LONG);
        View rootView = snackbar.getView();
        snackbar.getView().setBackgroundColor(context.getResources().getColor(R.color.app_red));
        TextView tv = (TextView) rootView.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snackbar.show();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void loadMenuIcon(Menu menu, int itemId, Icon icon) {
        menu.findItem(itemId).setIcon(
                new IconDrawable(MyApplication.getAppContext(), icon)
                        .colorRes(R.color.white)
                        .actionBarSize());
    }

    // Given a "sanitized" phone # (only digits with country code), turn it back to human-readable format
    // Example: +1 (510) 449-4353
    public static String humanizePhoneNumber(String phoneNumber) {
        if (phoneNumber.length() < 10 || (!phoneNumber.matches("[0-9]+")) || phoneNumber.length() > 11) {
            return phoneNumber;
        }
        if (phoneNumber.length() == 11) {
            String countryCode = "+" + phoneNumber.charAt(0);
            String areaCode = " (" + phoneNumber.substring(1, 4) + ") ";
            String numberPart1 = phoneNumber.substring(4, 7) + "-";
            String numberPart2 = phoneNumber.substring(7, 11);
            return countryCode + areaCode + numberPart1 + numberPart2;
        }
        else {
            String areaCode = "(" + phoneNumber.substring(0, 3) + ") ";
            String numberPart1 = phoneNumber.substring(3, 6) + "-";
            String numberPart2 = phoneNumber.substring(6, 10);
            return areaCode + numberPart1 + numberPart2;
        }
    }

    public static void showAddedSnackbar(final String location, final View parent) {
        final Context context = MyApplication.getAppContext();
        Snackbar snackbar = Snackbar.make(parent, context.getString(R.string.location_added), Snackbar.LENGTH_INDEFINITE);
        View rootView = snackbar.getView();
        snackbar.getView().setBackgroundColor(context.getResources().getColor(R.color.app_red));
        TextView tv = (TextView) rootView.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.setAction(android.R.string.yes, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferencesManager.get().setDefaultLocation(location);
                showSnackbar(parent, context.getString(R.string.default_location_set));
            }
        });
        snackbar.show();
    }
}
