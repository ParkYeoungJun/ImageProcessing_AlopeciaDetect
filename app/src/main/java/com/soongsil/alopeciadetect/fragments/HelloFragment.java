package com.soongsil.alopeciadetect.fragments;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.soongsil.alopeciadetect.R;

/**
 * Created by Park on 2017-11-28.
 */

public class HelloFragment extends Fragment {

    public HelloFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.fragment_hello, container, false);
        return layout;
    }

}
