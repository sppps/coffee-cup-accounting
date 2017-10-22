package ru.sppps.coffeecup.models;

import org.json.JSONObject;

/**
 * Created by sppps on 20.10.17.
 */

public class TechMap {
    protected String mCategory;
    protected String mName;

    TechMap() {}

    public void setName(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    public String getCategory() {
        return mCategory;
    }

    public static TechMap fromJsonObject(JSONObject json) {
        try {
            TechMap techMap = new TechMap();
            techMap.setName(json.getString("name"));
            techMap.setCategory(json.getString("category"));
            return techMap;
        }
        catch(org.json.JSONException e) {
            return null;
        }
    }

    public String toString () {
        return String.format("%s / %s", mCategory, mName);
    }
}
