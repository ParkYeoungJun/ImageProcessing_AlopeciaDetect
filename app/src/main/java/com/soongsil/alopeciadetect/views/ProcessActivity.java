package com.soongsil.alopeciadetect.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soongsil.alopeciadetect.R;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Park on 2017-12-03.
 */

public class ProcessActivity extends AppCompatActivity {

    private static Mat matKeratin;
    private static Mat matAlopecia;
    private static Bitmap bmpKeratin;
    private static Bitmap bmpAlopecia;
    private static int keratinScore;
    private static int alopeciaScore;

    private ViewPager viewPager;
    private Button btnKeratin;
    private Button btnAlopecia;
    private Button btnResult;

    public native int AddrToMat(long matAddrInput, long matAddrResult);
    static {
        System.loadLibrary("opencv_java3");
        System.loadLibrary("native-lib");
    }

    public ProcessActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);

        initialize();
    }

    public void initialize() {

        bmpAlopecia = null;
        bmpKeratin = null;
        viewPager = (ViewPager) findViewById(R.id.viewpager_process);
        viewPager.setAdapter(new PagerAdapterClass(getSupportFragmentManager()));
        btnKeratin = (Button) findViewById(R.id.btn_keratin);
        btnAlopecia = (Button) findViewById(R.id.btn_alopecia);
        btnResult = (Button) findViewById(R.id.btn_result);

        Intent intent = getIntent();

        long keratinAddr = intent.getLongExtra("keratin_mat", 0);
        long alopeciAddr = intent.getLongExtra("alopecia_mat", 0);

        matKeratin = new Mat(keratinAddr);
        matAlopecia = new Mat(alopeciAddr);

        keratinScore = intent.getIntExtra("keratin_score", 0);
        alopeciaScore = intent.getIntExtra("alopecia_score", 0);

        btnAlopecia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(0);
            }
        });
        btnKeratin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
            }
        });
        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(2);
            }
        });


    }

     private class PagerAdapterClass extends FragmentStatePagerAdapter {

        public PagerAdapterClass(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment frag = null;

            switch(position){
                case 0:
                    frag = new ProcessAlopeciaFragment();
                    break;
                case 1:
                    frag = new ProcessKeratinFragment();
                    break;
                case 2:
                    frag = new ProcessResultFragment();
                    break;
            }

            return frag;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    public static class ProcessAlopeciaFragment extends Fragment {

        public ProcessAlopeciaFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_alopecia, container, false);

            if(bmpAlopecia == null) {
                try {
                    bmpAlopecia = Bitmap.createBitmap(matAlopecia.cols(), matAlopecia.rows(), Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(matAlopecia, bmpAlopecia);
                } catch (Exception e) {
                    Log.e("error", e.toString());
                }
            }

            ((ImageView)layout.findViewById(R.id.img_alopecia)).setImageBitmap(bmpAlopecia);
            ((TextView)layout.findViewById(R.id.tv_alopecia)).setText("점수 : " + alopeciaScore);
            return layout;
        }

    }

    public static class ProcessKeratinFragment extends Fragment {

        public ProcessKeratinFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_keratin, container, false);

            if(bmpKeratin == null) {
                try {
                    bmpKeratin = Bitmap.createBitmap(matKeratin.cols(), matKeratin.rows(), Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(matKeratin, bmpKeratin);
                } catch (Exception e) {
                    Log.e("error", e.toString());
                }
            }

            ((ImageView)layout.findViewById(R.id.img_keratin)).setImageBitmap(bmpKeratin);
            ((TextView)layout.findViewById(R.id.tv_keratin)).setText("점수 : " + keratinScore);
            return layout;
        }

    }

    public static class ProcessResultFragment  extends Fragment {

        public ProcessResultFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_result, container, false);
            ((TextView)layout.findViewById(R.id.tv_result)).setText("블라블라");
            return layout;
        }
    }

}