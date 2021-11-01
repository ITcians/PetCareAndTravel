package com.pet.app.views.hegiene;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.pet.app.R;

import java.util.ArrayList;
import java.util.List;

public class HegieneActivity extends AppCompatActivity {

    private final List<String> hygieneList = new ArrayList<>();
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hegiene);
        new Thread(this::init).start();
    }

    void init() {
        ListView listView = findViewById(R.id.hygieneList);

        hygieneList.add("Bath");
        hygieneList.add("Cleaning");
        hygieneList.add("Ears");
        hygieneList.add("Hairs");
        hygieneList.add("Measurements");
        hygieneList.add("Medications");
        hygieneList.add("Nails");
        hygieneList.add("Teeth");
        hygieneList.add("Vaccination");
        Toolbar toolbar = findViewById(R.id.hegieneToolbar);
        toolbar.setNavigationOnClickListener(view -> this.finish());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_checked, hygieneList);
        handler.post(() -> listView.setAdapter(adapter));
    }
}