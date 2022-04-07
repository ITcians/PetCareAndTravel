package com.pet.app.views.home;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.pet.app.R;
import com.pet.app.controller.Reque;
import com.pet.app.controller.adapters.PetAdapter;
import com.pet.app.models.PetModel;
import com.pet.app.resources.Apis;
import com.pet.app.resources.UserSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    private final List<PetModel> pets = new ArrayList<>();
    private PetAdapter adapter;
    private final Handler handler = new Handler();
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        new Thread(() -> init(view)).start();
        return view;
    }

    void init(final View view) {
        RecyclerView recyclerView = view.findViewById(R.id.homeList);
        recyclerView.setHasFixedSize(true);
pets.clear();
        adapter = new PetAdapter(getContext(), pets);

        handler.post(() -> {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
        });
        loadData();
    }

    void loadData() {

        StringRequest request = new StringRequest(Request.Method.GET, Apis.GetPets,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        pets.clear();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            System.out.println("AZAD");
                            pets.add(new PetModel().fromJson(object));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressDialog.cancel();
                    handler.post(adapter::notifyDataSetChanged);
                    if (pets.isEmpty()) {
                        handler.post(() -> Toast.makeText(getContext(), "No pets found!"
                                , Toast.LENGTH_SHORT).show());
                    }
                },
                error -> {
                    progressDialog.cancel();
                    handler.post(() -> Toast.makeText(getContext(), String.valueOf(error.getLocalizedMessage()), Toast.LENGTH_SHORT).show());
                    error.printStackTrace();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> head = new HashMap<>();
                head.put(Apis.kAuth, "Bearer " + UserSession.getSession(getContext()).getToken());
                return head;
            }
        };
        Reque.getInstance(getContext()).addToRequestQueue(request);
    }
}