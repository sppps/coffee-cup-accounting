package ru.sppps.coffeecup;

import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;

import ru.sppps.coffeecup.models.Consumer;

public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener {
    private String[] mDrawerItems;
    private ListView mDrawerListView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Animation animRotateIn_icon = AnimationUtils.loadAnimation(this,
                R.anim.rotate);
        findViewById(android.R.id.content).startAnimation(animRotateIn_icon);

        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };


        mDrawerItems = getResources().getStringArray(R.array.drawer_items);
        mDrawerListView = (ListView) findViewById(R.id.left_drawer);
        mDrawerListView.setOnItemClickListener(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerListView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mDrawerItems));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                if (mDrawerLayout.isDrawerOpen(mDrawerListView)) {
                    mDrawerLayout.closeDrawer(mDrawerListView);
                } else {
                    mDrawerLayout.openDrawer(mDrawerListView);
                }
                return true;
        }
        return false;
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Fragment fragment;
        switch(position){
            case 0:
                fragment = new ConsumeFragment();
                break;
            case 1:
                fragment = new SupplyFragment();
                break;
            case 2:
                fragment = new ConsumersFragment();
                break;
            case 3:
                fragment = new IngredientsFragment();
                break;
            case 4:
                fragment = new TechmapsFragment();
                break;
            default: return;
        }
        Bundle args = new Bundle();
        args.putInt("menu", position);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        mDrawerListView.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerListView);
        setTitle(mDrawerItems[position]);
    }

}
