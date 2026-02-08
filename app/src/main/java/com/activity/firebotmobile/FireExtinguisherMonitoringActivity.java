package com.activity.firebotmobile;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Button;
import androidx.fragment.app.DialogFragment;

public class FireExtinguisherMonitoringActivity extends AppCompatActivity {

    // Added mapIcon to the view list
    View liveIcon, fireIcon, batteryIcon, historyIcon, chatIcon, logsIcon, mapIcon, profileIcon;

    // Header views
    ImageButton backButton, notificationButton;
    Button refillGuideButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_extinguisher_monitoring);

        backButton = findViewById(R.id.back_button);
        notificationButton = findViewById(R.id.notificationButton);
        refillGuideButton = findViewById(R.id.refill_guide_button);

        // bottom nav - Initialize all 8 buttons
        liveIcon = findViewById(R.id.nav_monitoring);
        fireIcon = findViewById(R.id.nav_fire);
        batteryIcon = findViewById(R.id.nav_battery);
        historyIcon = findViewById(R.id.nav_history);
        chatIcon = findViewById(R.id.nav_chat);
        logsIcon = findViewById(R.id.nav_logs);
        mapIcon = findViewById(R.id.nav_map); // Added Map initialization
        profileIcon = findViewById(R.id.nav_profile);

        // HIDE THE ACTION BAR
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // --- CLICK LISTENERS ---

        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }

        // bottom sheet notif
        if (notificationButton != null) {
            notificationButton.setOnClickListener(v -> {
                NotificationsBottomSheetDialogFragment bottomSheet = new NotificationsBottomSheetDialogFragment();
                bottomSheet.show(getSupportFragmentManager(), NotificationsBottomSheetDialogFragment.TAG);
            });
        }

        // forda refill dialog
        if (refillGuideButton != null) {
            refillGuideButton.setOnClickListener(v -> {
                DialogFragment refillDialog = new RefillGuideDialogFragment();
                refillDialog.show(getSupportFragmentManager(), RefillGuideDialogFragment.TAG);
            });
        }

        // --- NAVIGATION LOGIC ---

        liveIcon.setOnClickListener(v -> {
            startActivity(new Intent(this, LiveMonitoringActivity.class));
            finish();
        });

        fireIcon.setOnClickListener(v -> {
            // Already here
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