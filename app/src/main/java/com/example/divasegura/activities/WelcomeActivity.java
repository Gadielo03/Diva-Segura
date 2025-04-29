package com.example.divasegura.activities;

import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;
import androidx.viewpager2.widget.ViewPager2;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.divasegura.adapters.RegistrationPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.example.divasegura.fragments.UserInfoFragment;
import com.example.divasegura.fragments.EmergencyContactFragment;
import com.example.divasegura.fragments.CongratulationsFragment;

import com.example.divasegura.controladores.CRUDHelper;
import com.example.divasegura.R;
import com.example.divasegura.utils.AppPreferences;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WelcomeActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //para Testeo descomentar esta linea de codigo para reiniciar el first run
        //AppPreferences.resetFirstRun(this);

        if (AppPreferences.isFirstRun(this)) {
            setContentView(R.layout.activity_welcome);

            EdgeToEdge.enable(this);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            // Set up ViewPager and TabLayout
            viewPager = findViewById(R.id.viewPager);
            tabLayout = findViewById(R.id.tabDots);

            // Set up the adapter
            RegistrationPagerAdapter adapter = new RegistrationPagerAdapter(this);
            viewPager.setAdapter(adapter);

            // Disable swiping (navigation controlled by buttons)
            viewPager.setUserInputEnabled(false);

            // Connect TabLayout with ViewPager
            TabLayoutMediator mediator = new TabLayoutMediator(tabLayout, viewPager,
                    (tab, position) -> {
                        // No text for tabs
                    }
            );
            mediator.attach();

        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    public void navigateToPage(int page) {
        viewPager.setCurrentItem(page);
    }

    private class RegistrationPagerAdapter extends FragmentStateAdapter {
        private static final int NUM_PAGES = 4;

        public RegistrationPagerAdapter(FragmentActivity activity) {
            super(activity);
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

}