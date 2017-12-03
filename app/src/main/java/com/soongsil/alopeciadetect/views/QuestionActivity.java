package com.soongsil.alopeciadetect.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.soongsil.alopeciadetect.R;
import com.soongsil.alopeciadetect.fragments.ExplainFragment;
import com.soongsil.alopeciadetect.fragments.HelloFragment;
import com.soongsil.alopeciadetect.fragments.QuestionFragment1;
import com.soongsil.alopeciadetect.fragments.QuestionFragment2;
import com.soongsil.alopeciadetect.fragments.QuestionFragment3;
import com.soongsil.alopeciadetect.fragments.QuestionFragment4;
import com.soongsil.alopeciadetect.fragments.QuestionFragment5;
import com.soongsil.alopeciadetect.fragments.QuestionFragment6;
import com.soongsil.alopeciadetect.fragments.QuestionFragment7;
import com.soongsil.alopeciadetect.objects.QuestionRealmObj;
import com.unstoppable.submitbuttonview.SubmitButton;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.soongsil.alopeciadetect.utils.RequestCode.GALLERY_REQUEST_CODE;
import static com.soongsil.alopeciadetect.utils.RequestCode.QUESTION_REQUEST_CODE;

/**
 * Created by Park on 2017-11-28.
 */

public class QuestionActivity extends AppCompatActivity {

    private static ViewPager viewPager;
    private static Activity activity;
    private static int[] score;

    protected static SubmitButton sbmBtn;

    private static Bundle rtnBundle;

    private static Realm mRealm;

    public void QuestionActivity() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        initialize();

    }

    private void initialize()
    {
        activity = this;
        score = new int[7];
        rtnBundle = new Bundle();

        mRealm = Realm.getDefaultInstance();

        for(int i = 0 ; i < 7 ; ++i){
            score[i] = -1;
        }

        viewPager = (ViewPager) findViewById(R.id.viewpager_questions);
        viewPager.setAdapter(new PagerAdapterClass(getSupportFragmentManager()));
    }

    private class PagerAdapterClass extends FragmentStatePagerAdapter {

        public PagerAdapterClass(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            View v = null;
            Fragment frag = null;

            if(sbmBtn != null) sbmBtn.reset();

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
                    frag = new QuestionFragment4();
                    break;
                case 6:
                    frag = new QuestionFragment5();
                    break;
                case 7:
                    frag = new QuestionFragment6();
                    break;
                case 8:
                    frag = new QuestionFragment7();
                    break;
                case 9:
                    frag = new SubmitFragment();
                    break;
            }

            return frag;
        }

        @Override
        public int getCount() {
            return 10;
        }
    }


    public static View.OnClickListener mButtonClick = new View.OnClickListener(){
        public void onClick(View v){
            switch(v.getId())
            {
                case R.id.frag1_yes:

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            score[0] = 1;
                            viewPager.setCurrentItem(3);
                        }
                    }, 500);

                    break;

                case R.id.frag1_no:

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            score[0] = 0;
                            viewPager.setCurrentItem(3);
                        }
                    }, 500);

                    break;
                case R.id.frag2_yes:

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            score[1] = 1;
                            viewPager.setCurrentItem(4);
                        }
                    }, 500);

                    break;
                case R.id.frag2_no:

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            score[1] = 0;
                            viewPager.setCurrentItem(4);
                        }
                    }, 500);

                    break;
                case R.id.frag3_yes:

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            score[2] = 1;
                            viewPager.setCurrentItem(5);
                        }
                    }, 500);

                    break;
                case R.id.frag3_no:

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            score[2] = 0;
                            viewPager.setCurrentItem(5);
                        }
                    }, 500);

                    break;
                case R.id.frag4_yes:

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            score[3] = 1;
                            viewPager.setCurrentItem(6);
                        }
                    }, 500);

                    break;
                case R.id.frag4_no:

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            score[3] = 0;
                            viewPager.setCurrentItem(6);
                        }
                    }, 500);

                    break;
                case R.id.frag5_yes:

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            score[4] = 1;
                            viewPager.setCurrentItem(7);
                        }
                    }, 500);

                    break;
                case R.id.frag5_no:
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            score[4] = 0;
                            viewPager.setCurrentItem(7);
                        }
                    }, 500);

                    break;
                case R.id.frag6_yes:

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            score[5] = 1;
                            viewPager.setCurrentItem(8);
                        }
                    }, 500);

                    break;
                case R.id.frag6_no:

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            score[5] = 0;
                            viewPager.setCurrentItem(8);
                        }
                    }, 500);

                    break;
                case R.id.frag7_yes:

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            score[6] = 1;
                            viewPager.setCurrentItem(9);
                        }
                    }, 500);

                    break;
                case R.id.frag7_no:

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            score[6] = 0;
                            viewPager.setCurrentItem(9);
                        }
                    }, 500);

                    break;
                case R.id.sbtn_loading:

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            boolean doneFlag = true;

                            for (int i = 0; i < 7; ++i) {

                                if (score[i] == -1) {
                                    doneFlag = false;
                                    break;
                                }
                            }

                            if (doneFlag) {

                                sbmBtn.doResult(true);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        mRealm.beginTransaction();

                                        RealmResults<QuestionRealmObj> queList = mRealm.where(QuestionRealmObj.class).findAll();
                                        queList.deleteAllFromRealm();

                                        QuestionRealmObj user = mRealm.createObject(QuestionRealmObj.class);
                                        user.setScore(score[0] +""+ score[1] +""+ score[2] +""+ score[3] +""+ score[4] +""+ score[5] +""+ score[6]);

                                        mRealm.commitTransaction();

                                        rtnBundle.putIntArray("score", score);
                                        activity.setResult(QUESTION_REQUEST_CODE, new Intent().putExtra("data", rtnBundle));
                                        if(sbmBtn != null) sbmBtn.reset();
                                        activity.finish();

                                    }
                                }, 1000);

                            } else {

                                sbmBtn.doResult(false);

                                Toast.makeText(activity.getApplicationContext(), "모든 질문에 답해주세요", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, 2000);

                    break;
            }
        }
    };


    public static class SubmitFragment extends Fragment {

        public SubmitFragment(){}

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_submit, container, false);
            sbmBtn = (SubmitButton) layout.findViewById(R.id.sbtn_loading);
            sbmBtn.setOnClickListener(QuestionActivity.mButtonClick);
            return layout;
        }

    }

    private long backKeyPressedTime = 0;
    private Toast toast;

    @Override
    public void onBackPressed() {

        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(getApplicationContext(), "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000)
        {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            toast.cancel();
        }
    }

}
