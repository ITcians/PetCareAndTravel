package com.pet.app.controller;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class SwipeAdapter extends FragmentStateAdapter {
    private final List<Fragment> frags;

    public SwipeAdapter(@NonNull FragmentActivity fragmentActivity, List<Fragment> frags) {
        super(fragmentActivity);
        this.frags = frags;

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return frags.get(position);
    }

    @Override
    public int getItemCount() {
        return frags.size();
    }

}

