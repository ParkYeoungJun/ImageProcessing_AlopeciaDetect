package com.soongsil.alopeciadetect.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.soongsil.alopeciadetect.R;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.ByteArrayOutputStream;
import java.net.URI;

/**
 * Created by Park on 2017-11-28.
 */

public class PictureActivity extends AppCompatActivity {
    // TODO: Try Morphology function


    private Bitmap picOrigin, picGrey, picBinary, picEdge;
    private ImageView headImage;
    private Mat matOrigin, matGrey, matBinary, matEdge;

    private Button toGrey;
    private Button toBInary;
    private Button toEdge;

    /*
     * Call OpenCV library
     */
    public native void ConvertRGBtoGray(long matAddrInput, long matAddrResult);
    public native void Segmentation(long matAddrInput, long matAddrResult);
    public native void MorphologyOpening(long matAddrInput, long matAddrResult);
    public native void MorphologyClosing(long matAddrInput, long matAddrResult);
    public native void CanyEdgeDetect(long matAddrInput, long matAddrResult, int lowThreshold,
                                      int ratio, int kernel_size);

    /*
     * Native library
     */


    static {
        System.loadLibrary("opencv_java3");
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        initialize();
    }

    private void initialize() {

        picGrey = null;
        picEdge = null;
        toGrey = findViewById(R.id.btn_grey);
        toEdge = findViewById(R.id.btn_edge);
        toBInary = findViewById(R.id.btn_binary);

        try {

            headImage = findViewById(R.id.head_picture);

            Bundle it = getIntent().getExtras();
            Uri uri = Uri.parse(it.getString("uri"));

            picOrigin = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            headImage.setImageBitmap(picOrigin);


            /*
             *  Create Mat object
             */

            matOrigin = new Mat();
            Bitmap bmpTemp = picOrigin.copy(Bitmap.Config.ARGB_8888, true);
            Utils.bitmapToMat(bmpTemp, matOrigin);

            if ( matGrey != null ) matGrey.release();
            matGrey = new Mat(matOrigin.rows(), matOrigin.cols(), matOrigin.type());

            if ( matEdge != null ) matEdge.release();
            matEdge = new Mat(matOrigin.rows(), matOrigin.cols(), matOrigin.type());

            if ( matBinary != null ) matBinary.release();
            matBinary = new Mat(matOrigin.rows(), matOrigin.cols(), matOrigin.type());
        }
        catch (Exception e) {
            Log.e("error", ""+e);
        }


        toGrey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(picGrey == null) {
                    ConvertRGBtoGray(matOrigin.getNativeObjAddr(), matGrey.getNativeObjAddr());
                    picGrey = Bitmap.createBitmap(matGrey.cols(), matGrey.rows(), Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(matGrey, picGrey);
                    headImage.setImageBitmap(picGrey);
                } else {
                    headImage.setImageBitmap(picGrey);
                }

            }
        });
        toBInary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(picGrey != null) {
                    if (picBinary == null) {
                        Segmentation(matGrey.getNativeObjAddr(), matBinary.getNativeObjAddr());
                        picBinary = Bitmap.createBitmap(matBinary.cols(), matBinary.rows(), Bitmap.Config.ARGB_8888);
                        Utils.matToBitmap(matBinary, picBinary);
                        headImage.setImageBitmap(picBinary);
                    } else {
                        headImage.setImageBitmap(picBinary);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please create Greyscale", Toast.LENGTH_LONG).show();
                }
            }
        });
        toEdge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(picGrey != null) {
                    if (picEdge == null) {
                        CanyEdgeDetect(matGrey.getNativeObjAddr(), matEdge.getNativeObjAddr()
                                , 80, 3, 3);
                        picEdge = Bitmap.createBitmap(matEdge.cols(), matEdge.rows(), Bitmap.Config.ARGB_8888);
                        Utils.matToBitmap(matEdge, picEdge);
                        headImage.setImageBitmap(picEdge);
                    } else {
                        headImage.setImageBitmap(picEdge);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please create Greyscale", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
