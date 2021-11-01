package com.pet.app.controller;

import android.app.Activity;
import android.app.ProgressDialog;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pet.app.models.PetModel;
import com.pet.app.resources.Apis;
import com.pet.app.resources.Dry;
import com.pet.app.resources.UserSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PetController {

    private Activity activity;

    public PetController(Activity activity) {
        this.activity = activity;
    }

    public void uploadPet(PetModel petModel) {
        ProgressDialog progressDialog = new ProgressDialog(activity);
        try {

            progressDialog.setMessage("Uploading pet data..");
            progressDialog.setCancelable(false);
            progressDialog.show();

            JSONObject object = petModel.toJson();
            JSONArray array = new JSONArray();
            object.put("token", UserSession.getSession(activity).getToken());
            array.put(object);
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, Apis.AddPet, array,
                    response -> {
                        progressDialog.dismiss();
                        System.out.println("PET ADDED " + response.toString());
                    }, error -> {
                progressDialog.dismiss();
                error.printStackTrace();
                Dry.getInstance().alert("Failed", error.getLocalizedMessage(), activity);
            });

            Reque.getInstance(activity).addToRequestQueue(request);
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Dry.getInstance().alert("Failed", e.getLocalizedMessage(), activity);
        }
    }
}
