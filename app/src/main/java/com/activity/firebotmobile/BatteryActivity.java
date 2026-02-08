package com.activity.firebotmobile;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BatteryActivity extends AppCompatActivity {

    // Added mapIcon to the view list
    View liveIcon, fireIcon, batteryIcon, historyIcon, chatIcon, logsIcon, mapIcon, profileIcon;

    // Header views
    ImageButton backButton, notificationButton;

    // battery status page
    ProgressBar circularProgressBar;
    TextView batteryPercentage, batteryStatusValue, remainingRuntimeValue, lastChargedValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery);

        // HIDE THE ACTION BAR
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Header Setup
        backButton = findViewById(R.id.backButton);
        notificationButton = findViewById(R.id.notificationButton);

        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }

        // Notification bottom sheet
        if (notificationButton != null) {
            notificationButton.setOnClickListener(v -> {
                NotificationsBottomSheetDialogFragment bottomSheet = new NotificationsBottomSheetDialogFragment();
                bottomSheet.show(getSupportFragmentManager(), NotificationsBottomSheetDialogFragment.TAG);
            });
        }

        // Battery content views
        circularProgressBar = findViewById(R.id.circular_progress_bar);
        batteryPercentage = findViewById(R.id.battery_percentage);
        batteryStatusValue = findViewById(R.id.battery_status_value);
        remainingRuntimeValue = findViewById(R.id.remaining_runtime_value);
        lastChargedValue = findViewById(R.id.last_charged_value);

        // Bottom Nav - Initialize all 8 buttons
        liveIcon = findViewById(R.id.nav_monitoring);
        fireIcon = findViewById(R.id.nav_fire);
        batteryIcon = findViewById(R.id.nav_battery);
        historyIcon = findViewById(R.id.nav_history);
        chatIcon = findViewById(R.id.nav_chat);
        logsIcon = findViewById(R.id.nav_logs);
        mapIcon = findViewById(R.id.nav_map); // Initialized Map
        profileIcon = findViewById(R.id.nav_profile);

        // Navigation Logic
        liveIcon.setOnClickListener(v -> {
            startActivity(new Intent(this, LiveMonitoringActivity.class));
            finish();
        });

        fireIcon.setOnClickListener(v -> {
            startActivity(new Intent(this, FireExtinguisherMonitoringActivity.class));
            finish();
        });

        batteryIcon.setOnClickListener(v -> {
            // Already here
        });

        historyIcon.setOnClickListener(v -> {
            startActivity(new Intent(this, HistoryActivity.class));
            finish();
        });

        chatIcon.setOnClickListener(v -> {
            startActivity(new Intent(this, ChatActivity.class));
            finish();
        });

        logsIcon.setOnClickListener(v -> {
            startActivity(new Intent(this, LogsActivity.class));
            finish();
        });

        // ADDED MAP NAVIGATION
        if (mapIcon != null) {
            mapIcon.setOnClickListener(v -> {
                startActivity(new Intent(this, MapActivity.class));
                finish();
            });
        }

        profileIcon.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
            finish();
        });
    }
}