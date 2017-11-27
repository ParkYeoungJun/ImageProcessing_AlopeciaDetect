package com.soongsil.alopeciadetect.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.soongsil.alopeciadetect.R;
import com.soongsil.alopeciadetect.views.QuestionActivity;

/**
 * Created by Park on 2017-11-27.
 */

public class QuestionFragment1 extends Fragment {

    public QuestionFragment1(){}

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_question1, container, false);
        layout.findViewById(R.id.frag1_yes).setOnClickListener(QuestionActivity.mButtonClick);
        layout.findViewById(R.id.frag1_no).setOnClickListener(QuestionActivity.mButtonClick);
        return layout;
    }

}
