package com.activity.firebotmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class HistoryActivity extends AppCompatActivity {

    // Removed fireIcon and batteryIcon from variable list
    View liveIcon, historyIcon, chatIcon, logsIcon, mapIcon, profileIcon;
    ImageView backButton, notificationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        backButton = findViewById(R.id.backButton);
        notificationButton = findViewById(R.id.notificationButton);

        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }

        if (notificationButton != null) {
            notificationButton.setOnClickListener(v -> {
                NotificationsBottomSheetDialogFragment bottomSheet = new NotificationsBottomSheetDialogFragment();
                bottomSheet.show(getSupportFragmentManager(), NotificationsBottomSheetDialogFragment.TAG);
            });
        }

        // Initialize remaining 6 buttons only
        liveIcon = findViewById(R.id.nav_monitoring);
        historyIcon = findViewById(R.id.nav_history);
        chatIcon = findViewById(R.id.nav_chat);
        logsIcon = findViewById(R.id.nav_logs);
        mapIcon = findViewById(R.id.nav_map);
        profileIcon = findViewById(R.id.nav_profile);

        // --- NAVIGATION LOGIC ---
        liveIcon.setOnClickListener(v -> {
            startActivity(new Intent(this, LiveMonitoringActivity.class));
            finish();
        });

        // Removed fireIcon and batteryIcon listeners

        historyIcon.setOnClickListener(v -> {
            // Already on this screen
        });

        chatIcon.setOnClickListener(v -> {
            startActivity(new Intent(this, ChatActivity.class));
            finish();
        });

        logsIcon.setOnClickListener(v -> {
            startActivity(new Intent(this, LogsActivity.class));
            finish();
        });

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

        setupCardClickListeners();
    }

    private void setupCardClickListeners() {
        CardView cardAlarm = findViewById(R.id.card_alarm_triggered);
        CardView cardGas = findViewById(R.id.card_gas_detected);
        CardView cardFire = findViewById(R.id.card_fire_detected);
        CardView cardSms = findViewById(R.id.card_sms_alert);

        if (cardAlarm != null) cardAlarm.setOnClickListener(v -> showHistoryDialog());
        if (cardGas != null) cardGas.setOnClickListener(v -> showHistoryDialog());
        if (cardFire != null) cardFire.setOnClickListener(v -> showHistoryDialog());
        if (cardSms != null) cardSms.setOnClickListener(v -> showHistoryDialog());
    }

    private void showHistoryDialog() {
        HistoryDetailDialogFragment dialogFragment = new HistoryDetailDialogFragment();
        FragmentManager fm = getSupportFragmentManager();
        dialogFragment.show(fm, HistoryDetailDialogFragment.TAG);
    }
}