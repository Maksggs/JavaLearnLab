package com.example.javalearnlab.ui.main;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.javalearnlab.R;
import com.example.javalearnlab.utils.AuthManager;
import com.example.javalearnlab.utils.NetworkReceiver;
import com.example.javalearnlab.utils.ProgressManager;
import com.example.javalearnlab.utils.ReminderManager;
import com.example.javalearnlab.utils.SettingsPrefs;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private BottomNavigationView bottomNav;
    private NetworkReceiver networkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        bottomNav = findViewById(R.id.bottomNavigation);

        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                bottomNav.getMenu().getItem(position).setChecked(true);
            }
        });

        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.menu_topics) viewPager.setCurrentItem(0);
            else if (item.getItemId() == R.id.menu_profile) viewPager.setCurrentItem(1);
            return true;
        });

        if (AuthManager.isLogged(this)) {
            ProgressManager.syncFromServer(this, () -> {});
            scheduleReminderIfNeeded();
        }

        // Регистрируем ресивер для отслеживания сети
        networkReceiver = new NetworkReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (networkReceiver != null) {
            unregisterReceiver(networkReceiver);
        }
    }

    private void scheduleReminderIfNeeded() {
        if (AuthManager.isLogged(this) && SettingsPrefs.isReminderEnabled(this)) {
            int hour = SettingsPrefs.getReminderHour(this);
            int minute = SettingsPrefs.getReminderMinute(this);
            ReminderManager.scheduleReminder(this, hour, minute);
        } else {
            ReminderManager.cancelReminder(this);
        }
    }
}