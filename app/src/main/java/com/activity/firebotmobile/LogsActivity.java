package com.activity.firebotmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class LogsActivity extends AppCompatActivity {

    public static final String TAG = "LogsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_logs);

        // HIDE THE ACTION BAR
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Header Buttons
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        findViewById(R.id.notificationButton).setOnClickListener(v -> {
            NotificationsBottomSheetDialogFragment bottomSheet = new NotificationsBottomSheetDialogFragment();
            bottomSheet.show(getSupportFragmentManager(), NotificationsBottomSheetDialogFragment.TAG);
        });

        // --- NAVIGATION LOGIC ---

        findViewById(R.id.nav_monitoring).setOnClickListener(v -> {
            startActivity(new Intent(LogsActivity.this, LiveMonitoringActivity.class));
            finish();
        });

        findViewById(R.id.nav_fire).setOnClickListener(v -> {
            startActivity(new Intent(LogsActivity.this, FireExtinguisherMonitoringActivity.class));
            finish();
        });

        findViewById(R.id.nav_battery).setOnClickListener(v -> {
            startActivity(new Intent(LogsActivity.this, BatteryActivity.class));
            finish();
        });

        findViewById(R.id.nav_history).setOnClickListener(v -> {
            startActivity(new Intent(LogsActivity.this, HistoryActivity.class));
            finish();
        });

        findViewById(R.id.nav_chat).setOnClickListener(v -> {
            startActivity(new Intent(LogsActivity.this, ChatActivity.class));
            finish();
        });

        // ADDED MAP NAVIGATION
        findViewById(R.id.nav_map).setOnClickListener(v -> {
            startActivity(new Intent(LogsActivity.this, MapActivity.class));
            finish();
        });

        findViewById(R.id.nav_profile).setOnClickListener(v -> {
            startActivity(new Intent(LogsActivity.this, ProfileActivity.class));
            finish();
        });

        // Note: nav_logs doesn't need a listener as we are already on this screen.
    }
}