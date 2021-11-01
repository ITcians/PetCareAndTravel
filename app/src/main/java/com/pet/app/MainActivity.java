package com.pet.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.pet.app.controller.UserController;
import com.pet.app.views.home.HomeActivity;
import com.pet.app.views.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this,
                LoginActivity.class);
        if (new UserController(this).isLoggedIn())
            intent = new Intent(MainActivity.this,
                    HomeActivity.class);

        Intent finalIntent = intent;
        new Handler().postDelayed(() -> ActivityCompat.startActivity(MainActivity.this
                , finalIntent, null), 1100);
    }
}