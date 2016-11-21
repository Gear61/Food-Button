package com.randomappsinc.foodbutton.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.randomappsinc.foodbutton.Models.FavoritesFilter;
import com.randomappsinc.foodbutton.Models.Restaurant;
import com.randomappsinc.foodbutton.Persistence.DatabaseManager;
import com.randomappsinc.foodbutton.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by alexanderchiou on 4/14/16.
 */
public class FavoritesAdapter extends BaseAdapter {
    private Context context;
    private TextView noFavorites;
    private List<Restaurant> restaurantList;
    private FavoritesFilter filter;

    public FavoritesAdapter (Context context, TextView noFavorites) {
        this.context = context;
        this.noFavorites = noFavorites;
        this.filter = new FavoritesFilter();
        syncWithDb();
    }

    public FavoritesFilter getFilter() {
        return filter;
    }

    public void setFilter(FavoritesFilter filter) {
        this.filter = filter;
    }

    public void syncWithDb() {
        this.restaurantList = DatabaseManager.get().getFavorites(filter);
        notifyDataSetChanged();
        setNoContent();
    }

    public void setNoContent() {
        if (DatabaseManager.get().getFavorites(new FavoritesFilter()).isEmpty()) {
            noFavorites.setText(R.string.no_favorites);
        } else {
            noFavorites.setText(R.string.no_favorite_matches);
        }
        int viewVisibility = restaurantList.isEmpty() ? View.VISIBLE : View.GONE;
        noFavorites.setVisibility(viewVisibility);
    }

    public Restaurant getRandomRestaurant() {
        Random generator = new Random();
        int randomIndex = generator.nextInt(getCount());
        return getItem(randomIndex);
    }

    public void unfavoriteRestaurant(int position) {
        DatabaseManager.get().removeFavorite(getItem(position));
        restaurantList.remove(position);
        notifyDataSetChanged();
        setNoContent();
    }

    public class RestaurantViewHolder {
        @Bind(R.id.restaurant_picture) ImageView picture;
        @Bind(R.id.restaurant_name) TextView name;
        @Bind(R.id.address) TextView address;

        public RestaurantViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void loadRestaurant(Restaurant restaurant) {
            Picasso.with(context).load(restaurant.getImageUrl()).into(picture);
            name.setText(restaurant.getName());
            address.setText(restaurant.getAddress());
        }
    }

    @Override
    public int getCount() {
        return restaurantList.size();
    }

    @Override
    public Restaurant getItem(int position) {
        return restaurantList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        RestaurantViewHolder holder;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.restaurant_cell, parent, false);
            holder = new RestaurantViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (RestaurantViewHolder) view.getTag();
        }
        holder.loadRestaurant(getItem(position));
        return view;
    }
}
