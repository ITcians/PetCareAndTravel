package com.pet.app.views.settings;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.pet.app.R;
import com.pet.app.controller.Reque;
import com.pet.app.resources.Apis;
import com.pet.app.resources.Dry;
import com.pet.app.resources.UserSession;
import com.pet.app.views.login.LoginActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingFragment extends Fragment implements AdapterView.OnItemClickListener {

    final List<String> menu = new ArrayList<>();

    public SettingFragment() {
        // Required empty public constructor
        menu.add("Update Name");
        menu.add("Update Password");
        menu.add("Logout");
    }

    ProgressDialog progressDialog;

    public static SettingFragment newInstance() {

        return new SettingFragment();
    }

    final Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Updating..");
        new Thread(() -> init(view)).start();
        return view;
    }

    void init(final View view) {
        ListView listView = view.findViewById(R.id.settingList);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, menu);
        listView.setOnItemClickListener(this);
        handler.post(() -> listView.setAdapter(adapter));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0: {
                //update name
                alertForUsername();
                break;
            }
            case 1: {
                //update pass
                alertForPass();
                break;
            }
            case 2: {
                //logout
                UserSession.getSession(getContext()).destroy();
                ActivityCompat.startActivity(getContext(), new Intent(getActivity(), LoginActivity.class), null);
                getActivity().finishAffinity();
                break;
            }
            default: {
                break;
            }
        }
    }

    void alertForUsername() {
        EditText textView = new EditText(getContext());
        textView.setHint("New username");
        new AlertDialog.Builder(getContext())
                .setTitle("Update username")
                .setView(textView)
                .setPositiveButton("Update", (dialog, which) -> {
                    String name = textView.getText().toString();
                    if (name.isEmpty()) {
                        Toast.makeText(getContext(), "Empty name can not be updated!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Map<String, String> map = new HashMap<>();
                    map.put("name", name);
                    updateProfile(map);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                }).show();
    }

    void alertForPass() {
        EditText textView = new EditText(getContext());
        textView.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        textView.setHint("New Password");
        new AlertDialog.Builder(getContext())
                .setTitle("Update password")
                .setView(textView)
                .setPositiveButton("Update", (dialog, which) -> {
                    String name = textView.getText().toString();
                    if (Dry.getInstance().validatePassword(name)) {
                        Toast.makeText(getContext(), "Password is not strong!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Map<String, String> map = new HashMap<>();
                    map.put("password", name);
                    updateProfile(map);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                }).show();
    }

    void updateProfile(Map<String, String> map) {
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, Apis.updateUser,
                response -> handler.post(() -> {
                    progressDialog.cancel();
                    Toast.makeText(getContext(), "Updated",
                            Toast.LENGTH_SHORT).show();
                    if (map.containsKey("name")) {
                        UserSession.getSession(getContext())
                                .setUsername(map.get("name"));

                    }

                }),

                error -> handler.post(() -> {
                    progressDialog.cancel();
                    Toast.makeText(getContext(), String.valueOf(error.getLocalizedMessage()),
                            Toast.LENGTH_SHORT).show();
                })) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> m = new HashMap<>();
                m.put(Apis.kAuth, "Bearer " + UserSession.getSession(getContext()).getToken());
                return m;
            }
        };

        Reque.getInstance(getContext()).addToRequestQueue(request);
    }
}
