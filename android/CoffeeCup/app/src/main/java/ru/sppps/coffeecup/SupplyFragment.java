package ru.sppps.coffeecup;

import android.content.Intent;

import org.json.JSONObject;

import ru.sppps.coffeecup.models.Ingredient;
import ru.sppps.coffeecup.models.Supply;


public class SupplyFragment extends BaseModelsListFragment<Supply> {

    protected String getApiMethod() {
        return "supply/list";
    }

    protected void prepateIntentForEdit(Intent intent, Supply supply) {

    }

    protected Supply createModelInstanceFromJson(JSONObject json)
    {
        return Supply.fromJsonObject(json);
    }
}
