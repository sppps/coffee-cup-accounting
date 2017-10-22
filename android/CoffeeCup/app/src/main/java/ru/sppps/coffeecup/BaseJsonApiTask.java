package ru.sppps.coffeecup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class BaseJsonApiTask extends AsyncTask<Void, Void, JSONObject> {
    private BufferedReader reader = null;
    private JSONObject response = null;
    private String mMethod = null;
    protected HttpURLConnection urlConnection = null;
    protected Context mContext = null;
    final private String LOG_TAG = "BaseJsonApiTask";

    public BaseJsonApiTask(Context context, String method) {
        mContext = context;
        mMethod = method;
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        try {
            String urlString = mContext.getString(R.string.api_url_base) + mMethod;
            URL url = new URL(urlString);

            Log.d(LOG_TAG, urlString);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            setupUrlConnection();
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            try{
                response = new JSONObject(buffer.toString());
            }
            catch (org.json.JSONException e) {
                Log.d(LOG_TAG, e.toString());
                return null;
            }
        }
        catch (IOException e)
        {
            Log.d(LOG_TAG, String.format("Status: %d, %s", 500, e.toString()));
            return null;
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }
        return response;
    }

    protected void setupUrlConnection () throws java.net.ProtocolException {
        SharedPreferences sharedPref = mContext.getSharedPreferences(
                mContext.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String accessToken = sharedPref.getString("AccessToken", "");
        if (accessToken.length() > 0) {
            Log.d(LOG_TAG, String.format("Access token: %s", accessToken));
            urlConnection.setRequestProperty("X-Access-Token", accessToken);
        } else {
            Intent intent = new Intent(mContext, LoginActivity.class);
            mContext.startActivity(intent);
        }
    }
}
