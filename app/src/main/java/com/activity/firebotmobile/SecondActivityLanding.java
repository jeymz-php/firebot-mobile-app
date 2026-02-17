package com.activity.firebotmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivityLanding extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity_landing);

        // --- THE FIX: ADD THIS TO REMOVE THE TOP RED ACTION BAR ---
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // --- 1. Notification Button ---
        ImageButton notificationButton = findViewById(R.id.btnNotification);
        if (notificationButton != null) {
            notificationButton.setOnClickListener(v -> {
                NotificationsBottomSheetDialogFragment bottomSheet = new NotificationsBottomSheetDialogFragment();
                bottomSheet.show(getSupportFragmentManager(), NotificationsBottomSheetDialogFragment.TAG);
            });
        }

        // --- 2. Live Monitoring ---
        LinearLayout btnLiveMonitoring = findViewById(R.id.btnLiveMonitoring);
        if (btnLiveMonitoring != null) {
            btnLiveMonitoring.setOnClickListener(v -> {
                startActivity(new Intent(SecondActivityLanding.this, LiveMonitoringActivity.class));
            });
        }

        // --- 3. History ---
        LinearLayout btnHistory = findViewById(R.id.btnHistory);
        if (btnHistory != null) {
            btnHistory.setOnClickListener(v -> {
                startActivity(new Intent(SecondActivityLanding.this, HistoryActivity.class));
            });
        }

        // --- 4. Logs ---
        LinearLayout btnLogs = findViewById(R.id.btnLogs);
        if (btnLogs != null) {
            btnLogs.setOnClickListener(v -> {
                startActivity(new Intent(SecondActivityLanding.this, LogsActivity.class));
            });
        }

        // --- 5. Map ---
        LinearLayout btnMap = findViewById(R.id.btnMap);
        if (btnMap != null) {
            btnMap.setOnClickListener(v -> {
                startActivity(new Intent(SecondActivityLanding.this, MapActivity.class));
            });
        }

        // --- 6. Send Message ---
        LinearLayout btnSendMessage = findViewById(R.id.btnSendMessage);
        if (btnSendMessage != null) {
            btnSendMessage.setOnClickListener(v -> {
                startActivity(new Intent(this, MessageActivity.class));
            });
        }

        // --- 7. Profile ---
        LinearLayout btnProfile = findViewById(R.id.btnProfile);
        if (btnProfile != null) {
            btnProfile.setOnClickListener(v -> {
                startActivity(new Intent(this, ProfileActivity.class));
            });
        }
    }
}