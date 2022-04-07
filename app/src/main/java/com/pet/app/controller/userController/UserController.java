package com.pet.app.controller.userController;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pet.app.controller.Reque;
import com.pet.app.models.UserModel;
import com.pet.app.resources.Apis;
import com.pet.app.resources.Dry;
import com.pet.app.resources.UserSession;

import com.pet.app.views.home.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserController {
    private final Activity activity;
    private final Handler handler = new Handler();


    public UserController(Activity activity) {
        this.activity = activity;
    }

    public boolean isLoggedIn() {
        return UserSession.getSession(activity).getToken() != null;
    }

    public synchronized void createAccount(UserModel userModel,ProgressDialog progressDialog) {
        try {
            //convert image into base64

            String encoded = Dry.getInstance().getBase64(userModel.getPhoto(), activity);
            userModel.setPhoto(encoded);
            userModel.setFcm("null");
            // userModel.setPhoto(userModel.getPhoto());
            userModel.setToken(userModel.getToken());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Apis.signUp,
                    userModel.toJson(), response -> {
                if (response.has("Token")) {
                    try {
                        userModel.setToken(response.getString("Token"));
                        userModel.setPhoto(response.getString("Photo"));
                        //save user session
                        createSession(userModel);

                        toHome(progressDialog);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                handler.post(progressDialog::cancel);
            },
                    error -> {
                        error.printStackTrace();
                        handler.post(progressDialog::cancel);
                        handler.post(() -> Toast.makeText(activity, error.getLocalizedMessage(),
                                Toast.LENGTH_LONG).show());
                        System.out.println(error.getLocalizedMessage());
                    });
            Reque.getInstance(activity).addToRequestQueue(request);
        } catch (Exception ex) {
            ex.printStackTrace();
              handler.post(progressDialog::cancel);
            handler.post(() -> Toast.makeText(activity, ex.getLocalizedMessage(),
                    Toast.LENGTH_SHORT).show());

        }

    }

    public void createSession(UserModel userModel) {
        UserSession session =
                UserSession.getSession(activity);
        session.setEmail(userModel.getEmail());
        session.setPassword(userModel.getPassword());
        session.setUsername(userModel.getName());
        session.setPhoto(Apis.ImageUrl + userModel.getPhoto());
        session.setAccountType(userModel.getAccountType());
        session.setFcm(userModel.getFcm());
        session.setToken(userModel.getToken());
    }

    public void login(String email, String pass, ProgressDialog progressDialog) {

        try {
            JSONObject object = new JSONObject();
            object.put("email", email);
            object.put("pass", pass);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                    Apis.login,
                    object,
                    response -> {
                        //create session
                        System.out.println(response);
                        System.out.println("KALAM");
                        try {
                            if (response.has("Error")) {
                                //failed
                                handler.post(progressDialog::cancel);
                                Dry.getInstance().alert("Failed", response.getString("Error"), activity);
                                return;
                            }
                            UserModel userModel = new UserModel().fromJson(response);
                            createSession(userModel);
                            toHome(progressDialog);
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }, error -> {
                handler.post(progressDialog::cancel);
                Dry.getInstance().alert("Failed", error.getLocalizedMessage(), activity);
            });
            Reque.getInstance(activity).addToRequestQueue(request);


        } catch (Exception ex) {
            ex.printStackTrace();
            //    handler.post(progressDialog::cancel);
            handler.post(() -> Toast.makeText(activity, ex.getLocalizedMessage(),
                    Toast.LENGTH_SHORT).show());
        }
    }

    void toHome(ProgressDialog progressDialog) {
        handler.post(progressDialog::cancel);
        handler.post(() -> {
            Toast.makeText(activity, "Welcome to Pet CAT",
                    Toast.LENGTH_SHORT).show();
            ActivityCompat.startActivity(activity, new Intent(activity, HomeActivity.class), null);
            activity.finishAffinity();
        });
    }
}
