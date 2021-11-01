package com.pet.app.views.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.pet.app.R;
import com.pet.app.controller.UserController;
import com.pet.app.resources.Dry;
import com.pet.app.views.signup.SignUpActivity;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout emailLayout, passLayout;
    ProgressDialog progressDialog;
    UserController controller = new UserController(this);
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait..");
        new Thread(this::init).start();
    }

    void init() {
        emailLayout = findViewById(R.id.loginEmailLayout);
        passLayout = findViewById(R.id.loginPasswordgLayout);
    }

    public void onPassResetClick(View view) {
    }

    public void onLoginClick(View view) {
        //validate
        if (Dry.getInstance().validateEmail(emailLayout.getEditText().getText().toString())) {
            emailLayout.setError("Invalid email format!");
            return;
        }

        if (passLayout.getEditText().getText().toString().isEmpty()) {
            passLayout.setError("Empty password!");
            return;
        }

        //send login request
        progressDialog.show();
        new Thread(() -> controller.login(emailLayout.getEditText().getText().toString(),
                passLayout.getEditText().getText().toString(), progressDialog)).start();
    }

    public void onCreateAccountClick(View view) {
        ActivityCompat.startActivity(LoginActivity.this, new Intent(
                LoginActivity.this, SignUpActivity.class), null);
    }
}