package com.soongsil.alopeciadetect.views;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.realm.implementation.RealmLineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
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
import com.soongsil.alopeciadetect.objects.AlopeciaRealmObj;
import com.soongsil.alopeciadetect.objects.KeratinRealmObj;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by Park on 2017-12-03.
 */

public class HistoryActivity extends AppCompatActivity {

    private Realm realm;
    private static RealmResults<KeratinRealmObj> keratinResults;
    private static RealmResults<AlopeciaRealmObj> alopeciaResults;

    private LineChart chartAlopecia, chartKeratin;
    private TextView emptyData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        initialize();
    }

    public void initialize() {


        realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        keratinResults = realm.where(KeratinRealmObj.class).findAll();
        alopeciaResults = realm.where(AlopeciaRealmObj.class).findAll();

        realm.commitTransaction();

        chartAlopecia = findViewById(R.id.chart_alopecia);
        chartKeratin = findViewById(R.id.chart_keratin);
        emptyData = findViewById(R.id.empty_data);

        if(keratinResults.size() != 0) {

            emptyData.setVisibility(View.GONE);

            final List<Entry> alopeciaEntry = new ArrayList<>();
            List<Entry> keratinEntry = new ArrayList<>();

            for (int i = 0; i < alopeciaResults.size(); ++i) {
                alopeciaEntry.add(new Entry(i, alopeciaResults.get(i).getScore()));
            }
            for (int i = 0; i < keratinResults.size(); ++i) {
                keratinEntry.add(new Entry(i, keratinResults.get(i).getScore()));
            }

            LineDataSet lineDataSet1 = new LineDataSet(alopeciaEntry, "Alopecia");
            lineDataSet1.setLineWidth(2);
            lineDataSet1.setCircleRadius(6);
            lineDataSet1.setCircleColor(Color.parseColor("#FFA1B4DC"));
            lineDataSet1.setCircleColorHole(Color.BLUE);
            lineDataSet1.setColor(Color.parseColor("#FFA1B4DC"));
            lineDataSet1.setDrawCircleHole(true);
            lineDataSet1.setDrawCircles(true);
            lineDataSet1.setDrawHorizontalHighlightIndicator(false);
            lineDataSet1.setDrawHighlightIndicators(false);
            lineDataSet1.setDrawValues(false);

            LineDataSet lineDataSet2 = new LineDataSet(keratinEntry, "Keratin");
            lineDataSet2.setLineWidth(2);
            lineDataSet2.setCircleRadius(6);
            lineDataSet2.setCircleColor(Color.parseColor("#FFA1B4DC"));
            lineDataSet2.setCircleColorHole(Color.BLUE);
            lineDataSet2.setColor(Color.parseColor("#FFA1B4DC"));
            lineDataSet2.setDrawCircleHole(true);
            lineDataSet2.setDrawCircles(true);
            lineDataSet2.setDrawHorizontalHighlightIndicator(false);
            lineDataSet2.setDrawHighlightIndicators(false);
            lineDataSet2.setDrawValues(false);


            LineData lineData1 = new LineData(lineDataSet1);
            chartAlopecia.setData(lineData1);
            LineData lineData2 = new LineData(lineDataSet2);
            chartKeratin.setData(lineData2);


            XAxis xAxis1 = chartAlopecia.getXAxis();
            xAxis1.setDrawLabels(false);
            xAxis1.setDrawAxisLine(false);
            xAxis1.setDrawGridLines(false);

            XAxis xAxis2 = chartKeratin.getXAxis();
            xAxis2.setDrawLabels(false);
            xAxis2.setDrawAxisLine(false);
            xAxis2.setDrawGridLines(false);


            YAxis yRAxis1 = chartAlopecia.getAxisRight();
            yRAxis1.setDrawLabels(false);
            yRAxis1.setDrawAxisLine(false);
            yRAxis1.setDrawGridLines(false);

            Description description1 = new Description();
            description1.setText("높을수록 좋습니다");


            YAxis yRAxis2 = chartKeratin.getAxisRight();
            yRAxis2.setDrawLabels(false);
            yRAxis2.setDrawAxisLine(false);
            yRAxis2.setDrawGridLines(false);

            Description description2 = new Description();
            description2.setText("낮을수록 좋습니다");

            MyMarkerView marker1 = new MyMarkerView(this, R.layout.marker_text, keratinResults, alopeciaResults, false);
            marker1.setChartView(chartAlopecia);
            chartAlopecia.setMarker(marker1);

            MyMarkerView marker2 = new MyMarkerView(this, R.layout.marker_text, keratinResults, alopeciaResults, true);
            marker2.setChartView(chartKeratin);
            chartKeratin.setMarker(marker2);

            chartAlopecia.setDoubleTapToZoomEnabled(false);
            chartAlopecia.setDrawGridBackground(false);
            chartAlopecia.setDescription(description1);
            chartAlopecia.animateY(1000, Easing.EasingOption.EaseInCubic);
            chartAlopecia.invalidate();

            chartKeratin.setDoubleTapToZoomEnabled(false);
            chartKeratin.setDrawGridBackground(false);
            chartKeratin.setDescription(description2);
            chartKeratin.animateY(1000, Easing.EasingOption.EaseInCubic);
            chartKeratin.invalidate();
        }
        else {
            emptyData.setVisibility(View.VISIBLE);
        }

        /*
        try {


            ArrayList<String> xVals = new ArrayList<String>();
            xVals.add("1.0");
            xVals.add("1.0");
            xVals.add("1.0");
            xVals.add("1.0");
            xVals.add("1.0");

            LineData lineData = new LineData(lineDataSet);

            lineChart.setData(lineData);

            XAxis xAxis = lineChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTextColor(Color.BLACK);
            xAxis.setDrawGridLines(false);
//            xAxis.enableGridDashedLine(8, 24, 0);

            YAxis yLAxis = lineChart.getAxisLeft();
            yLAxis.setTextColor(Color.BLACK);

            YAxis yRAxis = lineChart.getAxisRight();
            yRAxis.setDrawLabels(false);
            yRAxis.setDrawAxisLine(false);
            yRAxis.setDrawGridLines(false);

            Description description = new Description();
            description.setText("");

            lineChart.setDoubleTapToZoomEnabled(false);
            lineChart.setDrawGridBackground(false);
            lineChart.setDescription(description);
            lineChart.animateY(1000, Easing.EasingOption.EaseInCubic);
            lineChart.invalidate();


        }
        catch (Exception e) {
            Log.e("exception", e.toString());
        }
        */
    }

