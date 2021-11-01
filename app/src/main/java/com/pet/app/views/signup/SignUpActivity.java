package com.pet.app.views.signup;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.pet.app.R;
import com.pet.app.controller.UserController;
import com.pet.app.models.UserModel;
import com.pet.app.resources.Dry;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    TextInputLayout nameLayout, emailLayout, passwordLayout;
    CircleImageView profileImage;
    UserModel user = new UserModel();
    ProgressDialog progressDialog;
    Handler handler = new Handler();
    Spinner spinner;
    UserController controller = new UserController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);


        new Thread(this::initViews).start();
    }

    void initViews() {
        nameLayout = findViewById(R.id.signUpNameLayout);
        emailLayout = findViewById(R.id.signUpEmailLayout);
        passwordLayout = findViewById(R.id.signUpPasswordgLayout);
        profileImage = findViewById(R.id.signUpLogo);
        profileImage.setOnClickListener(onProfileImageClick);
        Toolbar toolbar = findViewById(R.id.signUpToolbar);
        toolbar.setNavigationOnClickListener((view -> SignUpActivity.this.finish()));
        findViewById(R.id.signUpCreateAccountButton).setOnClickListener(onCreateButtonClick);
        spinner = findViewById(R.id.signUpAccountTypes);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,
                Dry.getInstance().getAccountTypes());
        handler.post(() -> spinner.setAdapter(adapter));


    }


    @SuppressLint("IntentReset")
    View.OnClickListener onProfileImageClick = (view) -> {
        if (!Dry.getInstance().hasPermissions(this)) {
            Dry.getInstance().methodRequiresTwoPermission(this);
            return;
        }
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        @SuppressLint("IntentReset") Intent pickIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
        ActivityCompat.startActivityForResult(this, chooserIntent, Dry.IMAGE_CODE, null);
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Dry.IMAGE_CODE && data != null) {
                if (data.getData() != null) {
                    Glide.with(SignUpActivity.this).load(data.getData()).fitCenter()
                            .into(profileImage);
                    user.setPhoto(data.getData().toString());
                }

            }
        }
    }

    View.OnClickListener onCreateButtonClick = (view) -> {

        user.setName(nameLayout.getEditText().getText().toString());
        if (user.getName() != null && user.getName().length() < 1) {
            nameLayout.setError("Can not proceed with empty name!");
            return;
        }
        nameLayout.setError(null);
        user.setEmail(emailLayout.getEditText().getText().toString());
        if (Dry.getInstance().validateEmail(user.getEmail())) {
            emailLayout.setError("Invalid email pattern! Ex. example@example.com");
            return;
        }
        emailLayout.setError(null);
        user.setPassword(passwordLayout.getEditText().getText().toString());
        if (Dry.getInstance().validatePassword(user.getPassword())) {
            passwordLayout.setError("Password is not strong! At least 1 digit ,lower & upper case letter, special character and length of 8");
            return;
        }
        passwordLayout.setError(null);

        user.setAccountType(adapter.getItem(spinner.getSelectedItemPosition()));
        if (user.getAccountType() == null) {
            Toast.makeText(this, "Please select account type!", Toast.LENGTH_SHORT).show();
            return;
        }
        //All clear
        progressDialog.setMessage("Creating account please wait..");
        progressDialog.show();
        new Thread(() -> controller.createAccount(user, progressDialog)).start();

    };
}