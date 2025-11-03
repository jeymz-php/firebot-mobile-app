package com.activity.firebotmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class LiveMonitoringActivity extends AppCompatActivity {

    // Bottom navigation
    View liveIcon, fireIcon, batteryIcon, historyIcon, chatIcon, logsIcon;

    // Header views
    ImageButton backButton, notificationButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_monitoring);

        backButton = findViewById(R.id.back_button);
        notificationButton = findViewById(R.id.notification_button);

        // nav bottom
        liveIcon = findViewById(R.id.nav_live);
        fireIcon = findViewById(R.id.nav_fire);
        batteryIcon = findViewById(R.id.nav_battery);
        historyIcon = findViewById(R.id.nav_history);
        chatIcon = findViewById(R.id.nav_chat);
        logsIcon = findViewById(R.id.nav_logs);

        // ADD THIS ONE LINE TO HIDE THE ACTION BAR
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }

        if (notificationButton != null) {
            notificationButton.setOnClickListener(v -> {
                // notif bottom sheet
                NotificationsBottomSheetDialogFragment bottomSheet = new NotificationsBottomSheetDialogFragment();
                bottomSheet.show(getSupportFragmentManager(), NotificationsBottomSheetDialogFragment.TAG);
            });
        }


        // navigation
        liveIcon.setOnClickListener(v -> {
        });

        fireIcon.setOnClickListener(v -> {
            startActivity(new Intent(this, FireExtinguisherMonitoringActivity.class));
            finish();
        });

        batteryIcon.setOnClickListener(v -> {
            startActivity(new Intent(this, BatteryActivity.class));
            finish();
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
    }
}
