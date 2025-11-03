package com.activity.firebotmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast; // Kept from remote branch
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivityLanding extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity_landing);

        // --- 1. Notification Button Setup (Keeping local logic for bottom sheet) ---
        ImageButton notificationButton = findViewById(R.id.btnNotification);

        if (notificationButton != null) {
            notificationButton.setOnClickListener(v -> {
                // Assuming NotificationsBottomSheetDialogFragment is available in your local branch
                NotificationsBottomSheetDialogFragment bottomSheet = new NotificationsBottomSheetDialogFragment();
                bottomSheet.show(getSupportFragmentManager(), NotificationsBottomSheetDialogFragment.TAG);
            });
        }

        // --- 2. Live Monitoring Button Setup (From local HEAD) ---
        LinearLayout btnLiveMonitoring = findViewById(R.id.btnLiveMonitoring);

        if (btnLiveMonitoring != null) {
            btnLiveMonitoring.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SecondActivityLanding.this, LiveMonitoringActivity.class);
                    startActivity(intent);
                }
            });
        }

        // --- 3. Extinguisher Button Setup (From local HEAD) ---
        View extinguisherButton = findViewById(R.id.btnExtinguisher);

        if (extinguisherButton != null) {
            extinguisherButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SecondActivityLanding.this, FireExtinguisherMonitoringActivity.class);
                    startActivity(intent);
                }
            });
        }

        // --- 4. History Button Setup (From local HEAD) ---
        LinearLayout btnHistory = findViewById(R.id.btnHistory);

        if (btnHistory != null) {
            btnHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SecondActivityLanding.this, HistoryActivity.class);
                    startActivity(intent);
                }
            });
        }

        // --- 5. Battery Monitoring Button Setup (From local HEAD) 🔋 ---
        LinearLayout btnBattery = findViewById(R.id.btnBattery);

        if (btnBattery != null) {
            btnBattery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create an Intent to launch the BatteryActivity
                    Intent intent = new Intent(SecondActivityLanding.this, BatteryActivity.class);
                    startActivity(intent);
                }
            });
        }

        // --- 6. Logs Button Setup (From local HEAD) 📝 ---
        LinearLayout btnLogs = findViewById(R.id.btnLogs);

        if (btnLogs != null) {
            btnLogs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create an Intent to launch the LogsActivity
                    Intent intent = new Intent(SecondActivityLanding.this, LogsActivity.class);
                    startActivity(intent);
                }
            });
        }

        // --- 7. Send Message Button Setup (From remote) ---
        LinearLayout btnSendMessage = findViewById(R.id.btnSendMessage);
        btnSendMessage.setOnClickListener(v -> {
            Intent intent = new Intent(this, MessageActivity.class);
            startActivity(intent);
        });
    }
}