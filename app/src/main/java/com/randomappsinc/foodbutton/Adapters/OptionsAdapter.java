package com.randomappsinc.foodbutton.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.randomappsinc.foodbutton.R;
import com.rey.material.widget.CheckBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by alexanderchiou on 7/5/16.
 */
public class OptionsAdapter extends BaseAdapter {
    private Context context;
    private View noContent;
    private List<String> allOptions;
    private List<String> matchingOptions;
    private Set<String> chosen;

    public OptionsAdapter(Context context, View noContent, List<String> allOptions, List<String> chosen) {
        this.context = context;
        this.noContent = noContent;
        this.allOptions = allOptions;
        this.matchingOptions = new ArrayList<>();
        this.chosen = new HashSet<>(chosen);
        refreshList("");
    }

    public List<String> getChosen() {
        List<String> chosen = new ArrayList<>(this.chosen);
        Collections.sort(chosen);
        return chosen;
    }

    public void refreshList(String searchTerm) {
        matchingOptions.clear();
        if (searchTerm.isEmpty()) {
            matchingOptions.addAll(allOptions);
        } else {
            for (String option : allOptions) {
                if (option.toLowerCase().contains(searchTerm.toLowerCase())) {
                    matchingOptions.add(option);
                }
            }
        }
        refreshUI();
    }

    public void setNoContent() {
        if (matchingOptions.isEmpty()) {
            noContent.setVisibility(View.VISIBLE);
        } else {
            noContent.setVisibility(View.GONE);
        }
    }

    public void refreshUI() {
        notifyDataSetChanged();
        setNoContent();
    }

    @Override
    public int getCount() {
        return matchingOptions.size();
    }

    @Override
    public String getItem(int position) {
        return matchingOptions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class OptionViewHolder {
        @Bind(R.id.option_text) TextView optionText;
        @Bind(R.id.option_toggle) CheckBox optionToggle;

        private int position;

        public OptionViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void loadOption(int position) {
            this.position = position;
            this.optionText.setText(getItem(position));
            this.optionToggle.setCheckedImmediately(chosen.contains(getItem(position)));
        }

        @OnClick(R.id.parent)
        public void cellClicked() {
            optionToggle.setChecked(!chosen.contains(getItem(position)));
            processChoice();
        }

        @OnClick(R.id.option_toggle)
        public void onToggle() {
            processChoice();
        }

        private void processChoice() {
            if (chosen.contains(getItem(position))) {
                chosen.remove(getItem(position));
            } else {
                chosen.add(getItem(position));
            }
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        OptionViewHolder holder;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.option_cell, parent, false);
            holder = new OptionViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (OptionViewHolder) view.getTag();
        }
        holder.loadOption(position);
        return view;
    }
}
