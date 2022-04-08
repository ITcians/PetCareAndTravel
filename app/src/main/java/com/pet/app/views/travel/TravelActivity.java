package com.pet.app.views.travel;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.pet.app.R;
import com.pet.app.models.PetModel;
import com.pet.app.resources.Dry;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;


public class TravelActivity extends FragmentActivity implements OnMapReadyCallback {

    public static PetModel petModel;

    private GoogleMap mMap;
    private FusedLocationProviderClient providerClient;
    private Location mLocation;
    Marker pickMarker, dropMarker;
    LatLng latLng;
    EditText fromField, toField;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
            providerClient = LocationServices.getFusedLocationProviderClient(this);
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");

        fromField = findViewById(R.id.travelFrom);
        toField = findViewById(R.id.travelTo);
        //toField.getEditText().setOnClickListener(onToClick);
        findViewById(R.id.travelDone)
                .setOnClickListener(onTravelDone);

        toField.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                onToDoneClick();
                return true;
            }
            return false;
        });

        toField.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                MarkerOptions options = new MarkerOptions();
                options.title("Drop Location");
                options.draggable(true);
                options.rotation(90);
                Toast.makeText(TravelActivity.this, "Press & hold marker to drag!", Toast.LENGTH_SHORT).show();
                options.position(latLng);
                dropMarker = mMap.addMarker(options);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDragStart(@NonNull Marker marker) {

                    }

                    @Override
                    public void onMarkerDrag(@NonNull Marker marker) {

                    }

                    @Override
                    public void onMarkerDragEnd(@NonNull Marker marker) {
                        try {
                            Address address = Dry.getAddressFromLocation(marker.getPosition(), TravelActivity.this);
                            if (address != null) {
                                toField.setText(String.valueOf(address.getAddressLine(0)));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                onToDoneClick();
            }
        });
        Toolbar toolbar = findViewById(R.id.travelToolbar);
        toolbar.setTitle("Travel");
        toolbar.setNavigationOnClickListener(v -> this.finish());
    }

    void onToDoneClick() {
        if (toField.getText().toString().isEmpty())
            return;
        progressDialog.show();
        try {
            Address address = Dry.getAddressFromName(toField.getText().toString(), this);
            if (address != null) {
                toField.setText(address.getAddressLine(0));
            }
            progressDialog.cancel();
        } catch (IOException e) {
            e.printStackTrace();
            progressDialog.cancel();
            Toast.makeText(getBaseContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    View.OnClickListener onTravelDone = v -> {
        if (pickMarker == null) {
            Toast.makeText(this, "Select pick location", Toast.LENGTH_SHORT).show();
            return;
        }
        if (dropMarker == null) {
            Toast.makeText(this, "Select drop location", Toast.LENGTH_SHORT).show();
            return;
        }
        //done here
        if (petModel == null) {
            Toast.makeText(this, "No pet selected try again!", Toast.LENGTH_SHORT).show();
            return;
        }

        String pickLocation = "http://maps.google.com/maps?saddr=" + pickMarker.getPosition().latitude +
                "," + pickMarker.getPosition().longitude + "&daddr=" +
                dropMarker.getPosition().latitude + "," + dropMarker.getPosition().longitude;
        String message = "Hey! I need your transport service for my pet "
                + petModel.getPetSpecie() + ", weight=" + petModel.getPetWeight()
                + ", height=" + petModel.getPetHeight()
                + " and these are directions.\n" +
                pickLocation +
                "\nLet me know the price and availability!";
        String url = "https://api.whatsapp.com/send?phone=YOUR_PHONE&text=" + message;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    };

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        getDeviceLocation();
    }

    private void getDeviceLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Task<Location> locationResult = providerClient.getLastLocation();
        locationResult.addOnSuccessListener(location -> {

            if (location == null) {
                getDeviceLocation();
            } else {
                mLocation = location;
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                this.latLng = latLng;
                //LatLng latLng1 = marker.getPosition();
                try {
                    Address address = Dry.getAddressFromLocation(latLng, TravelActivity.this);
                    if (address == null)
                        return;

                    Objects.requireNonNull(fromField).
                            setText(address.getAddressLine(0));
                    MarkerOptions options = new MarkerOptions();
                    options.title("Pick up");
                    options.draggable(true);
                    options.position(latLng);
                    pickMarker = mMap.addMarker(options);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } // else closed
        });
    }
}
