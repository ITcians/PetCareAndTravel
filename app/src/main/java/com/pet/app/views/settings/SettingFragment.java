package com.pet.app.views.settings;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.pet.app.R;
import com.pet.app.resources.UserSession;
import com.pet.app.views.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class SettingFragment extends Fragment implements AdapterView.OnItemClickListener {

    final List<String> menu = new ArrayList<>();

    public SettingFragment() {
        // Required empty public constructor
        menu.add("Update Name");
        menu.add("Update Password");
        menu.add("Logout");
    }

    public static SettingFragment newInstance() {

        return new SettingFragment();
    }

    final Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        new Thread(() -> init(view)).start();
        return view;
    }

    void init(final View view) {
        ListView listView = view.findViewById(R.id.settingList);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, menu);
        listView.setOnItemClickListener(this);
        handler.post(() -> listView.setAdapter(adapter));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0: {
                //update name
                break;
            }
            case 1: {
                //update pass
                break;
            }
            case 2: {
                //logout
                UserSession.getSession(getContext()).destroy();
                ActivityCompat.startActivity(getContext(), new Intent(getActivity(), LoginActivity.class), null);
                getActivity().finishAffinity();
                break;
            }
            default: {
                break;
            }
        }
    }
}
