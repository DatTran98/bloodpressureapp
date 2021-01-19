package com.hust.bloddpressure.controllers;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class AnalysisActivity extends AppCompatActivity implements OnChartValueSelectedListener {
    private int rule;
    private ProgressDialog pDialog;
    private ArrayList<BloodPressureInfor> listPressure;
    private ArrayList<Predict> listPredict;
    private PieChart mChart, pieAge1, pieAge2, pieAge3;
    private ToggleButton btn_toggle;
    private ArrayList<Predict> listMax, listMin, listNormal;
    private Map<Integer, Integer> mapMax, mapMin, mapNormal;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private int gotData;

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
            // init chart default
            setVisibleChartAge(true);
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
        new NavigationSetting(AnalysisActivity.this);
        drawerLayout = findViewById(R.id.drawable);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.static_title);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.home:
                Intent intent = new Intent(this, MenuManagerActivity.class);
                startActivity(intent);
                return true;
            case R.id.reset:
                if (rule == Constant.ADMIN_RULE) {
                    initViewManager();
                } else {
                    setContentView(R.layout.activity_analysis);
                    initViewUser();
                }
                return true;
            case R.id.about:
                // Create about activity
                Toast.makeText(this, "About button selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.help:
                // Create help activity
                Toast.makeText(this, "Help button selected", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Set 3 chart by age invisible when default or visible
     *
     * @param b
     */
    private void setVisibleChartAge(boolean b) {
        if (b) {
            pieAge1.setVisibility(View.GONE);
            pieAge2.setVisibility(View.GONE);
            pieAge3.setVisibility(View.GONE);
        } else {
            pieAge1.setVisibility(View.VISIBLE);
            pieAge2.setVisibility(View.VISIBLE);
            pieAge3.setVisibility(View.VISIBLE);
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

    /**
     * Action when click button
     */
    private void setListenButton() {
        btn_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    analystDataByAge();
                    setPieChartAgeMax();
                    setPieChartAgeMin();
                    setPieChartAgeNormal();
                    setVisibleChartAge(false);
                } else {
                    setVisibleChartAge(true);
                }
            }
        });
    }

    /**
     * Get data for pie chart age
     */
    private void analystDataByAge() {
        mapMax = new TreeMap<>();
        mapMin = new TreeMap<>();
        mapNormal = new TreeMap<>();
        for (Predict item : listMax) {
            addElement(mapMax, item.getAge());
        }
        for (Predict item : listMin) {
            addElement(mapMin, item.getAge());
        }
        for (Predict item : listNormal) {
            addElement(mapNormal, item.getAge());
        }
    }

    /**
     * Add element for map to count value the same
     *
     * @param map
     * @param age
     */
    private void addElement(Map<Integer, Integer> map, int age) {
        if (map.containsKey(age)) {
            int count = map.get(age) + 1;
            map.put(age, count);
        } else {
            map.put(age, 1);
        }
    }

    /**
     * init view for pie chart analyst by normal pressure
     */
    private void setPieChartAgeNormal() {
        pieAge3.setRotationEnabled(true);
        pieAge3.setHoleRadius(35f);
        pieAge3.setTransparentCircleAlpha(0);
        pieAge3.setCenterText(Constant.PREDICT_NORMAL_NAME);
        pieAge3.setCenterTextSize(8);
        pieAge3.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                PieEntry pe = (PieEntry) e;
                Toast.makeText(AnalysisActivity.this, Constant.HAVE + (int) pe.getValue() + Constant.PEOPLE + Constant.AGE_LEVEL + ((PieEntry) e).getLabel(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
        Description description = new Description();
        if (listNormal.size() > 0) {
            description.setText(Constant.ANALYST_NORMAL);
        } else {
            description.setText(Constant.MESAGE_NO_DATA);
        }
        pieAge3.setDescription(description);

        ArrayList<PieEntry> yEntries = new ArrayList<>();
        ArrayList<String> xEntries = new ArrayList<>();
        for (Integer key : mapNormal.keySet()) {
            PieEntry pie = new PieEntry(mapNormal.get(key), key);
            pie.setLabel(key + Constant.EMPTY);
            yEntries.add(pie);
            xEntries.add(key + Constant.EMPTY);
        }
        PieDataSet pieDataSet = new PieDataSet(yEntries, Constant.AGE_LEVEL);
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);
        ArrayList<Integer> colors = new ArrayList<>();
        for (int i = 0; i < listNormal.size(); i++) {
            Random random = new Random();
            int red = random.nextInt(255);
            int green = random.nextInt(255);
            int blue = random.nextInt(1);
            red = (red + 255) / 2;
            green = (green + 255) / 2;
            blue = (blue + 255) / 2;
            colors.add(Color.rgb(red, green, blue));
        }
        pieDataSet.setColors(colors);

        Legend legend = pieAge3.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        PieData pieData = new PieData(pieDataSet);
        pieAge3.setData(pieData);
        pieAge3.invalidate();

    }

    /**
     * init view for pie chart analyst by min pressure
     */
    private void setPieChartAgeMin() {
        pieAge2.setRotationEnabled(true);
        pieAge2.setHoleRadius(35f);
        pieAge2.setTransparentCircleAlpha(0);
        pieAge2.setCenterText(Constant.PREDICT_MIN_NAME);
        pieAge2.setCenterTextSize(8);
        pieAge2.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                PieEntry pe = (PieEntry) e;
                Toast.makeText(AnalysisActivity.this, Constant.HAVE + (int) pe.getValue() + Constant.PEOPLE + Constant.AGE_LEVEL + ((PieEntry) e).getLabel(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
        Description description = new Description();
        if (listMin.size() > 0) {
            description.setText(Constant.ANALYST_MIN);
        } else {
            description.setText(Constant.MESAGE_NO_DATA);
        }
        pieAge2.setDescription(description);

        ArrayList<PieEntry> yEntries = new ArrayList<>();
        ArrayList<String> xEntries = new ArrayList<>();
        for (Integer key : mapMin.keySet()) {
            PieEntry pie = new PieEntry(mapMin.get(key), key);
            pie.setLabel(key + Constant.EMPTY);
            yEntries.add(pie);
            xEntries.add(key + Constant.EMPTY);
        }
        PieDataSet pieDataSet = new PieDataSet(yEntries, Constant.AGE_LEVEL);
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        ArrayList<Integer> colors = new ArrayList<>();
        for (int i = 0; i < listMin.size(); i++) {
            Random random = new Random();
            int red = random.nextInt(255);
            int green = random.nextInt(255);
            int blue = random.nextInt(1);
            red = (red + 255) / 2;
            green = (green + 255) / 2;
            blue = (blue + 255) / 2;
            colors.add(Color.rgb(red, green, blue));
        }
        pieDataSet.setColors(colors);

        Legend legend = pieAge2.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);

        PieData pieData = new PieData(pieDataSet);
        pieAge2.setData(pieData);
        pieAge2.invalidate();
    }

    /**
     * init view for pie chart analyst by max pressure
     */
    private void setPieChartAgeMax() {
        pieAge1.setRotationEnabled(true);
        pieAge1.setHoleRadius(35f);
        pieAge1.setTransparentCircleAlpha(0);
        pieAge1.setCenterText(Constant.PREDICT_MAX_NAME);
        pieAge1.setCenterTextSize(8);
        pieAge1.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                PieEntry pe = (PieEntry) e;
                Toast.makeText(AnalysisActivity.this, Constant.HAVE + (int) pe.getValue() + Constant.PEOPLE + Constant.AGE_LEVEL + ((PieEntry) e).getLabel(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
        Description description = new Description();
        if (listMax.size() > 0) {
            description.setText(Constant.ANALYST_MAX);
        } else {
            description.setText(Constant.MESAGE_NO_DATA);
        }
        pieAge1.setDescription(description);

        ArrayList<PieEntry> yEntries = new ArrayList<>();
        ArrayList<String> xEntries = new ArrayList<>();
        for (Integer key : mapMax.keySet()) {
            PieEntry pie = new PieEntry(mapMax.get(key), key);
            pie.setLabel(key + Constant.EMPTY);
            yEntries.add(pie);
            xEntries.add(key + Constant.EMPTY);
        }
        PieDataSet pieDataSet = new PieDataSet(yEntries, Constant.AGE_LEVEL);
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        ArrayList<Integer> colors = new ArrayList<>();
        for (int i = 0; i < listMax.size(); i++) {
            Random random = new Random();
            int red = random.nextInt(255);
            int green = random.nextInt(100);
            int blue = random.nextInt(100);
            red = (red + 255) / 2;
            green = (green + 255) / 2;
            blue = (blue + 255) / 2;
            colors.add(Color.rgb(red, green, blue));
        }
        pieDataSet.setColors(colors);

        Legend legend = pieAge1.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);

        PieData pieData = new PieData(pieDataSet);
        pieAge1.setData(pieData);
        pieAge1.invalidate();
    }

    /**
     * init view in case of admin
     */
    private void initViewManager() {
        mChart.setRotationEnabled(true);
        mChart.setHoleRadius(35f);
        mChart.setTransparentCircleAlpha(0);
        mChart.setCenterText(Constant.PREDICT_NAME);
        mChart.setCenterTextSize(10);
        mChart.setOnChartValueSelectedListener(this);
        Description description = new Description();
        description.setText(Constant.ANALYST_USER);
        description.setTextSize(10);
        mChart.setDescription(description);
        mChart.setDrawEntryLabels(true);
        GetListTypePredict getListTypePredict = new GetListTypePredict();
        getListTypePredict.execute();
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
                    gotData = Constant.SERVER_SUCCESS;
                } catch (JSONException e) {
                    e.printStackTrace();
                    gotData = Constant.SERVER_ERROR;
                }
            } else {
                Log.e(Constant.LOG_JSON, Constant.MSG_JSON);
                gotData = Constant.SERVER_FAIL;
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
            if (Constant.SERVER_SUCCESS == gotData) {
                addDataSet(mChart);
//                initTableView();
            } else if (Constant.SERVER_FAIL == gotData) {
                TextView mess = findViewById(R.id.message_chart);
                mess.setText(Constant.MESAGE_NO_DATA);
            } else {
                TextView mess = findViewById(R.id.message_chart);
                mess.setText(Constant.MESSAGE_SERVER_FAILED);
            }
        }
    }

    /**
     * Add data set for pie chart predict blood pressue
     *
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
            yEntries.add(new PieEntry(yData[i], xData[i]));
        }
        xEntries.addAll(Arrays.asList(xData));
        PieDataSet pieDataSet = new PieDataSet(yEntries, Constant.EMPTY);
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
     * init view for heart chart in case of user rule
     */
    @SuppressLint("ResourceAsColor")
    private void initViewHeartChart() {
        CombinedChart heartChart = findViewById(R.id.combined_chart_heart);
        heartChart.getDescription().setEnabled(false);
        heartChart.setBackgroundColor(Color.WHITE);
        heartChart.setDrawGridBackground(true);
        heartChart.setDrawBarShadow(false);
        heartChart.setHighlightFullBarEnabled(false);
        heartChart.setOnChartValueSelectedListener(this);

        YAxis rightAxis = heartChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(40f);

        YAxis leftAxis = heartChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(40f);

        final List<String> xLabel = new ArrayList<>();
        for (int i = listPressure.size() - 1; i >= 0; i--) {
            xLabel.add(listPressure.get(i).getBloodPressureId() + Constant.EMPTY);
        }

        XAxis xAxis = heartChart.getXAxis();
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
        lineDatasMin.addDataSet((ILineDataSet) dataChartHeart());
        dataMin.setData(lineDatasMin);
        xAxis.setAxisMaximum(dataMin.getXMax() + 0.25f);
        heartChart.setData(dataMin);
        heartChart.invalidate();
    }

    /**
     * Set data for Pressure Max Chart
     *
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
     *
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

    /**
     * Set data for Pressure Heart beat Chart
     *
     * @return data set
     */
    private DataSet dataChartHeart() {
        LineData d = new LineData();
        ArrayList<Entry> entriesHeart = new ArrayList<>();
        for (int i = 0; i < listPressure.size(); i++) {
            entriesHeart.add(new Entry(i, listPressure.get(listPressure.size() - 1 - i).getHeartBeat()));
        }
        LineDataSet set = new LineDataSet(entriesHeart, Constant.LABEL_CHART_HEART);
        set.setColor(Color.BLUE);
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.BLUE);
        set.setCircleRadius(5f);
        set.setFillColor(Color.BLUE);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.BLUE);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return set;
    }


    /**
     * Set Data for table view
     */
    @SuppressLint("SetTextI18n")
    private void initTableView() {
        TableLayout tableLayout = findViewById(R.id.table_result);
        for (BloodPressureInfor press : listPressure) {
            TextView id = new TextView(getBaseContext());
            TextView hearBeat = new TextView(getBaseContext());
            TextView pressMax = new TextView(getBaseContext());
            TextView pressMin = new TextView(getBaseContext());
            id.setText(press.getBloodPressureId() + Constant.EMPTY);
            hearBeat.setText(press.getHeartBeat() + Constant.EMPTY);
            pressMax.setText(press.getPressureMax() + Constant.EMPTY);
            pressMin.setText(press.getPressureMin() + Constant.EMPTY);
            TableRow tableRow = new TableRow(getBaseContext());
            tableRow.addView(id);
            tableRow.addView(hearBeat);
            tableRow.addView(pressMax);
            tableRow.addView(pressMin);
            tableLayout.addView(tableRow);
            tableLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_BEGINNING);
        }
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
                        bloodPressureInfor = new BloodPressureInfor(obj.getInt(Constant.PRESSURE_ID),
                                obj.getInt(Constant.PRESSURE_MAX),
                                obj.getInt(Constant.PRESSURE_MIN),
                                obj.getInt(Constant.HEART_BEAT),
                                obj.getInt(Constant.STANDARD_MAX),
                                obj.getInt(Constant.STANDARD_MIN), new SimpleDateFormat(Constant.DATE_FORMAT).parse(obj.getString(Constant.TIME)));
                        listPressure.add(bloodPressureInfor);
                    }
                    gotData = Constant.SERVER_SUCCESS;
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                    gotData = Constant.SERVER_ERROR;
                }
            } else {
                Log.e(Constant.LOG_JSON, Constant.MSG_JSON);
                gotData = Constant.SERVER_FAIL;
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
            if (Constant.SERVER_SUCCESS == gotData) {
                initViewMaxChart();
                initViewMinChart();
                initViewHeartChart();
                initTableView();
            } else if (Constant.SERVER_FAIL == gotData) {
                TextView mess = findViewById(R.id.message_chart);
                mess.setText(Constant.MESAGE_NO_DATA);
            } else {
                TextView mess = findViewById(R.id.message_chart);
                mess.setText(Constant.MESSAGE_SERVER_FAILED);
            }
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (rule == Constant.ADMIN_RULE) {

            int value = (int) h.getX();
            String mess = "";
            if (value == Constant.VALUE_NORMAL_PREDICT) {
                mess += Constant.PREDICT_NORMAL_NAME;
                Toast.makeText(this, Constant.HAVE
                                + (int) e.getY() + Constant.CHAR + listPredict.size() + Constant.PEOPLE + mess
                        , Toast.LENGTH_SHORT).show();
            } else if (value == Constant.VALUE_MAX_PREDICT) {
                mess += Constant.PREDICT_MAX_NAME;
                Toast.makeText(this, Constant.HAVE
                                + (int) e.getY() + Constant.CHAR + listPredict.size() + Constant.PEOPLE + mess
                        , Toast.LENGTH_SHORT).show();
            } else {
                mess += Constant.PREDICT_MIN_NAME;
                Toast.makeText(this, Constant.HAVE
                                + (int) e.getY() + Constant.CHAR + listPredict.size() + Constant.PEOPLE + mess
                        , Toast.LENGTH_SHORT).show();
            }


        } else {
            Toast.makeText(this, Constant.NUMBER_GOT
                            + e.getY()
                            + Constant.MMHG
                    , Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onNothingSelected() {
    }

}
