package ru.sppps.coffeecup;

import android.content.Intent;

import org.json.JSONObject;

import ru.sppps.coffeecup.models.Consume;

public class ConsumeFragment extends BaseModelsListFragment<Consume> {
    protected String getApiMethod() {
        return "/api/consume/list";
    }

    protected void prepateIntentForEdit(Intent intent, Consume consume) {

    }

    protected Consume createModelInstanceFromJson(JSONObject json)
    {
        return Consume.fromJsonObject(json);
    }
}
