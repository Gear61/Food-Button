package com.randomappsinc.foodbutton.Activities;

import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.foodbutton.API.APIUtils;
import com.randomappsinc.foodbutton.Models.Filter;
import com.randomappsinc.foodbutton.Persistence.PreferencesManager;
import com.randomappsinc.foodbutton.R;
import com.randomappsinc.foodbutton.Utils.UIUtils;
import com.rey.material.widget.CheckBox;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class FilterActivity extends StandardActivity {

    public static String FILTER_KEY = "filter";
    public static final int[] categoryIds = new int[] {R.id.american, R.id.chinese, R.id.fast_food, R.id.french,
            R.id.indian, R.id.italian, R.id.japanese, R.id.korean, R.id.mediterranean, R.id.middle_eastern,
            R.id.mexican, R.id.pizza, R.id.thai};

    @BindView(R.id.parent) View parent;
    @BindView(R.id.search_term) EditText searchInput;
    @BindView(R.id.clear_search) View clearSearch;
    @BindView(R.id.current_location) TextView currentLocation;

    // Distance options
    @BindView(R.id.very_close_toggle) CheckBox veryCloseToggle;
    @BindView(R.id.close_toggle) CheckBox closeToggle;
    @BindView(R.id.far_toggle) CheckBox farToggle;
    @BindView(R.id.very_far_toggle) CheckBox veryFarToggle;

    // Sort options
    @BindView(R.id.relevance_toggle) CheckBox relevanceToggle;
    @BindView(R.id.distance_toggle) CheckBox distanceToggle;
    @BindView(R.id.rating_toggle) CheckBox ratingToggle;

    // Additional settings
    @BindView(R.id.deals_toggle) CheckBox dealsToggle;
    @BindView(R.id.random_toggle) CheckBox randomToggle;

    private Filter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        filter = PreferencesManager.get().getFilter();
        searchInput.setText(filter.getSearchTerm());
        currentLocation.setText(PreferencesManager.get().getCurrentLocation());

        for (int categoryId : categoryIds) {
            String category = APIUtils.getCategoryFromId(categoryId);
            if (filter.getCategories().contains(category)) {
                int checkboxId = APIUtils.getCheckboxId(categoryId);
                CheckBox checkBox = (CheckBox) findViewById(checkboxId);
                if (checkBox != null) {
                    checkBox.setCheckedImmediately(true);
                }
            }
        }

        // Pre-fill correct distance parameter
        switch (filter.getRadius()) {
            case Filter.VERY_CLOSE:
                veryCloseToggle.setCheckedImmediately(true);
                break;
            case Filter.CLOSE:
                closeToggle.setCheckedImmediately(true);
                break;
            case Filter.FAR:
                farToggle.setCheckedImmediately(true);
                break;
            case Filter.VERY_FAR:
                veryFarToggle.setCheckedImmediately(true);
        }

        if (filter.isRandomizeResults()) {
            randomToggle.setCheckedImmediately(true);
        } else {
            switch (filter.getSortOption()) {
                case Filter.RELEVANCE:
                    relevanceToggle.setCheckedImmediately(true);
                    break;
                case Filter.DISTANCE:
                    distanceToggle.setCheckedImmediately(true);
                    break;
                case Filter.RATING:
                    ratingToggle.setCheckedImmediately(true);
                    break;
            }
        }

        dealsToggle.setCheckedImmediately(filter.isDealsOnly());
    }

    @OnTextChanged(value = R.id.search_term, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onSearchInput(Editable input) {
        clearSearch.setVisibility(input.length() == 0 ? View.GONE : View.VISIBLE);
    }

    @OnClick(R.id.clear_search)
    public void clearSearch() {
        searchInput.setText("");
    }

    @OnClick(R.id.current_location)
    public void setLocation() {
        chooseCurrentLocation();
    }

    private void chooseCurrentLocation() {
        new MaterialDialog.Builder(this)
                .title(R.string.choose_current_location)
                .content(R.string.current_instructions)
                .items(PreferencesManager.get().getLocationsArray())
                .itemsCallbackSingleChoice(PreferencesManager.get().getCurrentLocationIndex(),
                        (dialog, view, which, text) -> {
                            PreferencesManager.get().setCurrentLocation(text.toString());
                            currentLocation.setText(PreferencesManager.get().getCurrentLocation());
                            UIUtils.showSnackbar(parent, getString(R.string.current_location_set));
                            return true;
                        })
                .positiveText(R.string.choose)
                .negativeText(android.R.string.cancel)
                .neutralText(R.string.add_location_title)
                .onNeutral((dialog, which) -> addLocation())
                .show();
    }

    private void addLocation() {
        new MaterialDialog.Builder(this)
                .title(R.string.add_location_title)
                .input(getString(R.string.location), "", (dialog, input) -> {
                    String currentInput = input.toString().trim();
                    dialog.getActionButton(DialogAction.POSITIVE)
                            .setEnabled(!PreferencesManager.get().alreadyHasLocation(input.toString().trim())
                                    && !currentInput.isEmpty());
                })
                .alwaysCallInputCallback()
                .positiveText(R.string.add)
                .negativeText(android.R.string.no)
                .onPositive((dialog, which) -> {
                    String newLocation = dialog.getInputEditText().getText().toString();
                    PreferencesManager.get().addSavedLocation(newLocation);
                    PreferencesManager.get().setCurrentLocation(newLocation);
                    currentLocation.setText(PreferencesManager.get().getCurrentLocation());
                    UIUtils.showSnackbar(parent, getString(R.string.current_location_set));
                })
                .show();
    }

    @OnClick({R.id.american, R.id.chinese, R.id.fast_food, R.id.french, R.id.indian, R.id.italian, R.id.japanese,
            R.id.korean, R.id.mediterranean, R.id.middle_eastern, R.id.mexican, R.id.pizza, R.id.thai})
    public void categoryClicked(View view) {
        int checkboxId = APIUtils.getCheckboxId(view.getId());
        CheckBox checkBox = (CheckBox) findViewById(checkboxId);
        processClick(APIUtils.getCategoryFromId(view.getId()), checkBox);
    }

    @OnClick({R.id.american_toggle, R.id.chinese_toggle, R.id.fast_food_toggle, R.id.french_toggle, R.id.indian_toggle,
            R.id.italian_toggle, R.id.japanese_toggle, R.id.korean_toggle, R.id.mediterranean_toggle,
            R.id.middle_eastern_toggle, R.id.mexican_toggle, R.id.pizza_toggle, R.id.thai_toggle})
    public void checkboxClicked(View view) {
        filter.processCategory(APIUtils.getCategoryFromId(view.getId()));
    }

    private void processClick(String category, CheckBox checkBox) {
        if (filter.processCategory(category)) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
    }

    // Distance checkbox clicked
    @OnClick({R.id.very_close_toggle, R.id.close_toggle, R.id.far_toggle, R.id.very_far_toggle})
    public void distanceToggleClick(View view) {
        // If they're changing the radius, make sure to uncheck the previous thing and set the new radius
        if (filter.getRadius() != APIUtils.getDistance(view)) {
            uncheckDistance();
            switch (view.getId()) {
                case R.id.very_close_toggle:
                    filter.setRadius(Filter.VERY_CLOSE);
                    break;
                case R.id.close_toggle:
                    filter.setRadius(Filter.CLOSE);
                    break;
                case R.id.far_toggle:
                    filter.setRadius(Filter.FAR);
                    break;
                case R.id.very_far_toggle:
                    filter.setRadius(Filter.VERY_FAR);
            }
        } else {
            filter.setRadius(0);
        }
    }

    // Distance container clicked
    @OnClick({R.id.very_close, R.id.close, R.id.far, R.id.very_far})
    public void distanceBoxClick(View view) {
        // You're always unchecking something with these clicks (unless there was nothing to begin with)
        uncheckDistance();
        // If they're checking in something new, do the check, change radius
        if (filter.getRadius() != APIUtils.getDistance(view)) {
            switch (view.getId()) {
                case R.id.very_close:
                    filter.setRadius(Filter.VERY_CLOSE);
                    veryCloseToggle.setChecked(true);
                    break;
                case R.id.close:
                    filter.setRadius(Filter.CLOSE);
                    closeToggle.setChecked(true);
                    break;
                case R.id.far:
                    filter.setRadius(Filter.FAR);
                    farToggle.setChecked(true);
                    break;
                case R.id.very_far:
                    filter.setRadius(Filter.VERY_FAR);
                    veryFarToggle.setChecked(true);
            }
        } else {
            // If we're here, then they're unchecking something
            filter.setRadius(0);
        }
    }

    private void uncheckDistance() {
        switch (filter.getRadius()) {
            case Filter.VERY_CLOSE:
                veryCloseToggle.setChecked(false);
                break;
            case Filter.CLOSE:
                closeToggle.setChecked(false);
                break;
            case Filter.FAR:
                farToggle.setChecked(false);
                break;
            case Filter.VERY_FAR:
                veryFarToggle.setChecked(false);
        }
    }

    @OnClick({R.id.relevance_sort, R.id.distance_sort, R.id.rating_sort})
    public void sortBoxClick(View view) {
        randomToggle.setChecked(false);

        switch (view.getId()) {
            case R.id.relevance_sort:
                distanceToggle.setChecked(false);
                ratingToggle.setChecked(false);
                relevanceToggle.toggle();
                break;
            case R.id.distance_sort:
                relevanceToggle.setChecked(false);
                ratingToggle.setChecked(false);
                distanceToggle.toggle();
                break;
            case R.id.rating_sort:
                relevanceToggle.setChecked(false);
                distanceToggle.setChecked(false);
                ratingToggle.toggle();
                break;
        }
    }

    @OnClick({R.id.relevance_toggle, R.id.distance_toggle, R.id.rating_toggle})
    public void sortOptionChecked(View view) {
        randomToggle.setChecked(false);

        switch (view.getId()) {
            case R.id.relevance_toggle:
                distanceToggle.setChecked(false);
                ratingToggle.setChecked(false);
                break;
            case R.id.distance_toggle:
                relevanceToggle.setChecked(false);
                ratingToggle.setChecked(false);
                break;
            case R.id.rating_toggle:
                relevanceToggle.setChecked(false);
                distanceToggle.setChecked(false);
                break;
        }
    }

    @OnClick(R.id.deals_filter)
    public void toggleDealsFilter() {
        dealsToggle.toggle();
    }

    @OnCheckedChanged(R.id.random_toggle)
    public void onRandomToggle() {
        if (randomToggle.isChecked()) {
            relevanceToggle.setChecked(false);
            distanceToggle.setChecked(false);
            ratingToggle.setChecked(false);
        }
    }

    @OnClick(R.id.random_setting)
    public void toggleRandomSetting() {
        randomToggle.toggle();
    }

    private void clearFilters() {
        searchInput.setText("");

        // Clear distance filter
        veryCloseToggle.setCheckedImmediately(false);
        closeToggle.setCheckedImmediately(false);
        farToggle.setCheckedImmediately(false);
        veryFarToggle.setCheckedImmediately(false);

        dealsToggle.setCheckedImmediately(false);
        randomToggle.setCheckedImmediately(false);

        for (int categoryId : categoryIds) {
            int checkboxId = APIUtils.getCheckboxId(categoryId);
            CheckBox checkBox = (CheckBox) findViewById(checkboxId);
            if (checkBox != null) {
                checkBox.setCheckedImmediately(false);
            }
        }

        filter.clear();
        PreferencesManager.get().saveFilter(filter);
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

                if (relevanceToggle.isChecked()) {
                    filter.setSortOption(Filter.RELEVANCE);
                } else if (distanceToggle.isChecked()) {
                    filter.setSortOption(Filter.DISTANCE);
                } else if (ratingToggle.isChecked()) {
                    filter.setSortOption(Filter.RATING);
                }

                filter.setDealsOnly(dealsToggle.isChecked());
                filter.setRandomizeResults(randomToggle.isChecked());
                PreferencesManager.get().saveFilter(filter);
                setResult(RESULT_OK);
                finish();
                return true;
            case R.id.clear_filters:
                new MaterialDialog.Builder(this)
                        .title(R.string.clear_confirmation)
                        .content(R.string.remove_all_filters)
                        .positiveText(android.R.string.yes)
                        .negativeText(android.R.string.no)
                        .onPositive((dialog, which) -> clearFilters())
                        .show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
