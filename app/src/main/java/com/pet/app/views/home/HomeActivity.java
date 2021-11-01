package com.pet.app.views.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pet.app.R;
import com.pet.app.resources.UserSession;
import com.pet.app.search.SearchActivity;
import com.pet.app.views.pets.AddPetFragment;
import com.pet.app.views.pets.MyPets;
import com.pet.app.views.profile.ProfileFragment;

public class HomeActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navigationView = findViewById(R.id.homeBottomBar);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setSelectedItemId(R.id.bottomHome);
        UserSession.getSession(this).setToken("fsddf4w");
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