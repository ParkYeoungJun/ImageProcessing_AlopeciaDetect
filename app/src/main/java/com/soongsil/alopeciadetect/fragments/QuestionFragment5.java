package com.soongsil.alopeciadetect.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.soongsil.alopeciadetect.R;
import com.soongsil.alopeciadetect.views.QuestionActivity;

/**
 * Created by Park on 2017-12-01.
 */

public class QuestionFragment5 extends Fragment {

    public QuestionFragment5() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_question5, container, false);
        layout.findViewById(R.id.frag5_yes).setOnClickListener(QuestionActivity.mButtonClick);
        layout.findViewById(R.id.frag5_no).setOnClickListener(QuestionActivity.mButtonClick);
        return layout;
    }
}
