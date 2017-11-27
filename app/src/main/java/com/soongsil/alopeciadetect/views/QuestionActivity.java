package com.soongsil.alopeciadetect.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.soongsil.alopeciadetect.R;
import com.soongsil.alopeciadetect.fragments.ExplainFragment;
import com.soongsil.alopeciadetect.fragments.HelloFragment;
import com.soongsil.alopeciadetect.fragments.QuestionFragment1;
import com.soongsil.alopeciadetect.fragments.QuestionFragment2;
import com.soongsil.alopeciadetect.fragments.QuestionFragment3;
import com.soongsil.alopeciadetect.fragments.SubmitFragment;

import static com.soongsil.alopeciadetect.utils.RequestCode.QUESTION_REQUEST_CODE;

/**
 * Created by Park on 2017-11-28.
 */

public class QuestionActivity extends AppCompatActivity {

    private static ViewPager viewPager;
    private static Activity activity;
    private static int score;

    public void QuestionActivity() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        initialize();

    }

    private void initialize()
    {
        score = 3;

        activity = this;

        viewPager = (ViewPager) findViewById(R.id.viewpager_questions);
        viewPager.setAdapter(new PagerAdapterClass(getSupportFragmentManager()));
    }

    public class PagerAdapterClass extends FragmentStatePagerAdapter {

        private LayoutInflater inflater;

        public PagerAdapterClass(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            View v = null;
            Fragment frag = null;

            switch(position){
                case 0:
                    frag = new HelloFragment();
                    break;
                case 1:
                    frag = new ExplainFragment();
                    break;
                case 2:
                    frag = new QuestionFragment1();
                    break;
                case 3:
                    frag = new QuestionFragment2();
                    break;
                case 4:
                    frag = new QuestionFragment3();
                    break;
                case 5:
                    frag = new SubmitFragment();
                    break;
            }

            return frag;
        }

        @Override
        public int getCount() {
            return 6;
        }
    }


    public static View.OnClickListener mButtonClick = new View.OnClickListener(){
        public void onClick(View v){
            switch(v.getId())
            {
                case R.id.frag1_yes:
                    viewPager.setCurrentItem(3);
                    break;
                case R.id.frag1_no:
                    viewPager.setCurrentItem(3);
                    break;
                case R.id.frag2_yes:
                    viewPager.setCurrentItem(4);
                    break;
                case R.id.frag2_no:
                    viewPager.setCurrentItem(4);
                    break;
                case R.id.frag3_yes:
                    viewPager.setCurrentItem(5);
                    break;
                case R.id.frag3_no:
                    viewPager.setCurrentItem(5);
                    break;
                case R.id.frag_submit:
                    Bundle bundle = new Bundle();
                    bundle.putInt("score", score);
                    activity.setResult(QUESTION_REQUEST_CODE, new Intent().putExtra("data", bundle));
                    activity.finish();
                    break;
            }
        }
    };

}
