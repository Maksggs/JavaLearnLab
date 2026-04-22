package com.example.javalearnlab.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.javalearnlab.R;
import com.example.javalearnlab.auth.LoginActivity;
import com.example.javalearnlab.data.TheoryRepository;
import com.example.javalearnlab.ui.settings.SettingsActivity;
import com.example.javalearnlab.utils.AuthManager;
import com.example.javalearnlab.utils.ProgressManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ProfileFragment extends Fragment {

    private TextView nameTextView;
    private ProgressBar progressBar;
    private Button loginButton;
    private Button settingsButton;
    private LinearLayout calendarContainer;

    public ProfileFragment() {
        super(R.layout.fragment_main_profile);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameTextView = view.findViewById(R.id.name);
        progressBar = view.findViewById(R.id.progress);
        loginButton = view.findViewById(R.id.loginBtn);
        settingsButton = view.findViewById(R.id.settings);
        calendarContainer = view.findViewById(R.id.calendarContainer);

        settingsButton.setOnClickListener(v ->
                startActivity(new Intent(getContext(), SettingsActivity.class))
        );

        loginButton.setOnClickListener(v ->
                startActivity(new Intent(getContext(), LoginActivity.class))
        );

        updateUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        if (!AuthManager.isLogged(requireContext())) {
            loginButton.setVisibility(View.VISIBLE);
            nameTextView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            calendarContainer.setVisibility(View.GONE);
        } else {
            loginButton.setVisibility(View.GONE);
            nameTextView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            calendarContainer.setVisibility(View.VISIBLE);

            nameTextView.setText(AuthManager.getName(requireContext()));

            int completed = ProgressManager.getCompletedCount(
                    requireContext(),
                    TheoryRepository.loadTopics(requireContext())
            );
            int totalTopics = 9;
            int percent = totalTopics > 0 ? (completed * 100 / totalTopics) : 0;
            progressBar.setProgress(percent);

            buildCalendar(calendarContainer);
        }
    }

    private void buildCalendar(LinearLayout container) {
        container.removeAllViews();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat fullFormat = new SimpleDateFormat("yyyyMMdd", Locale.US); // важно Locale.US!

        for (int i = 13; i >= 0; i--) {
            Calendar day = (Calendar) cal.clone();
            day.add(Calendar.DAY_OF_YEAR, -i);
            String dayKey = fullFormat.format(day.getTime());

            boolean active = ProgressManager.wasActive(requireContext(), dayKey);
            TextView dayView = new TextView(requireContext());
            dayView.setText(dayFormat.format(day.getTime()));
            dayView.setBackgroundResource(active ? R.drawable.bg_day_active : R.drawable.bg_day_inactive);
            dayView.setTextColor(getResources().getColor(R.color.white));
            dayView.setTextSize(14);
            dayView.setPadding(8, 4, 8, 4);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(4, 0, 4, 0);
            dayView.setLayoutParams(params);
            container.addView(dayView);
        }
    }
}