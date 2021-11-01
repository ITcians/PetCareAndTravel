package com.pet.app.views.pets;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pet.app.R;
import com.pet.app.controller.adapters.PetAdapter;
import com.pet.app.models.PetModel;
import com.pet.app.resources.UserSession;

import java.util.ArrayList;
import java.util.List;

public class MyPets extends Fragment {


    public MyPets() {
        // Required empty public constructor
    }

    public static MyPets newInstance() {
        return new MyPets();
    }

    private final List<PetModel> pets = new ArrayList<>();
    private final Handler handler = new Handler();
    private PetAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_pets, container, false);
        new Thread(() -> init(view)).start();
        return view;
    }

    void init(final View view) {
        RecyclerView recyclerView = view.findViewById(R.id.myPetList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PetAdapter(getContext(), pets);
        PetModel pet1 = new PetModel();
        pet1.setPetGender("Female");
        pet1.setPetWeight("1.3kg");
        pet1.setPetHeight("1.2ft");
        pet1.setPetPrice("PKR1200");
        pet1.setUserToken("sd");
        pet1.setPetSpecie("Poodle");
        pet1.setPetAge("1/2 year");
        pet1.setPetAddress("Phase 2 Jamshoro ,Sindh Pakistan");
        pet1.setPetName("Jassie");
        pet1.setLang((float) 26.234);
        pet1.setLat((float) 26.5245);
        pet1.setPetImage("https://image.freepik.com/free-photo/toy-poodle-grassy-field_1359-54.jpg");
        pets.add(pet1);

        PetModel pet2 = new PetModel();
        pet2.setPetGender("Male");
        pet2.setPetWeight("1.9kg");
        pet2.setPetHeight("1.4ft");
        pet2.setPetPrice("PKR3200");
        pet2.setPetSpecie("Bull Dog");
        pet2.setPetAge("1 year");
        pet2.setPetName("Razor");
        pet2.setPetAddress("Phase 1 Jamshoro ,Sindh Pakistan");
        pet2.setLang((float) 26.234);
        pet1.setUserToken("sd");
        pet2.setLat((float) 26.5245);
        pet2.setPetImage("https://image.freepik.com/free-photo/american-staffordshire-terrier-puppy-table_1150-18208.jpg");
        pets.add(pet2);
        handler.post(() -> recyclerView.setAdapter(adapter));
    }
}