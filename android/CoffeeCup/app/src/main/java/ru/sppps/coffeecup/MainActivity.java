package ru.sppps.coffeecup;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener {
    private ArrayList<DrawerMenuItem> mDrawerItems = new ArrayList<DrawerMenuItem>();
    private ListView mDrawerListView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    protected class DrawerMenuItem {
        public int mIcon;
        public String mTitle;
    }

    protected class MainMenuAdapter extends BaseAdapter {
        ArrayList<DrawerMenuItem> mItems;
        Context mCtx;
        MainMenuAdapter(Context ctx, ArrayList<DrawerMenuItem> items) {
            mCtx = ctx;
            mItems = items;
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        // пункт списка
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // используем созданные, но не используемые view
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater)mCtx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_item_drawer, parent, false);
            }

            DrawerMenuItem i = mItems.get(position);

//            // заполняем View в пункте списка данными из товаров: наименование, цена
//            // и картинка
            ((TextView) view.findViewById(R.id.label)).setText(i.mTitle);
//            ((TextView) view.findViewById(R.id.tvPrice)).setText(p.price + "");
//            ((ImageView) view.findViewById(R.id.ivImage)).setImageResource(p.image);
//
//            CheckBox cbBuy = (CheckBox) view.findViewById(R.id.cbBox);
//            // присваиваем чекбоксу обработчик
//            cbBuy.setOnCheckedChangeListener(myCheckChangeList);
//            // пишем позицию
//            cbBuy.setTag(position);
//            // заполняем данными из товаров: в корзине или нет
//            cbBuy.setChecked(p.box);
            return view;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        String[] drawerItems = getResources().getStringArray(R.array.drawer_items);
        for (String item: drawerItems) {
            DrawerMenuItem menuItem = new DrawerMenuItem();
            menuItem.mTitle = item;
            mDrawerItems.add(menuItem);
        }

        // mDrawerItems = getResources().getStringArray(R.array.drawer_items);
        mDrawerListView = (ListView) findViewById(R.id.left_drawer);
        mDrawerListView.setOnItemClickListener(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // mDrawerListView.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item_drawer, mDrawerItems));
        mDrawerListView.setAdapter(new MainMenuAdapter(this, mDrawerItems));

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
            case 5:
                SharedPreferences sharedPref = getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                sharedPref.edit().remove("AccessToken").commit();
                return;
            default:
                return;
        }
        Bundle args = new Bundle();
        args.putInt("menu", position);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        mDrawerListView.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerListView);
        getSupportActionBar().setTitle(mDrawerItems.get(position).mTitle);
    }

}
