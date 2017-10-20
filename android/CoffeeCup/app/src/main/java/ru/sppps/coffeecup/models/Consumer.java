package ru.sppps.coffeecup.models;

import org.json.JSONObject;

public class Consumer {
    protected String mName = null;
    protected Double mDebt = 0.0;

    Consumer() {}

    public void setName(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setDebt(Double debt) {
        mDebt = debt;
    }

    public Double getDebt() {
        return mDebt;
    }

    public static Consumer fromJsonObject(JSONObject json) {
        try {
            Consumer consumer = new Consumer();
            consumer.setName(json.getString("name"));
            consumer.setDebt(json.getDouble("debt"));
            return consumer;
        }
        catch(org.json.JSONException e) {
            return null;
        }
    }

    public String toString () {
        return String.format("%s: %.2f руб.", mName, mDebt);
    }
}
