package com.pet.app.views.map;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.pet.app.R;
import com.pet.app.models.PetModel;
import com.pet.app.resources.Dry;
import com.pet.app.resources.LocationPrefs;
import com.pet.app.views.pets.AddPetFragment;
import com.pet.app.views.pets.BuyPet;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private FusedLocationProviderClient providerClient;
    private Marker marker;
    private Location mLocation;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        findViewById(R.id.btnSaveLocation).setOnClickListener(onSaveButtonClick);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        providerClient = LocationServices.getFusedLocationProviderClient(this);
        Toolbar toolbar=findViewById(R.id.mapToolbar);
        toolbar.setNavigationOnClickListener(view-> this.finish());
        if(getIntent().getStringExtra("buy")!=null){
            toolbar.setTitle("Delivery location");
        }


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        getDeviceLocation();


    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }




        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(@NonNull Marker marker) {

            }

            @Override
            public void onMarkerDrag(@NonNull Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(@NonNull Marker mapMarker) {
                marker = mapMarker;
                onLocationChanged(mapMarker.getPosition());

            }
        });

        Task<Location> locationResult = providerClient.getLastLocation();
        locationResult.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if(location==null)
                {
                    getDeviceLocation();
                }else {
                    mLocation = location;
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Pet Location Here...");
                    marker = mMap.addMarker(markerOptions);
                    marker.setDraggable(true);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    //LatLng latLng1 = marker.getPosition();
                    try {
                        Address address = Dry.getAddressFromLocation(latLng, MapsActivity.this);
                        TextInputLayout layout = findViewById(R.id.mapLocationField);
                        layout.getEditText().setText(address.getAddressLine(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } // else closed
            }
        });


        /*try {
            if (providerClient == null)
                return;

            Task<Location> locationResult = providerClient.getLastLocation();
            locationResult.addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    // Set the map's camera position to the current location of the device.

                    if()



                    if (task.getResult() != null) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(task.getResult().getLatitude(),
                                        task.getResult().getLongitude()), 15));
                        LatLng latLng = new LatLng(task.getResult().getLatitude(),task.getResult().getLongitude());

                        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Pet Location here....");
                        mMap.addMarker(markerOptions);



                        //get location address
                        try {
                            Address address = Dry.getAddressFromLocation(task.getResult(), this);
                            TextInputLayout layout = findViewById(R.id.mapLocationField);
                            layout.getEditText().setText(address.getAddressLine(0));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    System.out.println("NO CURRENT LOCATION");
                }
            });
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }*/
    }

    void onLocationChanged(LatLng latLng)
    {
        try {
            Address address = Dry.getAddressFromLocation(latLng, MapsActivity.this);
            TextInputLayout layout = findViewById(R.id.mapLocationField);
            layout.getEditText().setText(address.getAddressLine(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    void onSaveLocation()
    {
        TextInputLayout layout = findViewById(R.id.mapLocationField);
        if( layout.getEditText()==null || layout.getEditText().length()<1)
        {
            layout.getEditText().setError("Please Select Location");
            return;
        }else
        {
            if(getIntent().getStringExtra("buy")!=null){
                try {
                    PetModel petModel=new PetModel().fromJson(new JSONObject(getIntent().getStringExtra("pet")));
                    BuyPet buyPet=new BuyPet(this,petModel,mLocation);
                    buyPet.show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return;
            }
            LocationPrefs locationPrefs = LocationPrefs.getSession(getApplicationContext());
            locationPrefs.setLatitude(mLocation.getLatitude());
            locationPrefs.setLongitude(mLocation.getLongitude());
            this.finish();
//            Intent intent = new Intent(context, AddPetFragment.class);
//            startActivity(intent);
        }





    }

    View.OnClickListener onSaveButtonClick = (View) ->
    {
        onSaveLocation();
    };
}