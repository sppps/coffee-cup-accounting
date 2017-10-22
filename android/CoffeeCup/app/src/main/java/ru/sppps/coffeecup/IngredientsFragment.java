package ru.sppps.coffeecup;

import android.content.Intent;
import org.json.JSONObject;
import ru.sppps.coffeecup.models.Ingredient;


public class IngredientsFragment extends BaseModelsListFragment<Ingredient> {

    protected String getApiMethod() {
        return "ingredients/list";
    }

    protected void prepateIntentForEdit(Intent intent, Ingredient consume) {

    }

    protected Ingredient createModelInstanceFromJson(JSONObject json)
    {
        return Ingredient.fromJsonObject(json);
    }
}
