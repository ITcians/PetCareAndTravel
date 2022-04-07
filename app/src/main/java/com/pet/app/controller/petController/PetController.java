package com.pet.app.controller.petController;

import android.app.Activity;
import android.app.ProgressDialog;
import android.location.Address;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.pet.app.controller.Reque;
import com.pet.app.models.PetModel;
import com.pet.app.resources.Apis;
import com.pet.app.resources.Dry;
import com.pet.app.resources.UserSession;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
            try {
                Address address = Dry.getAddressFromLocation(petModel.getLat(), petModel.getLang(), activity);
                if (address != null)
                    petModel.setPetAddress(address.getAddressLine(0));
            } catch (IOException e) {
                e.printStackTrace();
            }


            //object.put("token",UserSession.getSession(activity).getToken());


            StringRequest request = new StringRequest(Request.Method.POST, Apis.AddPet,
                    response -> {
                        try {
                            JSONObject object = new JSONObject(response);

                            System.out.println("PET ADDED " + response.toString());
                            if (object.has("Error")) {
                                Dry.getInstance().alert("Failed", object.getString("Error"), activity);
                            } else {
                                Dry.getInstance().alert("Pet Added", "You can check in my pets section", activity);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }, error -> {
                progressDialog.dismiss();
                error.printStackTrace();
                Dry.getInstance().alert("Failed", error.getLocalizedMessage(), activity);
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return petModel.toMap();
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put(Apis.kAuth, "Bearer " + UserSession.getSession(activity).getToken());
                    return map;
                }
            };

            Reque.getInstance(activity).addToRequestQueue(request);
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Dry.getInstance().alert("Failed", e.getLocalizedMessage(), activity);
        }
    }

    public void updatePetPrice(PetModel petModel) {
        ProgressDialog progressDialog = new ProgressDialog(activity);
        try {
            progressDialog.setMessage("updating pet data..");
            progressDialog.setCancelable(false);
            progressDialog.show();

            StringRequest request = new StringRequest(Request.Method.POST, Apis.UpdatePet,
                    response -> {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.has("Error")) {
                                Dry.getInstance().alert("Failed", object.getString("Error"), activity);
                            } else {
                                Dry.getInstance().alert("Pet Updated", "You can check in my pets section", activity);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }, error -> {
                progressDialog.dismiss();
                error.printStackTrace();
                Dry.getInstance().alert("Failed", error.getLocalizedMessage(), activity);
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    if (petModel.getPetImage() != null)
                        return petModel.toMap();
                    else {
                        Map<String, String> map = petModel.toMap();
                        map.remove("petImage");
                        return map;
                    }

                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put(Apis.kAuth, "Bearer " + UserSession.getSession(activity).getToken());
                    return map;
                }
            };

            Reque.getInstance(activity).addToRequestQueue(request);
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Dry.getInstance().alert("Failed", e.getLocalizedMessage(), activity);
        }
    }

    public void updatePet(PetModel petModel) {
        ProgressDialog progressDialog = new ProgressDialog(activity);
        try {
            progressDialog.setMessage("updating pet data..");
            progressDialog.setCancelable(false);
            progressDialog.show();

            StringRequest request = new StringRequest(Request.Method.POST, Apis.UpdatePet,
                    response -> {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.has("Error")) {
                                Dry.getInstance().alert("Failed", object.getString("Error"), activity);
                            } else {
                                Dry.getInstance().alert("Pet Updated", "You can check in my pets section", activity);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }, error -> {
                progressDialog.dismiss();
                error.printStackTrace();
                Dry.getInstance().alert("Failed", error.getLocalizedMessage(), activity);
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    if (petModel.getPetImage() != null)
                        return petModel.toMap();
                    else {
                        Map<String, String> map = petModel.toMap();
                        map.remove("petImage");
                        return map;
                    }

                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put(Apis.kAuth, "Bearer " + UserSession.getSession(activity).getToken());
                    return map;
                }
            };

            Reque.getInstance(activity).addToRequestQueue(request);
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Dry.getInstance().alert("Failed", e.getLocalizedMessage(), activity);
        }
    }


//    public LatLng getSharedPreference(){
//        LocationPrefs locationPrefs = LocationPrefs.getSession(activity);
//        return new LatLng(locationPrefs.getLatitude(),locationPrefs.getLongitude());
//    }
}
