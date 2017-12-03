package com.soongsil.alopeciadetect.views;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.soongsil.alopeciadetect.R;
import com.soongsil.alopeciadetect.objects.AlopeciaRealmObj;
import com.soongsil.alopeciadetect.objects.KeratinRealmObj;

import io.realm.RealmResults;

/**
 * Created by Park on 2017-12-04.
 */

public class MyMarkerView extends MarkerView {

    private TextView tvContent;
    private RealmResults<KeratinRealmObj> keratinResults;
    private RealmResults<AlopeciaRealmObj> alopeciaResults;
    private boolean flag;

    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        tvContent = (TextView)findViewById(R.id.tvContent);
    }

    public MyMarkerView(Context context, int layoutResource, RealmResults<KeratinRealmObj> keratinResults, RealmResults<AlopeciaRealmObj> alopeciaResults, boolean flag) {
        super(context, layoutResource);

        tvContent = (TextView)findViewById(R.id.tvContent);

        this.flag = flag;


        if(flag)
            this.keratinResults = keratinResults;
        else
            this.alopeciaResults = alopeciaResults;

    }


    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        if (e instanceof CandleEntry) {

            CandleEntry ce = (CandleEntry) e;

            int x = (int)ce.getX();

            if(flag)
                tvContent.setText("" + keratinResults.get(x).getDate());
            else
                tvContent.setText("" + alopeciaResults.get(x).getDate());

        } else {

            int x = (int)e.getX();


            if(flag)
                tvContent.setText("" + keratinResults.get(x).getDate());
            else
                tvContent.setText("" + alopeciaResults.get(x).getDate());
        }

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}