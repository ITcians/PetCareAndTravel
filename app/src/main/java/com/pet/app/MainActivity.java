package com.pet.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.pet.app.controller.userController.UserController;
import com.pet.app.resources.Dry;
import com.pet.app.resources.UserSession;
import com.pet.app.views.home.HomeActivity;
import com.pet.app.views.login.LoginActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onStart() {
        super.onStart();
        if(!Dry.getInstance().hasPermissions(this)){
            Dry.getInstance().methodRequiresTwoPermission(this);
        }
    }
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

//        String token = UserSession.getSession(this).getToken();


    }
}