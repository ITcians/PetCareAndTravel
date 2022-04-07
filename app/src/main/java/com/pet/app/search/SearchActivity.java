package com.pet.app.search;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputLayout;
import com.pet.app.R;
import com.pet.app.controller.Reque;
import com.pet.app.controller.adapters.DoctorAdapter;
import com.pet.app.models.doctor.DoctorConverter;
import com.pet.app.models.doctor.DoctorModel;
import com.pet.app.resources.Apis;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    final Handler handler = new Handler();
    ProgressDialog progressDialog;
    DoctorAdapter adapter;
    TextInputLayout searchField;
    final List<DoctorModel> doctors=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");

        new Thread(this::init).start();
    }

    void init() {
        Toolbar toolbar = findViewById(R.id.searchToolbar);
        toolbar.setNavigationOnClickListener(view -> this.finish());
        searchField = findViewById(R.id.searchField);
        RecyclerView recyclerView = findViewById(R.id.searchRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new DoctorAdapter(this,doctors);
        handler.post(()->recyclerView.setAdapter(adapter));
        findViewById(R.id.searchButton)
                .setOnClickListener(onSearchClick);
    }

    View.OnClickListener onSearchClick = view -> {
        String term = searchField.getEditText().getText().toString();
        if (term.isEmpty()) {
            return;
        }
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.GET, Apis.searchDoctor,
                response -> {
                    try {
                        DoctorModel[] doctorModel= DoctorConverter.fromJsonString(response);
                        doctors.addAll(Arrays.asList(doctorModel));
                        handler.post(adapter::notifyDataSetChanged);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    handler.post(progressDialog::cancel);

                },
                error -> {
                    handler.post(() -> {
                        progressDialog.cancel();
                        Toast.makeText(this, String.valueOf(error.getLocalizedMessage()),
                                Toast.LENGTH_SHORT).show();
                    });
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("term", term);
                return map;
            }
        };
        Reque.getInstance(this).addToRequestQueue(request);
    };
}