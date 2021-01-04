package com.hust.bloddpressure.controllers;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.hust.bloddpressure.R;
import com.hust.bloddpressure.model.MyService;
import com.hust.bloddpressure.model.entities.BloodPressureInfor;
import com.hust.bloddpressure.model.entities.InforStaticClass;
import com.hust.bloddpressure.model.entities.Predict;
import com.hust.bloddpressure.util.Constant;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AnalysisActivity extends AppCompatActivity implements OnChartValueSelectedListener {
    private int rule;
    ProgressDialog pDialog;
    ArrayList<BloodPressureInfor> listPressure;
    ArrayList<Predict> listPredict;
    PieChart mChart, pieAge1, pieAge2, pieAge3;
    ToggleButton btn_toggle;
    ArrayList<Predict> listMax, listMin, listNormal;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get rule and check
        rule = InforStaticClass.getRule();
        if (rule == Constant.ADMIN_RULE) {
            // if it is admin rule
            setContentView(R.layout.activity_analysis_manager);
            // find view by id
            findViewByIdManager();
            // set listener for button
            setListenButton();
            listPredict = new ArrayList<>();
            listMax = new ArrayList<>();
            listMin = new ArrayList<>();
            listNormal = new ArrayList<>();
            initViewManager();
        } else {
            setContentView(R.layout.activity_analysis);
            initViewUser();
        }

    }

    /**
     * Find view by id in case of manager
     */
    private void findViewByIdManager() {
        btn_toggle = findViewById(R.id.btn_toggle);
        mChart = (PieChart) findViewById(R.id.pie_chart_predict);
        pieAge1 = (PieChart) findViewById(R.id.pie_chart_age1);
        pieAge2 = (PieChart) findViewById(R.id.pie_chart_age2);
        pieAge3 = (PieChart) findViewById(R.id.pie_chart_age3);
    }

    private void setListenButton() {
        btn_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    setPieChartAgeMax();
                    setPieChartAgeMin();
                    setPieChartAgeNormal();
                    Toast.makeText(AnalysisActivity.this, "Check", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AnalysisActivity.this, "None CHecks", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    /**
     * init view for pie chart analyst by max pressure
     */
    private void setPieChartAgeNormal() {

    }
    /**
     * init view for pie chart analyst by min pressure
     */
    private void setPieChartAgeMin() {

    }

    /**
     * init view for pie chart analyst by normal pressure
     */
    private void setPieChartAgeMax() {
        pieAge1.setRotationEnabled(true);
        pieAge1.setHoleRadius(35f);
        pieAge1.setTransparentCircleAlpha(0);
        pieAge1.setCenterText(Constant.PREDICT_NAME);
        pieAge1.setCenterTextSize(10);
        pieAge1.setOnChartValueSelectedListener(this);
        ArrayList<Integer> listCount = new ArrayList<>();
//        for (Predict item : listPredict) {
//            if (item.getTypePredict()=Co)
//
//        }
//        for (int i = 0; i < listPredict.size(); i++) {
//            int count = 0;
//            for (int j = i + 1; j < listPredict.size(); j++) {
//                if (listPredict.get(i) == listPredict.get(j))
//                    count++;
//            }
//            listCount.add(count);
//
//        }
        TextView t = findViewById(R.id.empty);
        t.setText(listMax.size() +" " +listMin.size()+" " + listNormal.size());
    }

    /**
     * init view in case of admin
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    private void initViewManager() {
        mChart.setRotationEnabled(true);
        mChart.setHoleRadius(35f);
        mChart.setTransparentCircleAlpha(0);
        mChart.setCenterText(Constant.PREDICT_NAME);
        mChart.setCenterTextSize(10);
        mChart.setOutlineAmbientShadowColor(Color.rgb(238, 238, 238));
        mChart.setOnChartValueSelectedListener(this);
//        mChart.setDrawEntryLabels(true);
        GetListTypePredict getListTypePredict = new GetListTypePredict();
        getListTypePredict.execute();
    }

    /**
     * init view in case of user
     */
    private void initViewUser() {
        String userId = InforStaticClass.getUserId();
        listPressure = new ArrayList<>();
        GetListPressure getListPressure = new GetListPressure(userId);
        getListPressure.execute();

    }
    /**
     * init view for min pressure chart in case of user rule
     */
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
        for (int i = listPressure.size() - 1; i >= 0; i--) {
            xLabel.add(listPressure.get(i).getBloodPressureId() + Constant.EMPTY);
        }

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

    /**
     * init view for max pressure chart in case of user rule
     */
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
        for (int i = listPressure.size() - 1; i >= 0; i--) {
            xLabel.add(listPressure.get(i).getBloodPressureId() + Constant.EMPTY);
        }
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

    /**
     * Set data for Pressure Max Chart
     * @return data set
     */
    private DataSet dataChartMax() {
        LineData d = new LineData();
        ArrayList<Entry> entriesMax = new ArrayList<>();
        for (int i = 0; i < listPressure.size(); i++) {
            entriesMax.add(new Entry(i, listPressure.get(listPressure.size() - 1 - i).getPressureMax()));
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
    /**
     * Set data for Pressure Min Chart
     * @return data set
     */
    private DataSet dataChartMin() {
        LineData d = new LineData();
        ArrayList<Entry> entriesMin = new ArrayList<>();
        for (int i = 0; i < listPressure.size(); i++) {
            entriesMin.add(new Entry(i, listPressure.get(listPressure.size() - 1 - i).getPressureMin()));
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
    public void onNothingSelected() {
    }

    /**
     * Class get pressure information
     */
    @SuppressLint("StaticFieldLeak")
    class GetListPressure extends AsyncTask {
        private String userId;

        public GetListPressure(String userId) {
            this.userId = userId;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            MyService jsonParser = new MyService();
            List<NameValuePair> args = new ArrayList<>();
            args.add(new BasicNameValuePair(Constant.USER_ID, userId));
            String json = jsonParser.callService(Constant.URL_LIST_PRESSURE, MyService.GET, args);
            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    JSONArray jsonArrayBloodPressure = jsonObj.getJSONArray(Constant.OBJECT_JSON_LIST_PRESSURE);
                    for (int i = 0; i < jsonArrayBloodPressure.length(); i++) {
                        JSONObject obj = (JSONObject) jsonArrayBloodPressure.get(i);
                        @SuppressLint("SimpleDateFormat") BloodPressureInfor bloodPressureInfor;
                        bloodPressureInfor = new BloodPressureInfor(obj.getInt(Constant.PRESSURE_ID), obj.getInt(Constant.PRESSURE_MAX), obj.getInt(Constant.PRESSURE_MIN), new SimpleDateFormat(Constant.DATE_FORMAT).parse(obj.getString(Constant.TIME)));
                        listPressure.add(bloodPressureInfor);
                    }
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e(Constant.LOG_JSON, Constant.MSG_JSON);
            }
            return null;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AnalysisActivity.this);
            pDialog.setMessage(Constant.MESSAGE_LOADING);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Object o) {
            if (pDialog.isShowing())
                pDialog.dismiss();
            initViewMaxChart();
            initViewMinChart();
        }
    }

    /**
     * Background Get List predict
     */
    @SuppressLint("StaticFieldLeak")
    class GetListTypePredict extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            MyService jsonParser = new MyService();
            String json = jsonParser.callService(Constant.URL_LIST_TYPE_PREDICT, MyService.GET);
            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    JSONArray jsonArrayPredict = jsonObj.getJSONArray(Constant.OBJECT_JSON_LIST_PREDICT);
                    for (int i = 0; i < jsonArrayPredict.length(); i++) {
                        JSONObject obj = (JSONObject) jsonArrayPredict.get(i);
                        int predictType = obj.getInt(Constant.PREDICT_TYPE);
                        int age = obj.getInt(Constant.AGE);
                        listPredict.add(new Predict(predictType, age));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e(Constant.LOG_JSON, Constant.MSG_JSON);
            }
            return null;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AnalysisActivity.this);
            pDialog.setMessage(Constant.MESSAGE_LOADING);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (pDialog.isShowing())
                pDialog.dismiss();
            addDataSet(mChart);

        }
    }

    /**
     * Add data set for pie chart predict blood pressue
     * @param pieChart pie chart need set
     */
    private void addDataSet(PieChart pieChart) {

        ArrayList<PieEntry> yEntries = new ArrayList<>();
        ArrayList<String> xEntries = new ArrayList<>();


        int countMax = 0;
        int countMin = 0;
        for (Predict item : listPredict) {
            if (Constant.VALUE_MAX_PREDICT == item.getTypePredict()) {
                listMax.add(item);
                countMax++;
            }
            if (Constant.VALUE_MIN_PREDICT == item.getTypePredict()) {
                listMin.add(item);
                countMin++;
            }
            if (Constant.VALUE_NORMAL_PREDICT == item.getTypePredict()) {
                listNormal.add(item);
            }
        }
        int normal = listPredict.size() - (countMax + countMin);

        int[] yData = {countMax, countMin, normal};
        String[] xData = {Constant.PREDICT_MAX_NAME, Constant.PREDICT_MIN_NAME, Constant.PREDICT_NORMAL_NAME};

        for (int i = 0; i < yData.length; i++) {
            yEntries.add(new PieEntry(yData[i], i));
        }
        for (int i = 0; i < xData.length; i++) {
            xEntries.add(xData[i]);
        }
        PieDataSet pieDataSet = new PieDataSet(yEntries, Constant.PREDICT_CONTENT);
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(255, 87, 34));
        colors.add(Color.rgb(255, 193, 7));
        colors.add(Color.rgb(3, 169, 244));
        pieDataSet.setColors(colors);

        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (rule == Constant.ADMIN_RULE) {
            int value = (int) h.getX();
            String mess = "";
            if (value == Constant.VALUE_NORMAL_PREDICT) {
                mess += Constant.PREDICT_NORMAL_NAME;
            } else if (value == Constant.VALUE_MAX_PREDICT) {
                mess += Constant.PREDICT_MAX_NAME;
            } else {
                mess += Constant.PREDICT_MIN_NAME;
            }

            Toast.makeText(this, "Có: "
                            + (int) e.getY() + "/" + listPredict.size() + " người " + mess
                    , Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Chỉ số đo: "
                            + e.getY()
                            + " mmHg, index: "
                            + h.getX()
                    , Toast.LENGTH_SHORT).show();
        }

    }
}
