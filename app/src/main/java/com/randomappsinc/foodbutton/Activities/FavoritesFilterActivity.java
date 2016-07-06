package com.randomappsinc.foodbutton.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.foodbutton.Models.FavoritesFilter;
import com.randomappsinc.foodbutton.R;
import com.randomappsinc.foodbutton.Utils.RestaurantUtils;
import com.randomappsinc.foodbutton.Utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by alexanderchiou on 7/3/16.
 */
public class FavoritesFilterActivity extends StandardActivity {
    public static final int CATEGORIES_REQUEST = 1;
    public static final int CITIES_REQUEST = 2;

    @Bind(R.id.categories_input) EditText categoriesInput;
    @Bind(R.id.cities_input) EditText citiesInput;

    private FavoritesFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites_filter);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        filter = getIntent().getParcelableExtra(FilterActivity.FILTER_KEY);
    }

    private void setInputs() {
        categoriesInput.setText(RestaurantUtils.getListString(filter.getCategories()));
        citiesInput.setText(RestaurantUtils.getListString(filter.getCities()));
    }

    @OnClick(R.id.categories_input)
    public void chooseCategories() {
        Intent intent = new Intent(this, OptionsActivity.class);
        intent.putExtra(OptionsActivity.MODE_KEY, CATEGORIES_REQUEST);
        intent.putExtra(FilterActivity.FILTER_KEY, filter);
        startActivityForResult(intent, CATEGORIES_REQUEST);
    }

    @OnClick(R.id.cities_input)
    public void chooseCities() {
        Intent intent = new Intent(this, OptionsActivity.class);
        intent.putExtra(OptionsActivity.MODE_KEY, CITIES_REQUEST);
        intent.putExtra(FilterActivity.FILTER_KEY, filter);
        startActivityForResult(intent, CATEGORIES_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            filter = data.getParcelableExtra(FilterActivity.FILTER_KEY);
            setInputs();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_menu, menu);
        UIUtils.loadMenuIcon(menu, R.id.apply_filters, IoniconsIcons.ion_checkmark);
        UIUtils.loadMenuIcon(menu, R.id.clear_filters, IoniconsIcons.ion_android_close);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.apply_filters:
                setResult(RESULT_OK);
                finish();
                return true;
            case R.id.clear_filters:
                new MaterialDialog.Builder(this)
                        .title(R.string.clear_confirmation)
                        .content(R.string.remove_all_filters)
                        .positiveText(android.R.string.yes)
                        .negativeText(android.R.string.no)
                        .show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
