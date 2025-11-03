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

        findViewById(R.id.backButton).setOnClickListener(v -> finish());


        findViewById(R.id.notificationButton).setOnClickListener(v -> {

            NotificationsBottomSheetDialogFragment bottomSheet = new NotificationsBottomSheetDialogFragment();
            // notif bottom sheet
            bottomSheet.show(getSupportFragmentManager(), NotificationsBottomSheetDialogFragment.TAG);
        });

        findViewById(R.id.nav_live).setOnClickListener(v -> {
            Intent intent = new Intent(LogsActivity.this, LiveMonitoringActivity.class);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.nav_fire).setOnClickListener(v -> {
            Intent intent = new Intent(LogsActivity.this, FireExtinguisherMonitoringActivity.class);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.nav_battery).setOnClickListener(v -> {
            Intent intent = new Intent(LogsActivity.this, BatteryActivity.class);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.nav_history).setOnClickListener(v -> {
            Intent intent = new Intent(LogsActivity.this, HistoryActivity.class);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.nav_chat).setOnClickListener(v -> {
            Intent intent = new Intent(LogsActivity.this, ChatActivity.class);
            startActivity(intent);
            finish();
        });


    }
}