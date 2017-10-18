package ru.sppps.coffeecup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.os.AsyncTask;
import android.util.Log;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.Intent;

import org.json.JSONObject;

import java.net.*;
import java.io.*;
import ru.sppps.coffeecup.R;



public class ConsumersFragment extends Fragment {

    public ConsumersFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.consumers_layout, container, false);
        new FetchConsumersList(getContext()).execute();
        return rootView;
    }

    private class FetchConsumersList extends BaseJsonApiTask {
        FetchConsumersList (Context context) {
            super(context, "consumers/list");
        }
        protected void onPostExecute(final JSONObject response) {
            if (response != null) {
                Log.d("LOG", response.toString());
            }
        }
    }
}
