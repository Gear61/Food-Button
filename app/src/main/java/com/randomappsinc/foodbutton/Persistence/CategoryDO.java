package com.randomappsinc.foodbutton.Persistence;

import io.realm.RealmObject;

/**
 * Created by alexanderchiou on 4/14/16.
 */
public class CategoryDO extends RealmObject {
    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
