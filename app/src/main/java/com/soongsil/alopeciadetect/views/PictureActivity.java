package com.soongsil.alopeciadetect.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.soongsil.alopeciadetect.R;

/**
 * Created by Park on 2017-11-28.
 */

public class PictureActivity extends AppCompatActivity {

    private Bitmap picture;
    private ImageView headImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        initialize();
    }

    private void initialize() {
        headImage = findViewById(R.id.head_picture);

        Intent it = getIntent();
        byte[] bytes = it.getByteArrayExtra("picture");
        picture = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        headImage.setImageBitmap(picture);
    }
}
