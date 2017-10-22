package ru.sppps.coffeecup.models;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sppps on 20.10.17.
 */

public class Consume {
    private String mId;
    private String mTechMapId;
    private Date mDateTime;
    private Double mTotal;
    private ArrayList<Consumer> mConsumers;

    Consume() {}

    public static Consume fromJsonObject(JSONObject json) {
        Consume consume = new Consume();
        return consume;
    }
}
