package com.pet.app.views.profile;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.pet.app.R;
import com.pet.app.controller.Reque;
import com.pet.app.controller.SwipeAdapter;
import com.pet.app.resources.Apis;
import com.pet.app.resources.Dry;
import com.pet.app.resources.UserSession;
import com.pet.app.views.orders.OrderFragment;
import com.pet.app.views.settings.SettingFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }


    final Handler handler = new Handler();
    ProgressDialog progressDialog;
    CircleImageView profileImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Updating...");

        new Thread(() -> init(view)).start();
        return view;
    }

    void init(final View view) {
        TabLayout tabs = view.findViewById(R.id.profileTabs);
        ViewPager2 viewPager2 = view.findViewById(R.id.profilePager);
        TextView nameText = view.findViewById(R.id.profileUsername);
        TextView accountType = view.findViewById(R.id.profileType);
        UserSession user = UserSession.getSession(getContext());
        profileImage = view.findViewById(R.id.profileImage);
        profileImage.setOnClickListener(onImageClick);
        handler.post(() -> {
            String text = user.getUsername() + "\n" + user.getEmail();
            nameText.setText(text);
            accountType.setText(user.getAccountType());
            Glide.with(getContext()).load( user.getPhoto())
                    .fitCenter().into(profileImage);
            List<Fragment> f = new ArrayList<>();
            f.add(OrderFragment.newInstance());
            f.add(SettingFragment.newInstance());
            viewPager2.setAdapter(new SwipeAdapter(getActivity(), f));
            new TabLayoutMediator(tabs, viewPager2, (tab, position) -> {
                if (position == 0) {
                    tab.setText(getString(R.string.orders));
                    tab.setIcon(getActivity().getDrawable(R.drawable.ic_baseline_shopping_cart_24));
                } else {
                    tab.setText(getString(R.string.settings));
                    tab.setIcon(getActivity().getDrawable(R.drawable.ic_baseline_settings_24));
                }

            }).attach();
        });
    }

    View.OnClickListener onImageClick = view -> {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("SELECTED OMAGE " + resultCode + " " + requestCode);
        if (resultCode == -1 && requestCode == Dry.IMAGE_CODE && data != null) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageURI(data.getData());
            new AlertDialog.Builder(getContext())
                    .setView(imageView)
                    .setNegativeButton("Cancel", (dialog, which) ->
                            dialog.cancel())
                    .setPositiveButton("Upload", (dialog, which) -> {
                                String encodedImage = null;
                                try {
                                    encodedImage = Dry.getInstance()

                                            .getBase64(String.valueOf(data.getData()), getActivity());
                                    Map<String, String> map = new HashMap<>();
                                    map.put("photo", encodedImage);
                                    updateProfile(map);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }

                            }
                    )
                    .show();
        }
    }

    void updateProfile(Map<String, String> map) {
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, Apis.updateUser,
                response -> handler.post(() -> {
                    progressDialog.cancel();
                    try {
                        JSONObject object = new JSONObject(response);
                        UserSession.getSession(getContext())
                                .setPhoto(Apis.ImageUrl + object.getString("Photo"));
                        Glide.with(getContext())
                                .load(UserSession.getSession(getContext()).getPhoto()).into(profileImage);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getContext(), "Updated",
                            Toast.LENGTH_SHORT).show();

                }),
                error -> handler.post(() -> {
                    progressDialog.cancel();
                    Toast.makeText(getContext(), String.valueOf(error.getLocalizedMessage()),
                            Toast.LENGTH_SHORT).show();
                })) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> m = new HashMap<>();
                m.put(Apis.kAuth, "Bearer " + UserSession.getSession(getContext()).getToken());
                return m;
            }
        };

        Reque.getInstance(getContext()).addToRequestQueue(request);
    }
}