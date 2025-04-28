package com.example.divasegura.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.divasegura.fragments.UserInfoFragment;
import com.example.divasegura.fragments.EmergencyContactFragment;
import com.example.divasegura.fragments.CongratulationsFragment;

public class RegistrationPagerAdapter extends FragmentStateAdapter {
    private static final int NUM_PAGES = 4;

    public RegistrationPagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new UserInfoFragment();
            case 1:
                return new EmergencyContactFragment(1);
            case 2:
                return new EmergencyContactFragment(2);
            case 3:
                return new CongratulationsFragment();
            default:
                return new UserInfoFragment();
        }
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}