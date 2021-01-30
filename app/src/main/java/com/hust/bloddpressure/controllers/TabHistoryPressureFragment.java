package com.hust.bloddpressure.controllers;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.hust.bloddpressure.R;
import com.hust.bloddpressure.model.MyService;
import com.hust.bloddpressure.model.entities.BloodPressureInfor;
import com.hust.bloddpressure.model.entities.InforStaticClass;
import com.hust.bloddpressure.util.Constant;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TabHistoryPressureFragment# newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabHistoryPressureFragment extends Fragment {
    ProgressDialog pDialog;
    ListView listViewPressure;
    ArrayList<BloodPressureInfor> listPressure;
    ListViewPressureAdapter listViewPressureAdapter;
    TextView txtMessage;
    private int rule;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_histor_pressure, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtMessage = getActivity().findViewById(R.id.message);
        ImageView img = getActivity().findViewById(R.id.img_no_pressure);
        img.setVisibility(View.GONE);
        Bundle bundle = getArguments();
        if (bundle != null) {
            // GET information from db
            String userId;
            rule = InforStaticClass.getRule();
            if (Constant.USER_RULE == rule) {
                userId = InforStaticClass.getUserId();
            } else {
                userId = bundle.getString(Constant.USER_ID);
            }
            listViewPressure = view.findViewById(R.id.list_result_pressure);
            listPressure = new ArrayList<>();
            GetListPressure getListPressure = new GetListPressure(userId);
            getListPressure.execute();

        } else {
            setMessageNoData();
        }
    }

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
                } catch (JSONException | ParseException e) {
                    txtMessage.setTextColor(ContextCompat.getColor(getContext(), R.color.no_data_color));
                    txtMessage.setText(Constant.MESSAGE_SERVER_FAILED);
                }
            } else {
                Log.e(Constant.LOG_JSON, Constant.MSG_JSON);
                txtMessage.setText(Constant.MESSAGE_SERVER_FAILED);
            }
            return null;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage(Constant.MESSAGE_LOADING);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Object o) {
            if (pDialog.isShowing())
                pDialog.dismiss();
            getData();
        }

        private void getData() {
            if (listPressure.size() != 0) {
                txtMessage.setText(R.string.history_detail);
                listViewPressureAdapter = new ListViewPressureAdapter(listPressure);
                listViewPressure.setAdapter(listViewPressureAdapter);
                listViewPressure.setSelection(listViewPressureAdapter.getCount() - 1);
                listViewPressureAdapter.notifyDataSetChanged();
                listViewPressure.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //Create interface dialog
                        AlertDialog.Builder confirm = new AlertDialog.Builder(adapterView.getContext());
                        // Set information for dialog
                        setUpDialog(confirm, i);
                        //Create dialog
                        AlertDialog dialogConfirm = confirm.create();
                        dialogConfirm.show();
                    }

                    private void setUpDialog(AlertDialog.Builder confirm, final int position) {
                        confirm.setTitle(Constant.DETAIL);
                        int pressMax = listPressure.get(position).getPressureMax();
                        int pressMin = listPressure.get(position).getPressureMin();
                        int standardMax = listPressure.get(position).getStandardMax();
                        int standardMin = listPressure.get(position).getStandardMin();
                        if ((pressMax >= standardMax) || (pressMin >= 90)) {
                            confirm.setMessage(Constant.MESSAGE_WARING_MAX);
                        } else if (pressMax <= 100 || pressMin < standardMin) {
                            confirm.setMessage(Constant.MESSAGE_WARING_MIN);
                        } else {
                            confirm.setMessage(Constant.MESSAGE_NORMAL);
                        }
                        confirm.setPositiveButton(Constant.YES, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                    }
                });
            } else {
                setMessageNoData();
            }
        }
    }

    private void setMessageNoData() {
        txtMessage.setTextColor(ContextCompat.getColor(getContext(), R.color.no_data_color));
        txtMessage.setText(R.string.no_data);
        ImageView img = getActivity().findViewById(R.id.img_no_pressure);
        img.setVisibility(View.VISIBLE);
        img.setImageResource(R.mipmap.blood_pressure_icon_vector);
    }
}