package com.pet.app.views.profile;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.pet.app.R;
import com.pet.app.controller.SwipeAdapter;
import com.pet.app.resources.Apis;
import com.pet.app.resources.UserSession;
import com.pet.app.views.orders.OrderFragment;
import com.pet.app.views.settings.SettingFragment;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }


    final Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        new Thread(() -> init(view)).start();
        return view;
    }

    void init(final View view) {
        TabLayout tabs = view.findViewById(R.id.profileTabs);
        ViewPager2 viewPager2 = view.findViewById(R.id.profilePager);
        TextView nameText = view.findViewById(R.id.profileUsername);
        TextView accountType = view.findViewById(R.id.profileType);
        UserSession user = UserSession.getSession(getContext());
        CircleImageView imageView = view.findViewById(R.id.profileImage);
        handler.post(() -> {
            String text = user.getUsername() + "\n" + user.getEmail();
            nameText.setText(text);
            accountType.setText(user.getAccountType());
            Glide.with(getContext()).load(Apis.ImageUrl + user.getPhoto())
                    .fitCenter().into(imageView);
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
}