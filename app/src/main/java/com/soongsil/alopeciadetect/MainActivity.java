package com.soongsil.alopeciadetect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.soongsil.alopeciadetect.views.HistoryActivity;
import com.soongsil.alopeciadetect.views.ProcessActivity;
import com.soongsil.alopeciadetect.views.QuestionActivity;
import com.wang.avi.AVLoadingIndicatorView;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import static com.soongsil.alopeciadetect.utils.RequestCode.GALLERY_REQUEST_CODE;
import static com.soongsil.alopeciadetect.utils.RequestCode.QUESTION_REQUEST_CODE;


public class MainActivity extends AppCompatActivity {

    private int[] score;
    private Uri uri;

    private FloatingActionMenu floatingMenu;
    private FloatingActionButton rewriteBtn, toGallery, toAnalyze;

    private Bitmap headPicture;
    private ImageView headImgView;
    private TextView headHint;
    private AVLoadingIndicatorView avLoading;

    private SharedPreferences sp;

    public native void ConvertRGBtoGray(long matAddrInput, long matAddrResult);
    public native int IsKeratin(long matAddrInput, long matAddrResult);
    public native int IsAlopecia(long matAddrInput, long matAddrResult);
    static {
        System.loadLibrary("opencv_java3");
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initial();

        boolean hasVisted = sp.getBoolean("hasVisited", false);
        if(!hasVisted) {

            Intent questionIntent = new Intent(getApplicationContext(), QuestionActivity.class);
            startActivityForResult(questionIntent, QUESTION_REQUEST_CODE);

        }
    }

    private void initial() {

        headPicture = null;
        uri = null;

        Realm.init(this);
        RealmConfiguration realmConf = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConf);

        headImgView = findViewById(R.id.head_picture);
        floatingMenu = findViewById(R.id.floating_menu);
        rewriteBtn = findViewById(R.id.floating_rewrite);
        toGallery = findViewById(R.id.floating_gallery);
        toAnalyze = findViewById(R.id.floating_history);
        headHint = findViewById(R.id.head_picture_hint);
        avLoading = findViewById(R.id.avi);
        avLoading.hide();

        toGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALLERY_REQUEST_CODE);
            }
        });
        rewriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent questionIntent = new Intent(getApplicationContext(), QuestionActivity.class);
                startActivityForResult(questionIntent, QUESTION_REQUEST_CODE);
            }
        });
        toAnalyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent historyIntent = new Intent(getApplicationContext(), HistoryActivity.class);
                startActivity(historyIntent);
            }
        });

        sp = getSharedPreferences("firstflag", Context.MODE_PRIVATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }

        if (requestCode == QUESTION_REQUEST_CODE) {

            try {

                SharedPreferences.Editor e = sp.edit();
                e.putBoolean("hasVisited", true);
                e.commit();

                Bundle bundle = data.getExtras().getBundle("data");
                score = bundle.getIntArray("score");

                floatingMenu.close(true);

            } catch (Exception e) {
                Log.e("error", e.toString());
            }
        }
        else if(requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            try {

                uri = data.getData();
                headPicture = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                headHint.setText("");
                headImgView.setImageBitmap(headPicture);

                floatingMenu.close(true);

                avLoading.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent processIntent = new Intent(getApplicationContext(), ProcessActivity.class);
                        processIntent.putExtra("uri", uri.toString());
                        startActivity(processIntent);

                        avLoading.hide();
                    }
                }, 2000);


            } catch (Exception e) {
                Log.e("test", e.getMessage());
            }
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
            finish();
            toast.cancel();
        }
    }
}