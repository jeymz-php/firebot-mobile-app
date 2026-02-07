package com.activity.firebotmobile;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LiveMonitoringActivity extends AppCompatActivity {

    View monitoringIcon, fireIcon, batteryIcon, historyIcon, chatIcon, logsIcon, profileIcon;
    ImageButton backButton, notificationButton;
    View liveDot, flameIntensityBar, gasIntensityBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_monitoring);

        // Standard IDs
        backButton = findViewById(R.id.back_button);
        notificationButton = findViewById(R.id.notification_button);
        monitoringIcon = findViewById(R.id.nav_monitoring);
        fireIcon = findViewById(R.id.nav_fire);
        batteryIcon = findViewById(R.id.nav_battery);
        historyIcon = findViewById(R.id.nav_history);
        chatIcon = findViewById(R.id.nav_chat);
        logsIcon = findViewById(R.id.nav_logs);
        profileIcon = findViewById(R.id.nav_profile);

        // Animation IDs
        liveDot = findViewById(R.id.live_indicator_dot);
        flameIntensityBar = findViewById(R.id.flame_intensity_bar);
        gasIntensityBar = findViewById(R.id.gas_intensity_bar);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        setupClickListeners();

        // THE FIX: Wait for the layout to finish measuring before animating
        getWindow().getDecorView().postDelayed(() -> {
            startLivePulse();
            animateBarForcefully(flameIntensityBar, 0.75f); // 75% fill
            animateBarForcefully(gasIntensityBar, 0.60f);   // 60% fill
        }, 500);
    }

    private void startLivePulse() {
        if (liveDot != null) {
            ObjectAnimator pulse = ObjectAnimator.ofFloat(liveDot, View.ALPHA, 1.0f, 0.2f);
            pulse.setDuration(1000);
            pulse.setRepeatMode(ValueAnimator.REVERSE);
            pulse.setRepeatCount(ValueAnimator.INFINITE);
            pulse.start();
        }
    }

    private void animateBarForcefully(final View bar, float percentage) {
        if (bar == null) return;

        View container = (View) bar.getParent();
        int maxWidth = container.getWidth();
        int targetWidth = (int) (maxWidth * percentage);

        ValueAnimator anim = ValueAnimator.ofInt(1, targetWidth);
        anim.setDuration(1500); // 1.5 seconds sliding effect
        anim.setInterpolator(new DecelerateInterpolator());

        anim.addUpdateListener(animation -> {
            int val = (int) animation.getAnimatedValue();
            android.view.ViewGroup.LayoutParams params = bar.getLayoutParams();
            params.width = val;
            bar.setLayoutParams(params);
            bar.requestLayout(); // FORCE system to update the "image"
        });
        anim.start();
    }

    private void setupClickListeners() {
        if (backButton != null) backButton.setOnClickListener(v -> finish());

        fireIcon.setOnClickListener(v -> { startActivity(new Intent(this, FireExtinguisherMonitoringActivity.class)); finish(); });
        batteryIcon.setOnClickListener(v -> { startActivity(new Intent(this, BatteryActivity.class)); finish(); });
        historyIcon.setOnClickListener(v -> { startActivity(new Intent(this, HistoryActivity.class)); finish(); });
        chatIcon.setOnClickListener(v -> { startActivity(new Intent(this, ChatActivity.class)); finish(); });
        logsIcon.setOnClickListener(v -> { startActivity(new Intent(this, LogsActivity.class)); finish(); });
        profileIcon.setOnClickListener(v -> { startActivity(new Intent(this, ProfileActivity.class)); finish(); });
    }
}