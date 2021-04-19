package com.randomappsinc.foodbutton.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.foodbutton.Models.FavoritesFilter;
import com.randomappsinc.foodbutton.Persistence.DatabaseManager;
import com.randomappsinc.foodbutton.R;
import com.randomappsinc.foodbutton.Utils.RestaurantUtils;
import com.randomappsinc.foodbutton.Utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FavoritesFilterActivity extends StandardActivity {

    @BindView(R.id.categories_input) EditText categoriesInput;
    @BindView(R.id.cities_input) EditText citiesInput;

    private FavoritesFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites_filter);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        filter = getIntent().getParcelableExtra(FilterActivity.FILTER_KEY);
        setInputs();
    }

    private void setInputs() {
        categoriesInput.setText(RestaurantUtils.getListString(filter.getCategories()));
        citiesInput.setText(RestaurantUtils.getListString(filter.getCities()));
    }

    @OnClick(R.id.categories_input)
    public void chooseCategories() {
        List<String> allCategories = DatabaseManager.get().getCategories();
        Integer[] alreadyChosen = new Integer[filter.getCategories().size()];
        for (int i = 0; i < filter.getCategories().size(); i++) {
            alreadyChosen[i] = allCategories.indexOf(filter.getCategories().get(i));
        }

        new MaterialDialog.Builder(this)
                .title(R.string.categories)
                .items(allCategories)
                .itemsCallbackMultiChoice(alreadyChosen, (dialog, which, text) -> {
                    List<String> categories = new ArrayList<>();
                    for (CharSequence choice : text) {
                        categories.add(choice.toString());
                    }
                    filter.setCategories(categories);
                    setInputs();
                    return true;
                })
                .positiveText(R.string.choose)
                .negativeText(android.R.string.cancel)
                .show();
    }

    @OnClick(R.id.cities_input)
    public void chooseCities() {
        List<String> allCities = DatabaseManager.get().getCities();
        Integer[] alreadyChosen = new Integer[filter.getCities().size()];
        for (int i = 0; i < filter.getCities().size(); i++) {
            alreadyChosen[i] = allCities.indexOf(filter.getCities().get(i));
        }

        new MaterialDialog.Builder(this)
                .title(R.string.cities)
                .items(allCities)
                .itemsCallbackMultiChoice(alreadyChosen, (dialog, which, text) -> {
                    List<String> cities = new ArrayList<>();
                    for (CharSequence choice : text) {
                        cities.add(choice.toString());
                    }
                    filter.setCities(cities);
                    setInputs();
                    return true;
                })
                .positiveText(R.string.choose)
                .negativeText(android.R.string.cancel)
                .show();
    }

    private void setFilter() {
        Intent intent = new Intent();
        intent.putExtra(FilterActivity.FILTER_KEY, filter);
        setResult(RESULT_OK, intent);
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
                setFilter();
                finish();
                return true;
            case R.id.clear_filters:
                new MaterialDialog.Builder(this)
                        .title(R.string.clear_confirmation)
                        .content(R.string.remove_all_filters)
                        .positiveText(android.R.string.yes)
                        .negativeText(android.R.string.no)
                        .onPositive((dialog, which) -> {
                            filter.clear();
                            setInputs();
                            setFilter();
                        })
                        .show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
