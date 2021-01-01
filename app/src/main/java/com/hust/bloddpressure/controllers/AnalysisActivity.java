package com.hust.bloddpressure.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.hust.bloddpressure.R;
import com.hust.bloddpressure.model.entities.InforStaticClass;
import com.hust.bloddpressure.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class AnalysisActivity extends AppCompatActivity implements OnChartValueSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        initView();
    }

    private void initView() {
        int rule = InforStaticClass.getRule();
        String userId = InforStaticClass.getUserId();
        if (Constant.USER_RULE == rule) {

            initViewMaxChart();
            initViewMinChart();
        }

    }

    private void initViewMinChart() {
        CombinedChart minChart = findViewById(R.id.combined_chart_min);
        minChart.getDescription().setEnabled(false);
        minChart.setBackgroundColor(Color.WHITE);
        minChart.setDrawGridBackground(true);
        minChart.setDrawBarShadow(false);
        minChart.setHighlightFullBarEnabled(false);
        minChart.setOnChartValueSelectedListener(this);

        YAxis rightAxis = minChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(40f);

        YAxis leftAxis = minChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(40f);

        final List<String> xLabel = new ArrayList<>();
        xLabel.add("Lần 1");
        xLabel.add("Lần 2");
        xLabel.add("Lần 3");
        xLabel.add("Lần 4");
        xLabel.add("Lần 5");
        xLabel.add("Lần 6");
        xLabel.add("Lần 7");
        xLabel.add("Lần 8");
        xLabel.add("Lần 9");
        xLabel.add("Lần 10");
        xLabel.add("Lần 11");
        xLabel.add("Lần 12");


        XAxis xAxis = minChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xLabel.get((int) value % xLabel.size());
            }
        });
        CombinedData dataMin = new CombinedData();
        LineData lineDatasMin = new LineData();
        lineDatasMin.addDataSet((ILineDataSet) dataChartMin());
        dataMin.setData(lineDatasMin);
        xAxis.setAxisMaximum(dataMin.getXMax() + 0.25f);
        minChart.setData(dataMin);
        minChart.invalidate();
    }

    private void initViewMaxChart() {
        CombinedChart maxChart = findViewById(R.id.combined_chart_max);
        maxChart.getDescription().setEnabled(false);
        maxChart.setBackgroundColor(Color.WHITE);
        maxChart.setDrawGridBackground(true);
        maxChart.setDrawBarShadow(false);
        maxChart.setHighlightFullBarEnabled(false);
        maxChart.setOnChartValueSelectedListener(this);

        YAxis rightAxis = maxChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(80f);

        YAxis leftAxis = maxChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(80f);

        final List<String> xLabel = new ArrayList<>();
        xLabel.add("Lần 1");
        xLabel.add("Lần 2");
        xLabel.add("Lần 3");
        xLabel.add("Lần 4");
        xLabel.add("Lần 5");
        xLabel.add("Lần 6");
        xLabel.add("Lần 7");
        xLabel.add("Lần 8");
        xLabel.add("Lần 9");
        xLabel.add("Lần 10");
        xLabel.add("Lần 11");
        xLabel.add("Lần 12");


        XAxis xAxis = maxChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xLabel.get((int) value % xLabel.size());
            }
        });
        CombinedData dataMax = new CombinedData();
        LineData lineDatasMax = new LineData();
        lineDatasMax.addDataSet((ILineDataSet) dataChartMax());
        dataMax.setData(lineDatasMax);
        xAxis.setAxisMaximum(dataMax.getXMax() + 0.25f);
        maxChart.setData(dataMax);
        maxChart.invalidate();

    }

    private DataSet dataChartMax() {
        LineData d = new LineData();
        int[] dataMax = new int[]{125, 123, 121, 110, 110, 125, 130, 119, 115, 122, 125, 120};
        ArrayList<Entry> entriesMax = new ArrayList<>();
        for (int index = 0; index < dataMax.length; index++) {
            entriesMax.add(new Entry(index, dataMax[index]));
        }
        LineDataSet set = new LineDataSet(entriesMax, Constant.LABEL_CHART_MAX);
        set.setColor(Color.GREEN);
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.GREEN);
        set.setCircleRadius(5f);
        set.setFillColor(Color.GREEN);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.GREEN);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);
        return set;
    }

    private DataSet dataChartMin() {
        LineData d = new LineData();
        int[] dataMin = new int[]{60, 65, 66, 70, 75, 70, 80, 64, 71, 65, 78, 80};
        ArrayList<Entry> entriesMin = new ArrayList<>();

        for (int index = 0; index < dataMin.length; index++) {
            entriesMin.add(new Entry(index, dataMin[index]));
        }

        LineDataSet set = new LineDataSet(entriesMin, Constant.LABEL_CHART_MIN);
        set.setColor(Color.RED);
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.RED);
        set.setCircleRadius(5f);
        set.setFillColor(Color.RED);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.RED);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return set;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Toast.makeText(this, "Value: "
                + e.getY()
                + ", index: "
                + h.getX()
                + ", DataSet index: "
                + h.getDataSetIndex(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected() {

    }
}