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

        // --- 1. Notification Button Setup (Existing Logic) ---
        ImageButton notificationButton = findViewById(R.id.btnNotification);

        if (notificationButton != null) {
            notificationButton.setOnClickListener(v -> {
                NotificationsBottomSheetDialogFragment bottomSheet = new NotificationsBottomSheetDialogFragment();
                bottomSheet.show(getSupportFragmentManager(), NotificationsBottomSheetDialogFragment.TAG);
            });
        }

        // --- 2. Live Monitoring Button Setup (Existing Logic) ---
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

        // --- 3. Extinguisher Button Setup (Existing Logic) ---
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

        // --- 4. History Button Setup (Existing Logic) ---
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

        // --- 5. Battery Monitoring Button Setup (Existing Logic) 🔋 ---
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

        // --- 6. Logs Button Setup (NEW LOGIC) 📝 ---
        // Get the reference to the Logs layout/button view by its ID
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
    }
}