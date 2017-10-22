package ru.sppps.coffeecup.models;

import org.json.JSONObject;

import java.util.Date;

/**
 * Created by sppps on 20.10.17.
 */

public class Supply {
    private String mId;
    private String mIngredientId;
    private Double mPrice;
    private Double mSupplyAmount;
    private Double mCurrentAmount;
    private Date mDate;

    Supply() {}

    public static Supply fromJsonObject(JSONObject json) {
        try {
            Supply supply = new Supply();
            supply.mId = json.getString("id");
            return supply;
        }
        catch(org.json.JSONException e) {
            return null;
        }
    }

    public String toString() {
        return String.format("Supply %s", mId);
    }
}
