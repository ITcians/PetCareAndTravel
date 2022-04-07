package com.pet.app.views.hegiene;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.pet.app.R;
import com.pet.app.controller.Reque;
import com.pet.app.resources.Apis;
import com.pet.app.resources.UserSession;
import com.pet.app.views.pets.models.Report;
import com.pet.app.views.pets.models.ReportConverter;
import com.pet.app.views.pets.models.ReportModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HegieneActivity extends AppCompatActivity {

    private final List<String> hygieneList = new ArrayList<>();
    private final List<String> records = new ArrayList<>();
    private final List<Report> reports = new ArrayList<>();
    private static final String TAG = HegieneActivity.class.getClass().getName();
    private final Handler handler = new Handler();
    ListView listView;
    ProgressDialog progressDialog;
    ArrayAdapter<String> hygieneAdapter;
    ArrayAdapter<String> recordAdapter;
    Spinner typeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hegiene);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        new Thread(this::init).start();

    }

    void init() {

        listView = findViewById(R.id.hygieneList);
        hygieneList.add("Bath");
        hygieneList.add("Cleaning");
        hygieneList.add("Ears");
        hygieneList.add("Hairs");
        hygieneList.add("Measurements");
        hygieneList.add("Medications");
        hygieneList.add("Nails");
        hygieneList.add("Teeth");
        hygieneList.add("Vaccination");
        hygieneAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, hygieneList);
        typeSpinner = findViewById(R.id.hygieneTypes);

        Toolbar toolbar = findViewById(R.id.hegieneToolbar);
        toolbar.setNavigationOnClickListener(view -> this.finish());
        findViewById(R.id.hygieneUploadBtn)
                .setOnClickListener(onHygieneAdd);
        recordAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, records);
        handler.post(() -> {
            typeSpinner.setAdapter(hygieneAdapter);
            listView.setAdapter(recordAdapter);
        });
        loadReports();

//        listView.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                int index = i;
//                Toast.makeText(getApplicationContext(), "selected value " + listView.getItemAtPosition(index), Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    void loadReports() {
        String petId = getIntent().getStringExtra("petId");
        Log.d(TAG, "loadReports: " + getIntent().getStringExtra("petId"));
        StringRequest request = new StringRequest(Request.Method.GET, Apis.GetHygieneReport + "?petId=" + petId,
                response -> {
                    try {
                        ReportModel reportModel = ReportConverter.fromJsonString(response);
                        for (int i = 0; i < reportModel.getReports().length; i++) {
                            Report report = reportModel.getReports()[i];
                            reports.add(report);
                            records.add(report.getHygieneType() + "\n" + report.getCreatedAt());
                        }

                        handler.post(recordAdapter::notifyDataSetChanged);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    progressDialog.cancel();

                },
                error -> {
                    handler.post(() -> {
                        progressDialog.cancel();
                        Toast.makeText(this, String.valueOf(error.getLocalizedMessage()), Toast.LENGTH_SHORT).show();
                    });
                });
        Reque.getInstance(this).addToRequestQueue(request);
    }

    View.OnClickListener onHygieneAdd = view -> {
        if (typeSpinner.getSelectedItemPosition() < 0) {
            Toast.makeText(this, "Please select hygiene type!", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.show();
        //Upload
        StringRequest request = new StringRequest(Request.Method.POST, Apis.AddHygiene,
                response -> {
                    handler.post(() -> {
                        progressDialog.cancel();
                        Toast.makeText(this, "Hygiene Updated!", Toast.LENGTH_SHORT).show();
                        records.add(hygieneList.get(typeSpinner.getSelectedItemPosition()) + "\nJust now");
                        recordAdapter.notifyDataSetChanged();
                    });
                },
                error -> {
                    handler.post(() -> {
                        progressDialog.cancel();
                        Toast.makeText(this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    });
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parm = new HashMap<>();
                parm.put("hygieneType", hygieneList.get(typeSpinner.getSelectedItemPosition()));
                parm.put("petId", getIntent().getStringExtra("petId"));

                return parm;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put(Apis.kAuth, "Bearer " + UserSession.getSession(HegieneActivity.this).getToken());
                return map;
            }
        };

        Reque.getInstance(this).addToRequestQueue(request);
    };
}