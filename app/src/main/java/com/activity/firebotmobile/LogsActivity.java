package com.activity.firebotmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class LogsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

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

        // Removed nav_fire and nav_battery listeners to prevent crash

        findViewById(R.id.nav_history).setOnClickListener(v -> {
            startActivity(new Intent(LogsActivity.this, HistoryActivity.class));
            finish();
        });

        findViewById(R.id.nav_chat).setOnClickListener(v -> {
            startActivity(new Intent(LogsActivity.this, ChatActivity.class));
            finish();
        });

        findViewById(R.id.nav_map).setOnClickListener(v -> {
            startActivity(new Intent(LogsActivity.this, MapActivity.class));
            finish();
        });

        findViewById(R.id.nav_profile).setOnClickListener(v -> {
            startActivity(new Intent(LogsActivity.this, ProfileActivity.class));
            finish();
        });
    }
}