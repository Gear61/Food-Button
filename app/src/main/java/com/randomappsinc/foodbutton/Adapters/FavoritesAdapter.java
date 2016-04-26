package com.randomappsinc.foodbutton.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.randomappsinc.foodbutton.Persistence.DatabaseManager;
import com.randomappsinc.foodbutton.R;
import com.randomappsinc.foodbutton.Restaurant.Restaurant;
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
    private View noFavorites;
    private List<Restaurant> restaurantList;

    public FavoritesAdapter (Context context, View noFavorites) {
        this.context = context;
        this.noFavorites = noFavorites;
        syncWithDb();
    }

    public void syncWithDb() {
        this.restaurantList = DatabaseManager.get().getFavorites();
        notifyDataSetChanged();
        setNoContent();
    }

    public void setNoContent() {
        int viewVisibility = restaurantList.isEmpty() ? View.VISIBLE : View.GONE;
        noFavorites.setVisibility(viewVisibility);
    }

    public Restaurant getRandomRestaurant() {
        Random generator = new Random();
        int randomIndex = generator.nextInt(getCount());
        return getItem(randomIndex);
    }

    public class RestaurantViewHolder {
        @Bind(R.id.restaurant_picture) ImageView picture;
        @Bind(R.id.restaurant_name) TextView name;
        @Bind(R.id.categories) TextView categories;

        public RestaurantViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void loadRestaurant(Restaurant restaurant) {
            Picasso.with(context).load(restaurant.getImageUrl()).into(picture);
            name.setText(restaurant.getName());
            categories.setText(restaurant.getCategories());
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
        }
        else {
            holder = (RestaurantViewHolder) view.getTag();
        }
        holder.loadRestaurant(getItem(position));
        return view;
    }
}
