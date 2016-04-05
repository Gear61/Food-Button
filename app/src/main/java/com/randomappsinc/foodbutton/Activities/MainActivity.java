package com.randomappsinc.foodbutton.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.foodbutton.API.OAuth.ApiUtils;
import com.randomappsinc.foodbutton.API.RestClient;
import com.randomappsinc.foodbutton.API.SearchCallback;
import com.randomappsinc.foodbutton.R;
import com.randomappsinc.foodbutton.Utils.LocationUtils;
import com.randomappsinc.foodbutton.Utils.PermissionUtils;
import com.randomappsinc.foodbutton.Utils.PreferencesManager;
import com.randomappsinc.foodbutton.Utils.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends StandardActivity {
    public static final int LOCATION_REQUEST_CODE = 1;

    @Bind(R.id.parent) View parent;
    @Bind(R.id.food_button) FloatingActionButton foodButton;

    private MaterialDialog fetchingSuggestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLayout(getResources().getConfiguration().orientation);
        EventBus.getDefault().register(this);

        fetchingSuggestion = new MaterialDialog.Builder(this)
                .content(R.string.finding_you_food)
                .progress(true, 0)
                .cancelable(false)
                .build();

        if (PreferencesManager.get().shouldAskToRate()) {
            showPleaseRateDialog();
        }
    }

    private void loadLayout(int currentOrientation) {
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main_landscape);
        } else if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_main_portrait);
        }
        ButterKnife.bind(this);
        foodButton.setImageDrawable(new IconDrawable(this, IoniconsIcons.ion_android_restaurant)
                .colorRes(R.color.white));
    }

    private void showPleaseRateDialog() {
        new MaterialDialog.Builder(this)
                .content(R.string.please_rate)
                .negativeText(R.string.no_im_good)
                .positiveText(R.string.will_rate)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Uri uri =  Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        if (!(getPackageManager().queryIntentActivities(intent, 0).size() > 0)) {
                            UIUtils.showSnackbar(parent, getString(R.string.play_store_error));
                            return;
                        }
                        startActivity(intent);
                    }
                })
                .show();
    }

    @OnClick(R.id.food_button)
    public void findFood() {
        foodButton.setEnabled(false);
        if (PermissionUtils.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            String defaultLocation = PreferencesManager.get().getDefaultLocation();
            if (defaultLocation.equals(getString(R.string.automatic))) {
                String currentLocation = LocationUtils.getCurrentAddress();
                if (currentLocation.isEmpty()) {
                    new MaterialDialog.Builder(this)
                            .content(R.string.auto_location_fail)
                            .positiveText(android.R.string.yes)
                            .show();
                } else {
                    fetchSuggestions(currentLocation);
                }
            } else {
                fetchSuggestions(defaultLocation);
            }
        } else {
            PermissionUtils.requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_REQUEST_CODE);
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
        new MaterialDialog.Builder(this)
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

    private void chooseDefaultLocation() {
        new MaterialDialog.Builder(this)
                .title(R.string.choose_default_location)
                .content(R.string.default_instructions)
                .items(PreferencesManager.get().getLocationsArray())
                .itemsCallbackSingleChoice(PreferencesManager.get().getCurrentLocationIndex(),
                        new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                PreferencesManager.get().setDefaultLocation(text.toString());
                                UIUtils.showSnackbar(parent, getString(R.string.default_location_set));
                                return true;
                            }
                        })
                .positiveText(R.string.choose)
                .negativeText(android.R.string.cancel)
                .neutralText(R.string.add_location)
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        addLocation();
                    }
                })
                .show();
    }

    private void addLocation() {
        new MaterialDialog.Builder(this)
                .title(R.string.add_location_title)
                .input(getString(R.string.location), "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, @NonNull CharSequence input) {
                        String currentInput = input.toString().trim();
                        dialog.getActionButton(DialogAction.POSITIVE)
                                .setEnabled(!PreferencesManager.get().alreadyHasLocation(input.toString().trim())
                                && !currentInput.isEmpty());
                    }
                })
                .alwaysCallInputCallback()
                .positiveText(R.string.add)
                .negativeText(android.R.string.no)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String newLocation = dialog.getInputEditText().getText().toString();
                        PreferencesManager.get().addSavedLocation(newLocation);
                        UIUtils.showSnackbar(parent, getString(R.string.location_added));
                    }
                })
                .show();
    }

    public void fetchSuggestions(String location) {
        fetchingSuggestion.show();
        RestClient.get().getYelpService()
                .doSearch(ApiUtils.getSearchQueryMap(location))
                .enqueue(new SearchCallback(1, location));
    }

    @Subscribe
    public void onEvent(String event) {
        fetchingSuggestion.dismiss();
        switch (event) {
            case SearchCallback.RESTAURANTS_FETCHED:
                Intent loadRestaurant = new Intent(this, RestaurantActivity.class);
                loadRestaurant.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(loadRestaurant);
                break;
            case SearchCallback.NO_RESTAURANTS:
                UIUtils.showSnackbar(parent, getString(R.string.no_restaurants));
                break;
            case SearchCallback.SEARCH_FAIL:
                UIUtils.showSnackbar(parent, getString(R.string.search_fail));
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        loadLayout(newConfig.orientation);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        UIUtils.loadMenuIcon(menu, R.id.set_default_location, IoniconsIcons.ion_android_map);
        UIUtils.loadMenuIcon(menu, R.id.settings, IoniconsIcons.ion_android_settings);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.set_default_location:
                chooseDefaultLocation();
                return true;
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
