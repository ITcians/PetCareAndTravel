package com.pet.app.resources;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.util.Base64;

import androidx.core.app.ActivityCompat;

import com.pet.app.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pub.devrel.easypermissions.EasyPermissions;

public class Dry {

    private static volatile Dry dry;
    public static final int PERMISSION_CODE = 423;
    public static final int IMAGE_CODE = 411;

    private Dry() {
    }

    public static Dry getInstance() {
        if (dry == null)
            dry = new Dry();
        return dry;
    }

    public List<String> getAccountTypes() {
        List<String> accountTypes = new ArrayList<>();
        accountTypes.add("Buyer");
        accountTypes.add("Seller");
        accountTypes.add("Doctor");
        return accountTypes;
    }


    public boolean hasPermissions(Context context) {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION};
        return EasyPermissions.hasPermissions(context, perms);
    }

    public void methodRequiresTwoPermission(Activity context) {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION};
        if (!EasyPermissions.hasPermissions(context, perms)) {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(context, context.getString(R.string.permissions),
                    PERMISSION_CODE, perms);
        }
    }

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private static final Pattern VALID_STRONG_PASSWORD = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");

    public boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return !matcher.find();
    }

    public boolean validatePassword(String emailStr) {
        Matcher matcher = VALID_STRONG_PASSWORD.matcher(emailStr);
        return !matcher.find();
    }

    public String getBase64(String image, Activity activity) throws FileNotFoundException {
        if (image == null)
            return null;
        InputStream stream = activity.getContentResolver().openInputStream(Uri.parse(image));
        Bitmap bitmap = BitmapFactory.decodeStream(stream);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);
        byte[] arr = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(arr, Base64.DEFAULT);
    }

    public void alert(String title, String message, Context context) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("CLose", (dialogInterface, i) -> {
                    dialogInterface.cancel();
                })
                .show();
    }

    public void imagePicker(Activity context, Boolean isfrag) {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        @SuppressLint("IntentReset") Intent pickIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
        if (isfrag)
            context.startActivityForResult(chooserIntent, Dry.IMAGE_CODE);
        else
            ActivityCompat.startActivityForResult(context, chooserIntent, Dry.IMAGE_CODE, null);
    }

    public String resolveGender(int gender) {
        if (gender == 1)
            return "Male";
        else if (gender == 2)
            return "Female";
        else
            return "Other";
    }


    public static Address getAddressFromLocation(Location latLng, Activity activity) throws IOException {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(latLng.getLatitude(), latLng.getLongitude(), 1);
        if (addresses.size() > 0) {
            return addresses.get(0);
        }
        return null;
    }
}
