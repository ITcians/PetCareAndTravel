package com.pet.app.views.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.pet.app.R;
import com.pet.app.controller.Reque;
import com.pet.app.models.NotificationModel;
import com.pet.app.resources.Apis;
import com.pet.app.resources.Dry;
import com.pet.app.resources.LocationPrefs;
import com.pet.app.resources.UserSession;
import com.pet.app.search.SearchActivity;
import com.pet.app.views.pets.AddPetFragment;
import com.pet.app.views.pets.MyPets;
import com.pet.app.views.profile.ProfileFragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onStart() {
        super.onStart();
        if (!Dry.getInstance().hasPermissions(this)) {
            Dry.getInstance().methodRequiresTwoPermission(this);
        }
    }

    final Handler handler = new Handler();
    TextView notificationCounter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navigationView = findViewById(R.id.homeBottomBar);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setSelectedItemId(0);
        navigate(HomeFragment.newInstance());

        LocationPrefs.getSession(this).destroy();
        new Thread(this::init).start();

    }

    void init() {
        notificationCounter = findViewById(R.id.homeNotifications);
        findViewById(R.id.toolNotificationButton)
                .setOnClickListener(view -> navigate(NotificationFragment.newInstance()));
        StringRequest request = new StringRequest(Request.Method.GET, Apis.getNotifications,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        handler.post(() -> notificationCounter.setText(String.valueOf(array.length())));
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                },
                error -> error.printStackTrace()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put(Apis.kAuth, "Bearer " + UserSession.getSession(HomeActivity.this).getToken());
                return map;
            }
        };

        Reque.getInstance(this)
                .addToRequestQueue(request);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bottomHome: {
                navigate(HomeFragment.newInstance());
                return true;
            }
            case R.id.bottomPets: {
                navigate(MyPets.newInstance());
                return true;
            }
            case R.id.bottomCreate: {
                navigate(AddPetFragment.newInstance(null));
                return true;
            }
            case R.id.bottomShop: {
                ActivityCompat.startActivity(this, new Intent(this, SearchActivity.class), null);
                return true;
            }
            case R.id.bottomProfile: {
                navigate(ProfileFragment.newInstance());
                return true;
            }
            default:
                return false;
        }

    }

    @Override
    protected void onDestroy() {
        Toast.makeText(this, "Press again!", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    public void navigate(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.homeFrame, fragment)
                .addToBackStack(String.valueOf(System.currentTimeMillis()))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    }
}