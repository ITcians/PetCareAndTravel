package com.pet.app.views.home;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.pet.app.R;
import com.pet.app.controller.Reque;
import com.pet.app.models.NotificationModel;
import com.pet.app.resources.Apis;
import com.pet.app.resources.UserSession;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationFragment extends Fragment implements AdapterView.OnItemClickListener {


    public NotificationFragment() {
        // Required empty public constructor
    }

    public static NotificationFragment newInstance() {

        return new NotificationFragment();
    }


    ArrayAdapter<String> adapter;
    final List<NotificationModel> models = new ArrayList<>();
    final List<String> notifications = new ArrayList<>();
    View view;
    final Handler handler = new Handler();

    ProgressDialog progressDialog;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        this.view = view;
        new Thread(this::init).start();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        return view;
    }

    void init() {
        listView = view.findViewById(R.id.notificationListView);
        listView.setOnItemClickListener(this);
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, notifications);
        handler.post(() -> listView.setAdapter(adapter));
        loadData();
    }

    void loadData() {
        StringRequest request = new StringRequest(Request.Method.GET, Apis.getNotifications,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            NotificationModel model = new Gson().fromJson(array.getString(i), NotificationModel.class);
                            models.add(model);
                            notifications.add(model.getFromUser() + "\n" + model.getNotification() + "\n"
                                    + model.getTime());
                        }
                        handler.post(() -> {
                            adapter.notifyDataSetChanged();
                            if (notifications.size() < 1)
                                Toast.makeText(getContext(), "No new notifications", Toast.LENGTH_SHORT).show();
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                        handler.post(() -> {
                            Toast.makeText(getContext(), String.valueOf(e.getLocalizedMessage()), Toast.LENGTH_SHORT).show();
                        });
                    }
                    handler.post(() -> {
                        progressDialog.cancel();
                    });
                },
                error -> {
                    handler.post(() -> {
                        Toast.makeText(getContext(), String.valueOf(error.getLocalizedMessage()), Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                    });
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put(Apis.kAuth, "Bearer " + UserSession.getSession(getContext()).getToken());
                return map;
            }
        };

        Reque.getInstance(getContext())
                .addToRequestQueue(request);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        listView.setItemChecked(position, true);
        notifications.remove(position);
        adapter.notifyDataSetChanged();
        StringRequest request = new StringRequest(Request.Method.POST, Apis.readNotification,
                response -> {
                    System.out.println("JAAN " + response);
                },
                error -> {
                    error.printStackTrace();
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("notificationId", String.valueOf(models.get(position).getId()));
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put(Apis.kAuth, "Bearer " + UserSession.getSession(getContext()).getToken());
                return map;
            }
        };
        Reque.getInstance(getContext())
                .addToRequestQueue(request);
    }
}