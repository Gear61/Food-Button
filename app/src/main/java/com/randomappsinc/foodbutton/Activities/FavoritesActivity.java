package com.randomappsinc.foodbutton.Activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.foodbutton.Adapters.FavoritesAdapter;
import com.randomappsinc.foodbutton.Fragments.RestaurantFragment;
import com.randomappsinc.foodbutton.Models.FavoritesFilter;
import com.randomappsinc.foodbutton.Models.Restaurant;
import com.randomappsinc.foodbutton.Persistence.DatabaseManager;
import com.randomappsinc.foodbutton.R;
import com.randomappsinc.foodbutton.Utils.MyApplication;
import com.randomappsinc.foodbutton.Utils.UIUtils;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class FavoritesActivity extends StandardActivity {

    @BindView(R.id.parent) View parent;
    @BindView(R.id.favorites) SwipeMenuListView favorites;
    @BindView(R.id.no_favorites) TextView noFavorites;

    @BindColor(R.color.gray) int gray;
    @BindColor(R.color.app_red) int red;

    private FavoritesAdapter favoritesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        favoritesAdapter = new FavoritesAdapter(this, noFavorites);
        favorites.setAdapter(favoritesAdapter);

        favorites.setMenuCreator(new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem shareItem = new SwipeMenuItem(getApplicationContext());
                shareItem.setBackground(new ColorDrawable(red));
                shareItem.setWidth(UIUtils.convertDpToPixels(76));
                shareItem.setIcon(new IconDrawable(MyApplication.getAppContext(), IoniconsIcons.ion_android_share_alt)
                        .colorRes(R.color.white)
                        .sizeDp(32));
                menu.addMenuItem(shareItem);

                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(gray));
                deleteItem.setWidth(UIUtils.convertDpToPixels(76));
                deleteItem.setIcon(new IconDrawable(MyApplication.getAppContext(), IoniconsIcons.ion_trash_a)
                        .colorRes(R.color.white)
                        .sizeDp(32));
                menu.addMenuItem(deleteItem);
            }
        });

        favorites.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.restaurant_info));
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, favoritesAdapter.getItem(position).getShareText());
                        startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_with)));
                        break;
                    case 1:
                        favoritesAdapter.unfavoriteRestaurant(position);
                        invalidateOptionsMenu();
                        UIUtils.showSnackbar(parent, getString(R.string.restaurant_unfavorited));
                        break;
                }
                return true;
            }
        });
    }

    @OnItemClick(R.id.favorites)
    public void viewRestaurant(int position) {
        Restaurant restaurant = favoritesAdapter.getItem(position);
        Intent intent = new Intent(this, RestaurantActivity.class);
        intent.putExtra(RestaurantFragment.RESTAURANT_KEY, restaurant);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            FavoritesFilter filter = data.getParcelableExtra(FilterActivity.FILTER_KEY);
            favoritesAdapter.setFilter(filter);
            UIUtils.showSnackbar(parent, getString(R.string.filters_applied));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        favoritesAdapter.syncWithDb();
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorites_menu, menu);
        if (favorites.getCount() == 0) {
            menu.findItem(R.id.load_random_restaurant).setVisible(false);
            // Only hide filters if they legitimately have no favorites
            if (DatabaseManager.get().getFavorites(new FavoritesFilter()).isEmpty()) {
                menu.findItem(R.id.filters).setVisible(false);
            } else {
                UIUtils.loadMenuIcon(menu, R.id.filters, IoniconsIcons.ion_android_options);
            }
        } else {
            UIUtils.loadMenuIcon(menu, R.id.filters, IoniconsIcons.ion_android_options);
            UIUtils.loadMenuIcon(menu, R.id.load_random_restaurant, IoniconsIcons.ion_shuffle);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filters:
                Intent setFilterIntent = new Intent(this, FavoritesFilterActivity.class);
                setFilterIntent.putExtra(FilterActivity.FILTER_KEY, favoritesAdapter.getFilter());
                startActivityForResult(setFilterIntent, 1);
                return true;
            case R.id.load_random_restaurant:
                Intent intent = new Intent(this, RestaurantActivity.class);
                intent.putExtra(RestaurantFragment.RESTAURANT_KEY, favoritesAdapter.getRandomRestaurant());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
