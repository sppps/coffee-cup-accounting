package ru.sppps.coffeecup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import ru.sppps.coffeecup.R;


public class ConsumeFragment extends Fragment {

    public ConsumeFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.consume_layout, container, false);
        return rootView;
    }
}
