package com.pet.app.views.pets;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.pet.app.R;
import com.pet.app.controller.petController.PetController;
import com.pet.app.models.PetModel;
import com.pet.app.resources.Dry;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditPetActivity extends AppCompatActivity {

    final Handler handler = new Handler();
    ProgressDialog progressDialog;
    PetModel petModel;
    private CircleImageView petImage;
    TextInputLayout petName, petAge, petSpecie, petHeight, petWeight, petContact;
    RadioGroup genderGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pet);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        new Thread(this::init).start();

    }

    void init() {
        try {
            JSONObject object = new JSONObject(getIntent().getStringExtra("pet"));
            petModel = new PetModel().fromJson(object);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toolbar toolbar = findViewById(R.id.updatePetToolbar);
        toolbar.setNavigationOnClickListener(view -> this.finish());

        petImage = findViewById(R.id.addPetImage);
        petName = findViewById(R.id.addPetName);
        petAge = findViewById(R.id.addPetAge);
        petContact = findViewById(R.id.addPetContact);
        petSpecie = findViewById(R.id.addPetSpecie);

        petWeight = findViewById(R.id.addPetWeight);
        petHeight = findViewById(R.id.addPetHeight);
        petImage.setOnClickListener(selectImage);
        genderGroup = findViewById(R.id.addPetGender);
        findViewById(R.id.updatePetButton).setOnClickListener(savePet);
        handler.post(() -> {
            Glide.with(this).load(petModel.getPetImage()).into(petImage);
            Objects.requireNonNull(petName.getEditText()).setText(petModel.getPetName());
            Objects.requireNonNull(petAge.getEditText()).setText(petModel.getPetAge());
            Objects.requireNonNull(petSpecie.getEditText()).setText(petModel.getPetSpecie());
            Objects.requireNonNull(petHeight.getEditText()).setText(petModel.getPetHeight());
            Objects.requireNonNull(petWeight.getEditText()).setText(petModel.getPetWeight());
            Objects.requireNonNull(petContact.getEditText()).setText(String.valueOf(petModel.getContact()));
            genderGroup.check(Dry.getInstance().resolveGender(petModel.getPetGender()));
            petModel.setPetImage(null);
        });

    }

    View.OnClickListener selectImage = view -> {
        //pick image
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

    View.OnClickListener savePet = view -> {

        petModel.setPetName(petName.getEditText().getText().toString());
        if (petModel.getPetName() == null || petModel.getPetName().length() < 1) {
            petName.setError("Invalid name!");
            return;
        } else
            petName.setError(null);

        petModel.setPetAge(petAge.getEditText().getText().toString());
        if (petModel.getPetAge() == null || petModel.getPetAge().length() < 1 || petModel.getPetAge().length() > 8) {
            petAge.setError("Invalid age!");
            return;
        } else
            petAge.setError(null);

        petModel.setPetSpecie(petSpecie.getEditText().getText().toString());
        if (petModel.getPetSpecie() == null || petModel.getPetSpecie().length() < 2) {
            petSpecie.setError("Invalid specie!");
            return;
        } else
            petSpecie.setError(null);

        petModel.setPetHeight(petHeight.getEditText().getText().toString());
        if (petModel.getPetHeight() == null || petModel.getPetHeight().length() < 1 || petModel.getPetHeight().length() > 8) {
            petHeight.setError("Invalid height!");
            return;
        } else
            petHeight.setError(null);

        petModel.setPetWeight(petWeight.getEditText().getText().toString());
        if (petModel.getPetWeight() == null || petModel.getPetWeight().length() < 1 || petModel.getPetWeight().length() > 8) {
            petWeight.setError("Invalid weight!");
            return;
        } else
            petWeight.setError(null);

        petModel.setContact(petContact.getEditText().getText().toString());
        if (petModel.getContact() == null || petModel.getContact().length() != 11 || petModel.getContact().charAt(0) != '0') {
            petContact.setError("Invalid number! Ex 03000000000");
            return;
        } else
            petContact.setError(null);

        if (genderGroup.getCheckedRadioButtonId() == -1) {
            toast("Please select gender!");
            return;
        }


        petModel.setPetGender(Dry.getInstance().resolveGender(genderGroup.getCheckedRadioButtonId()));

        new PetController(this).updatePet(petModel);

    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("SELECTED OMAGE " + resultCode + " " + requestCode);
        if (resultCode == -1 && requestCode == Dry.IMAGE_CODE && data != null) {
            Glide.with(this).load(data.getData()).fitCenter().into(petImage);
            new Thread(() -> {
                try {
                    petModel.setPetImage(Dry.getInstance()
                            .getBase64(data.getData().toString(), this));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    void toast(String msg) {
        handler.post(() -> Toast.makeText(this, msg, Toast.LENGTH_SHORT).show());
    }

}