//    private class PagerAdapterClass extends FragmentStatePagerAdapter {
//
//        public PagerAdapterClass(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            Fragment frag = null;
//
//            switch(position) {
//                case 0:
//                    frag = new HistoryAlopeciaFragment();
//                    break;
//                case 1:
//                    frag = new HistoryKeratinFragment();
//                    break;
//            }
//
//            return frag;
//        }
//
//        @Override
//        public int getCount() {
//            return 2;
//        }
//    }
//
//    public static class HistoryAlopeciaFragment extends Fragment {
//
//        public HistoryAlopeciaFragment() {
//        }
//
//        @Override
//        public void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//            LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_history_alopecia, container, false);
//
//            LineChart lineChart = (LineChart) layout.findViewById(R.id.chart_alopecia);
//
//            RealmLineDataSet<AlopeciaRealmObj> lineDataSet = new RealmLineDataSet<AlopeciaRealmObj>(alopeciaResults, "result", "score");
//            lineDataSet.setLineWidth(2);
//            lineDataSet.setCircleRadius(6);
//            lineDataSet.setCircleColor(Color.parseColor("#FFA1B4DC"));
//            lineDataSet.setCircleColorHole(Color.BLUE);
//            lineDataSet.setColor(Color.parseColor("#FFA1B4DC"));
//            lineDataSet.setDrawCircleHole(true);
//            lineDataSet.setDrawCircles(true);
//            lineDataSet.setDrawHorizontalHighlightIndicator(false);
//            lineDataSet.setDrawHighlightIndicators(false);
//            lineDataSet.setDrawValues(false);
//            lineDataSet.setLabel("AlopeciaScore");
//
//            ArrayList<ILineDataSet> dataSetList = new ArrayList<ILineDataSet>();
//            dataSetList.add(lineDataSet);
//
//            LineData data = new LineData(dataSetList);
//
//            lineChart.setData(data);
//
//            XAxis xAxis = lineChart.getXAxis();
//            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//            xAxis.setTextColor(Color.BLACK);
//            xAxis.setValueFormatter(new IAxisValueFormatter() {
//                @Override
//                public String getFormattedValue(float value, AxisBase axis) {
//                    return alopeciaResults.get((int) value % alopeciaResults.size()).getDate();
//                }
//            });
//
//            YAxis yRAxis = lineChart.getAxisRight();
//            yRAxis.setDrawLabels(false);
//            yRAxis.setDrawAxisLine(false);
//            yRAxis.setDrawGridLines(false);
//
//            Description description = new Description();
//            description.setText("Alopecia Score");
//            lineChart.setDoubleTapToZoomEnabled(false);
//            lineChart.setDrawGridBackground(false);
//            lineChart.setDescription(description);
//            lineChart.animateY(1000, Easing.EasingOption.EaseInCubic);
//
//            lineChart.refreshDrawableState();
//
//            lineChart.invalidate();
//
//            return layout;
//        }
//
//    }
//
//    public static class HistoryKeratinFragment extends Fragment {
//
//        public HistoryKeratinFragment() {
//        }
//
//        @Override
//        public void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//           LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_history_keratin, container, false);
//
//            LineChart lineChart = ((LineChart)layout.findViewById(R.id.chart_keratin));
//
//            RealmLineDataSet<KeratinRealmObj> lineDataSet = new RealmLineDataSet<KeratinRealmObj>(keratinResults, "result", "score");
//            lineDataSet.setLineWidth(2);
//            lineDataSet.setCircleRadius(6);
//            lineDataSet.setCircleColor(Color.parseColor("#FFA1B4DC"));
//            lineDataSet.setCircleColorHole(Color.BLUE);
//            lineDataSet.setColor(Color.parseColor("#FFA1B4DC"));
//            lineDataSet.setDrawCircleHole(true);
//            lineDataSet.setDrawCircles(true);
//            lineDataSet.setDrawHorizontalHighlightIndicator(false);
//            lineDataSet.setDrawHighlightIndicators(false);
//            lineDataSet.setDrawValues(false);
//            lineDataSet.setLabel("KeratinScore");
//
//
//            ArrayList<ILineDataSet> dataSetList = new ArrayList<ILineDataSet>();
//            dataSetList.add(lineDataSet);
//
//            LineData data = new LineData(dataSetList);
//
//            lineChart.setData(data);
//
//            XAxis xAxis = lineChart.getXAxis();
//            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//            xAxis.setTextColor(Color.BLACK);
//            xAxis.setValueFormatter(new IAxisValueFormatter() {
//                @Override
//                public String getFormattedValue(float value, AxisBase axis) {
//                    return keratinResults.get((int) value % keratinResults.size()).getDate();
//                }
//            });
//
//            YAxis yRAxis = lineChart.getAxisRight();
//            yRAxis.setDrawLabels(false);
//            yRAxis.setDrawAxisLine(false);
//            yRAxis.setDrawGridLines(false);
//
//            Description description = new Description();
//            description.setText("Keratin score");
//            lineChart.setDoubleTapToZoomEnabled(false);
//            lineChart.setDrawGridBackground(false);
//            lineChart.setDescription(description);
//            lineChart.animateY(1000, Easing.EasingOption.EaseInCubic);
//
//            lineChart.refreshDrawableState();
//
//            lineChart.invalidate();
//
//            return layout;
//        }
//
//    }

}
