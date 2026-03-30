package com.example.javalearnlab.GeneratorTest;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.List;

public class FragmentClassAdapter extends FragmentStateAdapter {

    private List<Class<? extends Fragment>> fragmentClasses;

    public FragmentClassAdapter(@NonNull FragmentActivity fragmentActivity,
                                List<Class<? extends Fragment>> fragmentClasses) {
        super(fragmentActivity);
        this.fragmentClasses = fragmentClasses;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        try {
            Class<? extends Fragment> fragmentClass = fragmentClasses.get(position);
            Fragment fragment = fragmentClass.newInstance();

            // Передаем позицию фрагменту
            Bundle args = new Bundle();
            args.putInt("position", position);
            fragment.setArguments(args);

            return fragment;
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            return new Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return fragmentClasses.size();
    }
}