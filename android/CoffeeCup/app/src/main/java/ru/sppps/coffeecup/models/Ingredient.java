package ru.sppps.coffeecup.models;

import org.json.JSONObject;

/**
 * Created by sppps on 20.10.17.
 */

public class Ingredient {
    private String mId;
    private String mCategory;
    private String mName;
    private String mUnits;

    Ingredient() {}

    protected void setId(String id) { mId = id; }
    public void setCategory(String category) { mCategory = category; }
    public void setName(String name) { mName = name; }
    public void setUnits(String units) { mUnits = units; }

    public String getId() { return mId; }
    public String getCategory() { return mCategory; }
    public String getName() { return mName; }
    public String getUnits() { return mUnits; }

    public static Ingredient fromJsonObject(JSONObject json) {
        try {
            Ingredient ingredient = new Ingredient();
            ingredient.setId(json.getString("_id"));
            ingredient.setCategory(json.getString("category"));
            ingredient.setName(json.getString("name"));
            ingredient.setUnits(json.getString("units"));
            return ingredient;
        }
        catch (org.json.JSONException e) {
            return null;
        }
    }

    public String toString() {
        return String.format("%s %s", mCategory, mName);
    }
}
