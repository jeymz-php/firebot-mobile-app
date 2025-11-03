package com.activity.firebotmobile;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;


public class FireExtinguisherMonitoringActivity extends AppCompatActivity {

    View liveIcon, fireIcon, batteryIcon, historyIcon, chatIcon, logsIcon;

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

        // bottom nav
        liveIcon = findViewById(R.id.nav_live);
        fireIcon = findViewById(R.id.nav_fire);
        batteryIcon = findViewById(R.id.nav_battery);
        historyIcon = findViewById(R.id.nav_history);
        chatIcon = findViewById(R.id.nav_chat);
        logsIcon = findViewById(R.id.nav_logs);

        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }

        //bottom sheet notif
        if (notificationButton != null) {
            notificationButton.setOnClickListener(v -> {
                NotificationsBottomSheetDialogFragment bottomSheet = new NotificationsBottomSheetDialogFragment();
                bottomSheet.show(getSupportFragmentManager(), NotificationsBottomSheetDialogFragment.TAG);
            });
        }

        //forda refill dialog
        if (refillGuideButton != null) {
            refillGuideButton.setOnClickListener(v -> {
                DialogFragment refillDialog = new RefillGuideDialogFragment();
                refillDialog.show(getSupportFragmentManager(), RefillGuideDialogFragment.TAG);
            });
        }

        //navigate
        liveIcon.setOnClickListener(v -> {
            startActivity(new Intent(this, LiveMonitoringActivity.class));
            finish();
        });

        fireIcon.setOnClickListener(v -> {
            // nasa fireextinguisher screen na, no need action
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