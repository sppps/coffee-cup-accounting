package ru.sppps.coffeecup;

import android.content.Intent;
import org.json.JSONObject;
import ru.sppps.coffeecup.models.Consumer;

public class ConsumersFragment extends BaseModelsListFragment<Consumer> {

    protected String getApiMethod() {
        return "consumers/list";
    }

    protected void prepateIntentForEdit(Intent intent, Consumer consume) {

    }

    protected Consumer createModelInstanceFromJson(JSONObject json)
    {
        return Consumer.fromJsonObject(json);
    }
}
