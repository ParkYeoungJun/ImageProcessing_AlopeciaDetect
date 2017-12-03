package com.soongsil.alopeciadetect.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soongsil.alopeciadetect.R;
import com.soongsil.alopeciadetect.objects.AlopeciaRealmObj;
import com.soongsil.alopeciadetect.objects.KeratinRealmObj;
import com.soongsil.alopeciadetect.objects.QuestionRealmObj;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;

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
    private static float keratinClass;
    private static float alopeciaClass;

    private static Realm realm;

    private ViewPager viewPager;
    private ImageButton btnKeratin;
    private ImageButton btnAlopecia;
    private ImageButton btnResult;

    public native int IsKeratin(long matAddrInput, long matAddrResult);
    public native int IsAlopecia(long matAddrInput, long matAddrResult);
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
        btnKeratin = (ImageButton) findViewById(R.id.btn_keratin);
        btnAlopecia = (ImageButton) findViewById(R.id.btn_alopecia);
        btnResult = (ImageButton) findViewById(R.id.btn_result);

        Intent intent = getIntent();

        Uri uri = Uri.parse(intent.getStringExtra("uri"));
        try {

            Bitmap headPicture = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            Mat matInput = new Mat();
            Bitmap bmp = headPicture.copy(Bitmap.Config.ARGB_8888, true);
            Utils.bitmapToMat(bmp, matInput);

            matKeratin = new Mat(matInput.rows(), matInput.cols(), matInput.type());
            matAlopecia = new Mat(matInput.rows(), matInput.cols(), matInput.type());

            keratinScore = 0;
            alopeciaScore = 0;

            keratinScore = IsKeratin(matInput.getNativeObjAddr(), matKeratin.getNativeObjAddr());
            alopeciaScore = IsAlopecia(matInput.getNativeObjAddr(), matAlopecia.getNativeObjAddr());


            bmpAlopecia = Bitmap.createBitmap(matAlopecia.cols(), matAlopecia.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(matAlopecia, bmpAlopecia);
            bmpKeratin = Bitmap.createBitmap(matKeratin.cols(), matKeratin.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(matKeratin, bmpKeratin);

            if(keratinScore < 250)
                keratinClass = 1;
            else if(keratinScore < 500)
                keratinClass = 2;
            else
                keratinClass = 3;

            if(alopeciaScore > 50000)
                alopeciaClass = 1;
            else if(alopeciaScore > 20000)
                alopeciaClass = 2;
            else
                alopeciaClass = 3;

            Calendar calendar = Calendar.getInstance();

            int month = calendar.get(Calendar.MONTH) + 1;
            String date =  month + "/" + calendar.get(Calendar.DAY_OF_MONTH);

            realm = Realm.getDefaultInstance();
            realm.beginTransaction();

            KeratinRealmObj keratinObj = new KeratinRealmObj(date, keratinClass, keratinScore);
            AlopeciaRealmObj alopeciaObj = new AlopeciaRealmObj(date, alopeciaClass, alopeciaScore);

            realm.copyToRealm(keratinObj);
            realm.copyToRealm(alopeciaObj);

            realm.commitTransaction();


        } catch (Exception e) {
            Log.e("bitmap", e.toString());
        }

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

        viewPager.setAdapter(new PagerAdapterClass(getSupportFragmentManager()));
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

            String str = "탈모 등급 : ";
            if(alopeciaClass == 1)
                str += "양호\n";
            else if(alopeciaClass == 2)
                str += "주의\n";
            else if(alopeciaClass == 3)
                str += "위험\n";

            str += "각질 등급 : ";

            if(keratinClass == 1)
                str += "양호\n";
            else if(keratinClass == 2)
                str += "주의\n";
            else if(keratinClass == 3)
                str += "위험\n";


            realm.beginTransaction();
            RealmResults<QuestionRealmObj> queList = realm.where(QuestionRealmObj.class).findAll();
            realm.commitTransaction();

            String queStr = queList.last().getScore();

            if(queStr.charAt(0) == '1') {
                if(alopeciaClass == 1)
                    str += "이마 부위 탈모가 있을 수 있으며, ";
                else if(alopeciaClass == 2)
                    str += "이마 부위 탈모가 의심 되며, ";
                else if(alopeciaClass == 3)
                    str += "이마 부위 탈모가 의심 되며, ";
            }

            int sum = queStr.charAt(1) - 48 + queStr.charAt(2) - 48 + queStr.charAt(3) - 48 + queStr.charAt(4) - 48;

            if(sum >= 3) {
                if(alopeciaClass == 1)
                    str += "모발의 상태는 좋지만 탈모 위험군에 속하며 병원 내진이 필요합니다.\n";
                else if(alopeciaClass == 2)
                    str += "모발의 상태도 주의가 필요하며 탈모 위험군에 속해 병원 내진이 필요합니다.\n";
                else if(alopeciaClass == 3)
                    str += "모발의 상태가 위험 수준이며 탈모 위험군으로 병원 내진이 필요합니다.\n";
            }
            else if(sum >= 1) {
                if(alopeciaClass == 1)
                    str += "모발의 상태는 좋지만 탈모에 주의가 필요합니다.\n";
                else if(alopeciaClass == 2)
                    str += "모발의 상태도 주의가 필요하며 탈모에도 안전하지 않습니다.\n";
                else if(alopeciaClass == 3)
                    str += "모발의 상태가 위험 수준이며 탈모 진단이 필요합니다.\n";
            }
            else {
                if(alopeciaClass == 1)
                    str += "모발의 상태와 같이 탈모의 위험이 적습니다.\n";
                else if(alopeciaClass == 2)
                    str += "탈모의 위험이 적지만 모발의 상태로 볼 때 주의가 필요합니다.\n";
                else if(alopeciaClass == 3)
                    str += "모발의 상태로 볼 때 탈모 진단이 필요합니다.\n";
            }

            if(keratinClass == 1)
                str += "두피 각질은 양호한 편입니다.\n";
            else if(keratinClass == 2)
                str += "두피 각질에 주의가 필요한 상태입니다.\n";
            else if(keratinClass == 3)
                str += "두피 각질이 위험 수준이며 병원 내진이 필요합니다.\n";


            if(queStr.charAt(5) == '1') {
                str += "또한, 모근에 영향공급이 원할하지 않습니다.\n";
            }
            if(queStr.charAt(6) == '1') {
                str += "두피열에 의해 혈액순환이 원할하지 않습니다.";
            }

            ((TextView)layout.findViewById(R.id.tv_result)).setText(str);
            return layout;
        }
    }

}