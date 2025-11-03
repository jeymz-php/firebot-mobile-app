package com.activity.firebotmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class HistoryActivity extends AppCompatActivity {

    View liveIcon, fireIcon, batteryIcon, historyIcon, chatIcon, logsIcon;
    ImageView backButton, notificationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // ADD THIS ONE LINE TO HIDE THE ACTION BAR
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Header Buttons
        backButton = findViewById(R.id.backButton);
        notificationButton = findViewById(R.id.notificationButton);

        backButton.setOnClickListener(v -> finish());

        //notif bottom sheet
        notificationButton.setOnClickListener(v -> {
            NotificationsBottomSheetDialogFragment bottomSheet = new NotificationsBottomSheetDialogFragment();
            bottomSheet.show(getSupportFragmentManager(), NotificationsBottomSheetDialogFragment.TAG);
        });

        //nav bottom
        liveIcon = findViewById(R.id.nav_live);
        fireIcon = findViewById(R.id.nav_fire);
        batteryIcon = findViewById(R.id.nav_battery);
        historyIcon = findViewById(R.id.nav_history);
        chatIcon = findViewById(R.id.nav_chat);
        logsIcon = findViewById(R.id.nav_logs);

        // navigation
        liveIcon.setOnClickListener(v ->
                startActivity(new Intent(this, LiveMonitoringActivity.class))
        );

        fireIcon.setOnClickListener(v ->
                startActivity(new Intent(this, FireExtinguisherMonitoringActivity.class))
        );

        batteryIcon.setOnClickListener(v ->
                startActivity(new Intent(this, BatteryActivity.class))
        );

        historyIcon.setOnClickListener(v -> {
            // eto na yung screen, so no need action
        });

        chatIcon.setOnClickListener(v ->
                startActivity(new Intent(this, ChatActivity.class))
        );

        logsIcon.setOnClickListener(v ->
                startActivity(new Intent(this, LogsActivity.class))
        );

        // for the DialogFragment
        setupCardClickListeners();
    }

    private void setupCardClickListeners() {
        CardView cardAlarm = findViewById(R.id.card_alarm_triggered);
        CardView cardGas = findViewById(R.id.card_gas_detected);
        CardView cardFire = findViewById(R.id.card_fire_detected);
        CardView cardSms = findViewById(R.id.card_sms_alert);

        if (cardAlarm != null) {
            cardAlarm.setOnClickListener(v -> showHistoryDialog());
        }
        if (cardGas != null) {
            cardGas.setOnClickListener(v -> showHistoryDialog());
        }
        if (cardFire != null) {
            cardFire.setOnClickListener(v -> showHistoryDialog());
        }
        if (cardSms != null) {
            cardSms.setOnClickListener(v -> showHistoryDialog());
        }
    }

    private void showHistoryDialog() {
        HistoryDetailDialogFragment dialogFragment = new HistoryDetailDialogFragment();

        FragmentManager fm = getSupportFragmentManager();

        dialogFragment.show(fm, HistoryDetailDialogFragment.TAG);
    }
}