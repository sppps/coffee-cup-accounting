package ru.sppps.coffeecup;

import android.content.Intent;

import org.json.JSONObject;

import ru.sppps.coffeecup.models.Supply;
import ru.sppps.coffeecup.models.TechMap;

public class TechmapsFragment extends BaseModelsListFragment<TechMap> {

    protected String getApiMethod() {
        return "techmaps/list";
    }

    protected void prepateIntentForEdit(Intent intent, TechMap supply) {

    }

    protected TechMap createModelInstanceFromJson(JSONObject json)
    {
        return TechMap.fromJsonObject(json);
    }
}
