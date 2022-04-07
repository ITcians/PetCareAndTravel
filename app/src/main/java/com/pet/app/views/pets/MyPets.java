package com.pet.app.views.pets;

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
import com.pet.app.controller.petController.PetCircleAdapter;
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

public class MyPets extends Fragment {


    public MyPets() {
        // Required empty public constructor
    }

    public static MyPets newInstance() {
        return new MyPets();
    }

    private final List<PetModel> pets = new ArrayList<>();
    private final Handler handler = new Handler();
    private PetCircleAdapter adapter;
    ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_pets, container, false);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        new Thread(() -> init(view)).start();
        new Thread(this::loadData).start();
        return view;
    }

    void loadData() {

        StringRequest request = new StringRequest(Request.Method.GET, Apis.MyPets,
                response -> {
                    try {

                        JSONArray array = new JSONArray(response);
                        pets.clear();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            System.out.println(object);
                            pets.add(new PetModel().fromJson(object));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressDialog.cancel();
                    handler.post(() -> {
                        if (pets.isEmpty())
                            Toast.makeText(getContext(), "No pet found!", Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                    });
                },
                error -> {
                    progressDialog.cancel();
                    handler.post(() -> Toast.makeText(getContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
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

    void init(final View view) {
        RecyclerView recyclerView = view.findViewById(R.id.myPetList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PetCircleAdapter(getActivity(), pets);
        handler.post(() -> recyclerView.setAdapter(adapter));

    }


}