package com.activity.firebotmobile;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BatteryActivity extends AppCompatActivity {

    View liveIcon, fireIcon, batteryIcon, historyIcon, chatIcon, logsIcon;

    // Header views
    ImageButton backButton, notificationButton;

    // battery status page
    ProgressBar circularProgressBar;
    TextView batteryPercentage, batteryStatusValue, remainingRuntimeValue, lastChargedValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery);

        backButton = findViewById(R.id.backButton);

        notificationButton = findViewById(R.id.notificationButton);
        backButton.setOnClickListener(v -> finish());

        // notif bottom sheets
        if (notificationButton != null) {
            notificationButton.setOnClickListener(v -> {
                NotificationsBottomSheetDialogFragment bottomSheet = new NotificationsBottomSheetDialogFragment();
                bottomSheet.show(getSupportFragmentManager(), NotificationsBottomSheetDialogFragment.TAG);
            });
        }

        // battery content
        circularProgressBar = findViewById(R.id.circular_progress_bar);
        batteryPercentage = findViewById(R.id.battery_percentage);
        batteryStatusValue = findViewById(R.id.battery_status_value);
        remainingRuntimeValue = findViewById(R.id.remaining_runtime_value);
        lastChargedValue = findViewById(R.id.last_charged_value);

        // bottom nav
        liveIcon = findViewById(R.id.nav_live);
        fireIcon = findViewById(R.id.nav_fire);
        batteryIcon = findViewById(R.id.nav_battery);
        historyIcon = findViewById(R.id.nav_history);
        chatIcon = findViewById(R.id.nav_chat);
        logsIcon = findViewById(R.id.nav_logs);

        liveIcon.setOnClickListener(v ->
                startActivity(new Intent(this, LiveMonitoringActivity.class))
        );

        fireIcon.setOnClickListener(v ->
                startActivity(new Intent(this, FireExtinguisherMonitoringActivity.class))
        );

        batteryIcon.setOnClickListener(v -> {
            // walang action kasi nasa battery screen na
        });

        historyIcon.setOnClickListener(v ->
                startActivity(new Intent(this, HistoryActivity.class))
        );

        chatIcon.setOnClickListener(v ->
                startActivity(new Intent(this, ChatActivity.class))
        );

        logsIcon.setOnClickListener(v ->
                startActivity(new Intent(this, LogsActivity.class))
        );
    }
}