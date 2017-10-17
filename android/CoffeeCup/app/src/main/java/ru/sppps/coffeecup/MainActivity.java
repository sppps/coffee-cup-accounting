package ru.sppps.coffeecup;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;

public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener {
    private String[] mDrawerItems;
    private ListView mDrawerListView;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerItems = getResources().getStringArray(R.array.drawer_items);
        mDrawerListView = (ListView) findViewById(R.id.left_drawer);
        mDrawerListView.setOnItemClickListener(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerListView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mDrawerItems));
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Fragment fragment;
        switch(position){
            case 0: fragment = new ConsumeFragment(); break;
            case 1: fragment = new SupplyFragment(); break;
            case 2: fragment = new ConsumersFragment(); break;
            case 3: fragment = new IngredientsFragment(); break;
            case 4: fragment = new TechmapsFragment(); break;
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
