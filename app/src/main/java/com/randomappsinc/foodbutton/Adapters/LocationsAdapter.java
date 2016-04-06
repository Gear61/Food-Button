package com.randomappsinc.foodbutton.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.randomappsinc.foodbutton.R;
import com.randomappsinc.foodbutton.Utils.PreferencesManager;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by alexanderchiou on 4/5/16.
 */
public class LocationsAdapter extends BaseAdapter {
    private Context context;
    private List<String> content;
    private View noContent;
    private String locationHint;

    public LocationsAdapter(Context context, View noContent) {
        this.context = context;
        this.content = PreferencesManager.get().getUserLocations();
        this.noContent = noContent;
        this.locationHint = context.getString(R.string.location);
        setNoContent();
    }

    public void setNoContent() {
        int viewVisibility = content.isEmpty() ? View.VISIBLE : View.GONE;
        noContent.setVisibility(viewVisibility);
    }

    public void removeLocation(int index) {
        PreferencesManager.get().removeSavedLocation(getItem(index));
        content.remove(index);
        notifyDataSetChanged();
        setNoContent();
    }

    public void changeLocation(int position, String newLocation) {
        String oldLocation = getItem(position);
        PreferencesManager.get().changeSavedLocation(oldLocation, newLocation);
        content.set(position, newLocation);
        Collections.sort(content);
        notifyDataSetChanged();
    }

    public void showDeleteDialog(final int position) {
        String confirmDeletionMessage = context.getString(R.string.location_delete_confirmation) +
                getItem(position) + "?";

        new MaterialDialog.Builder(context)
                .title(R.string.delete_title)
                .content(confirmDeletionMessage)
                .positiveText(android.R.string.yes)
                .negativeText(android.R.string.no)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        removeLocation(position);
                    }
                })
                .show();
    }

    public void showRenameDialog(final int position) {
        new MaterialDialog.Builder(context)
                .title(R.string.change_location)
                .input(locationHint, getItem(position), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        boolean submitEnabled = !(input.toString().trim().isEmpty()
                                || PreferencesManager.get().alreadyHasLocation(input.toString()));
                        dialog.getActionButton(DialogAction.POSITIVE).setEnabled(submitEnabled);
                    }
                })
                .alwaysCallInputCallback()
                .negativeText(android.R.string.no)
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (which == DialogAction.POSITIVE) {
                            String newName = dialog.getInputEditText().getText().toString();
                            changeLocation(position, newName);
                        }
                    }
                })
                .show();
    }

    public class LocationViewHolder {
        @Bind(R.id.location) TextView locationText;

        private int position;

        public LocationViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void loadLocation(int position) {
            this.position = position;
            locationText.setText(getItem(position));
        }

        @OnClick(R.id.edit_icon)
        public void editLocation() {
            showRenameDialog(position);
        }

        @OnClick(R.id.delete_icon)
        public void deleteLocation() {
            showDeleteDialog(position);
        }
    }

    @Override
    public int getCount() {
        return content.size();
    }

    @Override
    public String getItem(int position) {
        return content.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LocationViewHolder holder;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.location_cell, parent, false);
            holder = new LocationViewHolder(view);
            view.setTag(holder);
        }
        else {
            holder = (LocationViewHolder) view.getTag();
        }
        holder.loadLocation(position);
        return view;
    }
}
