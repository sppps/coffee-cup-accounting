package ru.sppps.coffeecup;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


public abstract class BaseModelsListFragment<T>
        extends Fragment
        implements ListView.OnItemClickListener {
    ArrayList<T> listItems = new ArrayList<T>();
    ArrayAdapter<T> adapter;

    private ListView mListView = null;
    private View mProgressView = null;
    protected int mListItemId = R.layout.list_item_consumer;

    public BaseModelsListFragment() {}

    protected abstract T createModelInstanceFromJson(JSONObject json);
    protected abstract void prepateIntentForEdit(Intent intent, T item);
    protected abstract String getApiMethod();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_layout_base_models_list, container, false);
        mProgressView = (View)rootView.findViewById(R.id.task_progress);
        mListView = (ListView)rootView.findViewById(R.id.consumers_list);
        mListView.setOnItemClickListener(this);
        adapter = new ArrayAdapter<T>(getContext(), mListItemId, R.id.label, listItems);
        mListView.setAdapter(adapter);
        new FetchConsumersList(getContext()).execute();
        showProgress(true);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.consumers_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_consumer:
                Intent intent = new Intent(getContext(), ConsumerActivity.class);
                startActivityForResult(intent, 1);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Toast.makeText(getContext(), "GOOD!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), ConsumerActivity.class);
        intent.setAction(Intent.ACTION_EDIT);
        T item = listItems.get(position);
        prepateIntentForEdit(intent, item);
        getActivity().startActivityForResult(intent, 2);
        getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    private class FetchConsumersList extends BaseJsonApiTask {
        FetchConsumersList (Context context) {
            super(context, getApiMethod());
        }
        protected void onPostExecute(final JSONObject response) {
            showProgress(false);
            if (response != null) {
                Log.d("LOG", response.toString());
                try{
                    JSONArray consumers = response.getJSONArray("items");
                    listItems.clear();
                    for(int i=0; i<consumers.length(); i++) {
                        JSONObject json = consumers.getJSONObject(i);
                        listItems.add(createModelInstanceFromJson(json));
                    }
                    adapter.notifyDataSetChanged();
                }
                catch (org.json.JSONException e) {

                }
            }
        }
    }
}
