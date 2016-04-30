package com.randomappsinc.foodbutton.Fragments;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.foodbutton.API.OAuth.ApiUtils;
import com.randomappsinc.foodbutton.API.RestClient;
import com.randomappsinc.foodbutton.API.SearchCallback;
import com.randomappsinc.foodbutton.Activities.MainActivity;
import com.randomappsinc.foodbutton.Activities.SuggestionsActivity;
import com.randomappsinc.foodbutton.Models.Filter;
import com.randomappsinc.foodbutton.Persistence.PreferencesManager;
import com.randomappsinc.foodbutton.R;
import com.randomappsinc.foodbutton.Utils.LocationUtils;
import com.randomappsinc.foodbutton.Utils.PermissionUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by alexanderchiou on 4/14/16.
 */
public class FoodButtonFragment extends Fragment {
    public static final int LOCATION_REQUEST_CODE = 1;

    @Bind(R.id.food_button) FloatingActionButton foodButton;

    private MaterialDialog fetchingSuggestion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.food_button, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);

        foodButton.setImageDrawable(new IconDrawable(getActivity(), IoniconsIcons.ion_android_restaurant)
                .colorRes(R.color.white));
        EventBus.getDefault().register(this);

        fetchingSuggestion = new MaterialDialog.Builder(getActivity())
                .content(R.string.finding_you_food)
                .progress(true, 0)
                .cancelable(false)
                .build();

        return rootView;
    }

    @OnClick(R.id.food_button)
    public void findFood() {
        foodButton.setEnabled(false);
        if (PermissionUtils.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            String defaultLocation = PreferencesManager.get().getDefaultLocation();
            if (defaultLocation.equals(getString(R.string.automatic))) {
                if (LocationUtils.isLocationEnabled()) {
                    String currentLocation = LocationUtils.getCurrentAddress();
                    if (currentLocation.isEmpty()) {
                        new MaterialDialog.Builder(getActivity())
                                .content(R.string.auto_location_fail)
                                .positiveText(android.R.string.yes)
                                .show();
                    } else {
                        fetchSuggestions(currentLocation);
                    }
                } else {
                    showLocationServicesDialog();
                }
            } else {
                fetchSuggestions(defaultLocation);
            }
        } else {
            PermissionUtils.requestPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION,
                    LOCATION_REQUEST_CODE);
        }
        foodButton.setEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    findFood();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showLocationServicesDialog() {
        new MaterialDialog.Builder(getActivity())
                .content(R.string.location_services_needed)
                .negativeText(android.R.string.cancel)
                .positiveText(android.R.string.yes)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .show();
    }

    public void fetchSuggestions(String location) {
        fetchingSuggestion.show();
        Filter filter = ((MainActivity) getActivity()).getFilter();
        RestClient.get().getYelpService()
                .doSearch(ApiUtils.getSearchQueryMap(location, filter))
                .enqueue(new SearchCallback(1, location, filter));
    }

    private void showSnackbar(String message) {
        ((MainActivity) getActivity()).showSnackbar(message);
    }

    @Subscribe
    public void onEvent(String event) {
        fetchingSuggestion.dismiss();
        switch (event) {
            case SearchCallback.RESTAURANTS_FETCHED:
                Intent loadRestaurant = new Intent(getActivity(), SuggestionsActivity.class);
                loadRestaurant.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                getActivity().startActivity(loadRestaurant);
                break;
            case SearchCallback.NO_RESTAURANTS:
                showSnackbar(getString(R.string.no_restaurants));
                break;
            case SearchCallback.SEARCH_FAIL:
                showSnackbar(getString(R.string.search_fail));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        ButterKnife.unbind(this);
    }
}
