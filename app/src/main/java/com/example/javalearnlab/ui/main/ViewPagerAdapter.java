package com.example.javalearnlab.ui.main;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.javalearnlab.ui.profile.ProfileFragment;
import com.example.javalearnlab.ui.topics.TopicsFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new TopicsFragment();
            case 1: return new ProfileFragment();
            default: return new TopicsFragment();
        }
    }

    @Override
    public int getItemCount() { return 2; }
}
