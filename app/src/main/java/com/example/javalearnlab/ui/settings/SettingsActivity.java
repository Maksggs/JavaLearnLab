package com.example.javalearnlab.ui.settings;

import android.Manifest;
import android.app.AlarmManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import com.example.javalearnlab.R;
import com.example.javalearnlab.ui.main.MainActivity;
import com.example.javalearnlab.utils.AuthManager;
import com.example.javalearnlab.utils.ProgressManager;
import com.example.javalearnlab.utils.ReminderManager;
import com.example.javalearnlab.utils.ServerManager;
import com.example.javalearnlab.utils.SettingsPrefs;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    private EditText ipEditText;
    private SwitchCompat switchReminder;
    private View timePickerContainer;
    private TextView timeDisplay;
    private Button changeTimeBtn;
    private TextView permissionWarning;
    private Button grantPermissionBtn;
    private Button logoutButton;
    private Button resetButton;

    private int reminderHour;
    private int reminderMinute;

    private final ActivityResultLauncher<String> requestNotificationPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    enableReminderSwitch();
                    checkExactAlarmAndEnableReminder();
                } else {
                    Toast.makeText(this, "Без разрешения уведомления не будут работать", Toast.LENGTH_SHORT).show();
                    disableReminderSwitch();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Настройки");
        }

        ipEditText = findViewById(R.id.ip);
        Button saveButton = findViewById(R.id.saveIp);
        logoutButton = findViewById(R.id.logout);
        resetButton = findViewById(R.id.reset);
        switchReminder = findViewById(R.id.switchReminder);
        timePickerContainer = findViewById(R.id.timePickerContainer);
        timeDisplay = findViewById(R.id.timeDisplay);
        changeTimeBtn = findViewById(R.id.changeTimeBtn);
        permissionWarning = findViewById(R.id.permissionWarning);
        grantPermissionBtn = findViewById(R.id.grantPermissionBtn);

        String currentRawIp = ServerManager.getRawIp(this);
        ipEditText.setText(currentRawIp);

        reminderHour = SettingsPrefs.getReminderHour(this);
        reminderMinute = SettingsPrefs.getReminderMinute(this);
        updateTimeDisplay();

        if (!AuthManager.isLogged(this)) {
            // Скрываем всё, что связано с авторизованным пользователем
            switchReminder.setVisibility(View.GONE);
            timePickerContainer.setVisibility(View.GONE);
            permissionWarning.setVisibility(View.GONE);
            grantPermissionBtn.setVisibility(View.GONE);
            logoutButton.setVisibility(View.GONE);
        } else {
            boolean hasNotificationPermission = hasNotificationPermission();
            if (hasNotificationPermission) {
                enableReminderSwitch();
                boolean reminderEnabled = SettingsPrefs.isReminderEnabled(this);
                switchReminder.setChecked(reminderEnabled);
                timePickerContainer.setVisibility(reminderEnabled ? View.VISIBLE : View.GONE);
            } else {
                disableReminderSwitch();
                SettingsPrefs.setReminderEnabled(this, false);
            }
        }

        saveButton.setOnClickListener(v -> {
            String newIp = ipEditText.getText().toString().trim();
            if (newIp.isEmpty()) {
                Toast.makeText(this, "Введите IP-адрес", Toast.LENGTH_SHORT).show();
                return;
            }
            ServerManager.saveIP(this, newIp);
            String fullUrl = ServerManager.getBaseUrl(this);
            Toast.makeText(this, "Сохранено: " + fullUrl, Toast.LENGTH_LONG).show();
        });

        switchReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!hasNotificationPermission()) {
                switchReminder.setChecked(false);
                requestNotificationPermission();
                return;
            }
            SettingsPrefs.setReminderEnabled(this, isChecked);
            timePickerContainer.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            if (isChecked && AuthManager.isLogged(this)) {
                if (!hasExactAlarmPermission()) {
                    requestExactAlarmPermission();
                }
                scheduleReminder();
            } else {
                ReminderManager.cancelReminder(this);
            }
        });

        changeTimeBtn.setOnClickListener(v -> {
            TimePickerDialog dialog = new TimePickerDialog(this,
                    (view, hourOfDay, minute) -> {
                        reminderHour = hourOfDay;
                        reminderMinute = minute;
                        SettingsPrefs.setReminderTime(this, hourOfDay, minute);
                        updateTimeDisplay();
                        if (switchReminder.isChecked() && AuthManager.isLogged(this) && hasNotificationPermission()) {
                            if (!hasExactAlarmPermission()) {
                                requestExactAlarmPermission();
                            }
                            scheduleReminder();
                        }
                    },
                    reminderHour, reminderMinute, true);
            dialog.show();
        });

        grantPermissionBtn.setOnClickListener(v -> requestNotificationPermission());

        logoutButton.setOnClickListener(v -> {
            AuthManager.logout(this);   // Внутри logout уже вызывается ProgressManager.clearLocalProgress
            ReminderManager.cancelReminder(this);
            Toast.makeText(this, "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        resetButton.setOnClickListener(v -> {
            ProgressManager.resetAll(this);
            Toast.makeText(this, "Прогресс сброшен", Toast.LENGTH_SHORT).show();
        });
    }

    private boolean hasNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    private boolean hasExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            return alarmManager != null && alarmManager.canScheduleExactAlarms();
        }
        return true;
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
        } else {
            enableReminderSwitch();
        }
    }

    private void requestExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            try {
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            } catch (Exception e) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        }
    }

    private void checkExactAlarmAndEnableReminder() {
        if (switchReminder.isChecked() && AuthManager.isLogged(this) && hasNotificationPermission()) {
            if (!hasExactAlarmPermission()) {
                requestExactAlarmPermission();
            }
            scheduleReminder();
        }
    }

    private void scheduleReminder() {
        if (switchReminder.isChecked() && AuthManager.isLogged(this) && hasNotificationPermission()) {
            ReminderManager.scheduleReminder(this, reminderHour, reminderMinute);
        }
    }

    private void disableReminderSwitch() {
        switchReminder.setEnabled(false);
        switchReminder.setChecked(false);
        permissionWarning.setVisibility(View.VISIBLE);
        grantPermissionBtn.setVisibility(View.VISIBLE);
        timePickerContainer.setVisibility(View.GONE);
    }

    private void enableReminderSwitch() {
        switchReminder.setEnabled(true);
        permissionWarning.setVisibility(View.GONE);
        grantPermissionBtn.setVisibility(View.GONE);
    }

    private void updateTimeDisplay() {
        timeDisplay.setText(String.format(Locale.getDefault(), "%02d:%02d", reminderHour, reminderMinute));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}