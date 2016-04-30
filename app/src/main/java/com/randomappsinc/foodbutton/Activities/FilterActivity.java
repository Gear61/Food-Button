package com.randomappsinc.foodbutton.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.foodbutton.API.OAuth.ApiUtils;
import com.randomappsinc.foodbutton.Models.Filter;
import com.randomappsinc.foodbutton.R;
import com.randomappsinc.foodbutton.Utils.UIUtils;
import com.rey.material.widget.CheckBox;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by alexanderchiou on 4/28/16.
 */
public class FilterActivity extends StandardActivity {
    public static String FILTER_KEY = "filter";
    public static final int[] categoryIds = new int[] {R.id.american, R.id.chinese, R.id.fast_food, R.id.french,
            R.id.indian, R.id.japanese, R.id.korean, R.id.mediterranean, R.id.middle_eastern,
            R.id.mexican, R.id.pizza, R.id.thai};

    @Bind(R.id.search_term) EditText searchInput;

    private Filter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        filter = getIntent().getParcelableExtra(FILTER_KEY);
        searchInput.setText(filter.getSearchTerm());

        for (int categoryId : categoryIds) {
            String category = ApiUtils.getCategoryFromId(categoryId);
            if (filter.getCategories().contains(category)) {
                int checkboxId = ApiUtils.getCheckboxId(categoryId);
                CheckBox checkBox = (CheckBox) findViewById(checkboxId);
                if (checkBox != null) {
                    checkBox.setCheckedImmediately(true);
                }
            }
        }
    }

    @OnClick(R.id.clear_search)
    public void clearSearch() {
        searchInput.setText("");
    }

    @OnClick({R.id.american, R.id.chinese, R.id.fast_food, R.id.french, R.id.indian, R.id.japanese, R.id.korean,
              R.id.mediterranean, R.id.middle_eastern, R.id.mexican, R.id.pizza, R.id.thai})
    public void categoryClicked(View view) {
        int checkboxId = ApiUtils.getCheckboxId(view.getId());
        CheckBox checkBox = (CheckBox) findViewById(checkboxId);
        processClick(ApiUtils.getCategoryFromId(view.getId()), checkBox);
    }

    @OnClick({R.id.american_toggle, R.id.chinese_toggle, R.id.fast_food_toggle, R.id.french_toggle,
            R.id.indian_toggle, R.id.japanese_toggle, R.id.korean_toggle, R.id.mediterranean_toggle,
            R.id.middle_eastern_toggle, R.id.mexican_toggle, R.id.pizza_toggle, R.id.thai_toggle})
    public void checkboxClicked(View view) {
        filter.processCategory(ApiUtils.getCategoryFromId(view.getId()));
    }

    private void processClick(String category, CheckBox checkBox) {
        if (filter.processCategory(category)) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
    }

    private void clearFilters() {
        searchInput.setText("");

        for (int categoryId : categoryIds) {
            int checkboxId = ApiUtils.getCheckboxId(categoryId);
            CheckBox checkBox = (CheckBox) findViewById(checkboxId);
            if (checkBox != null) {
                checkBox.setCheckedImmediately(false);
            }
        }

        filter.clear();
        Intent returnIntent = new Intent();
        returnIntent.putExtra(FILTER_KEY, filter);
        setResult(RESULT_OK, returnIntent);
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
                filter.setSearchTerm(searchInput.getText().toString().trim());
                Intent returnIntent = new Intent();
                returnIntent.putExtra(FILTER_KEY, filter);
                setResult(RESULT_OK, returnIntent);
                finish();
                return true;
            case R.id.clear_filters:
                new MaterialDialog.Builder(this)
                        .title(R.string.clear_confirmation)
                        .content(R.string.remove_all_filters)
                        .positiveText(android.R.string.yes)
                        .negativeText(android.R.string.no)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                clearFilters();
                            }
                        })
                        .show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
