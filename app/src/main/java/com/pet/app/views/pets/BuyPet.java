package com.pet.app.views.pets;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.api.Api;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.pet.app.R;
import com.pet.app.controller.Reque;
import com.pet.app.models.PetModel;
import com.pet.app.resources.Apis;
import com.pet.app.resources.UserSession;

import java.util.HashMap;
import java.util.Map;

public class BuyPet extends BottomSheetDialog {
    final PetModel petModel;
    final Location location;

    public BuyPet(@NonNull Context context, PetModel petModel, Location location) {
        super(context);
        this.petModel = petModel;
        this.location = location;
    }
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textView = new TextView(getContext());
        textView.setTextSize(20);
        textView.setText("Are you sure to buy this pet ?\nNote: Payment will be paid on delivery. We will deliver at your door step");
        textView.setPadding(16, 16, 16, 16);
        LinearLayout layout = new LinearLayout(getContext());
        layout.setPadding(16, 16, 16, 16);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(textView);

        Button exitButton = new Button(getContext());
        exitButton.setText("Exit");
        exitButton.setTextColor(ActivityCompat.getColor(getContext(), R.color.white));
        exitButton.setOnClickListener(onCancel);
        exitButton.setBackgroundColor(ActivityCompat.getColor(getContext(), R.color.red));
        layout.addView(exitButton);

        Button confirm = new Button(getContext());
        confirm.setText("Confirm");
        confirm.setOnClickListener(onConfirm);
        confirm.setBackgroundColor(ActivityCompat.getColor(getContext(), R.color.teal_700));
        confirm.setTextColor(ActivityCompat.getColor(getContext(), R.color.white));
        layout.addView(confirm);
        setContentView(layout);
         progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait....");

    }

    View.OnClickListener onCancel = view -> this.cancel();
    View.OnClickListener onConfirm = view -> {
       // progressDialog.show();
        new Thread(this::placeOrder).start();
        this.cancel();

        Toast.makeText(getContext(), "Your order has been placed!", Toast.LENGTH_SHORT).show();
    };

    void placeOrder() {
        StringRequest request = new StringRequest(Request.Method.POST, Apis.PlaceOrder,
                response -> {

                    System.out.println(response + " JAAN ");
                },
                error -> {
                    System.out.println(" JAAN " + error.getLocalizedMessage());
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("pickUpLat", String.valueOf(petModel.getLat()));
                map.put("pickUpLang", String.valueOf(petModel.getLang()));
                map.put("dropOfLat", String.valueOf(location.getLatitude()));
                map.put("dropOfLang", String.valueOf(location.getLongitude()));
                map.put("saleFess", String.valueOf(Double.parseDouble(petModel.getPetPrice()) * .20));
                map.put("petPrice", petModel.getPetPrice());
                map.put("paymentMethod", "Cash on delivery");
                map.put("paymentStatus", "Pending");
                map.put("petId", String.valueOf(petModel.getId()));
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put(Apis.kAuth, "Bearer " + UserSession.getSession(getContext()).getToken());
                return map;
            }
        };
        Reque.getInstance(getContext()).addToRequestQueue(request);
    }
}
