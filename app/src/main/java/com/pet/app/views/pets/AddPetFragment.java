package com.pet.app.views.pets;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.pet.app.R;
import com.pet.app.models.PetModel;
import com.pet.app.resources.Dry;
import com.pet.app.views.map.MapsActivity;

import java.io.FileNotFoundException;

import de.hdodenhof.circleimageview.CircleImageView;


public class AddPetFragment extends Fragment {
    PetModel petModel = new PetModel();

    public AddPetFragment(PetModel petModel) {
        // Required empty public constructor
        if (petModel == null)
            petModel = new PetModel();

        this.petModel = petModel;
    }

    public static AddPetFragment newInstance(PetModel petModel) {
        return new AddPetFragment(petModel);
    }

    private final Handler handler = new Handler();
    private CircleImageView petImage;
    TextInputLayout petName, petAge, petSpecie, petPrice, petHeight, petWeight;
    RadioGroup genderGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_pet, container, false);
        new Thread(() -> init(view)).start();
        return view;
    }

    void init(final View view) {
        petImage = view.findViewById(R.id.addPetImage);
        petName = view.findViewById(R.id.addPetName);
        petAge = view.findViewById(R.id.addPetAge);
        petSpecie = view.findViewById(R.id.addPetSpecie);
        petPrice = view.findViewById(R.id.addPetPrice);
        petWeight = view.findViewById(R.id.addPetWeight);
        petHeight = view.findViewById(R.id.addPetHeight);
        petImage.setOnClickListener(selectImage);
        genderGroup = view.findViewById(R.id.addPetGender);
        view.findViewById(R.id.addPetButton).setOnClickListener(savePet);
    }

    View.OnClickListener selectImage = view -> {
        //pick image
        if (!Dry.getInstance().hasPermissions(getContext())) {
            Dry.getInstance().methodRequiresTwoPermission(getActivity());
            return;
        }
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        @SuppressLint("IntentReset") Intent pickIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
        getActivity().startActivityFromFragment(this, chooserIntent, Dry.IMAGE_CODE);
    };

    View.OnClickListener savePet = view -> {
        //Save pet
        if (petModel.getPetImage() == null) {
            toast("Please select image!");
            return;
        }

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

        petModel.setPetPrice(petPrice.getEditText().getText().toString());
        if (petModel.getPetPrice() == null || petModel.getPetPrice().length() < 1 || petModel.getPetPrice().length() > 8) {
            petPrice.setError("Invalid price!");
            return;
        } else
            petPrice.setError(null);

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

        if (genderGroup.getCheckedRadioButtonId() == -1) {
            toast("Please select gender!");
            return;
        }

        petModel.setPetGender(Dry.getInstance().resolveGender(genderGroup.getCheckedRadioButtonId()));
        //upload
        //  new PetController(getActivity()).uploadPet(petModel);
        ActivityCompat.startActivity(getContext(),
                new Intent(getContext(), MapsActivity.class), null);

    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("SELECTED OMAGE " + resultCode + " " + requestCode);
        if (resultCode == -1 && requestCode == Dry.IMAGE_CODE && data != null) {
            Glide.with(getActivity()).load(data.getData()).fitCenter().into(petImage);
            new Thread(() -> {
                try {
                    petModel.setPetImage(Dry.getInstance()
                            .getBase64(data.getData().toString(), getActivity()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    void toast(String msg) {
        handler.post(() -> Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show());
    }
}