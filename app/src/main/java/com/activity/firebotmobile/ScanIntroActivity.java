package com.activity.firebotmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ScanIntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_intro);

        ImageView qrIcon = findViewById(R.id.qrIcon);
        TextView titleText = findViewById(R.id.titleText);
        TextView subText = findViewById(R.id.subText);
        Button scanButton = findViewById(R.id.scanButton);

        // 🔹 Bounce animation for the QR icon
        Animation bounceAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        qrIcon.startAnimation(bounceAnim);

        // 🔹 Fade + slide-up animations for text and button
        titleText.setAlpha(0f);
        subText.setAlpha(0f);
        scanButton.setAlpha(0f);

        titleText.setTranslationY(50f);
        subText.setTranslationY(50f);
        scanButton.setTranslationY(50f);

        titleText.animate()
                .alpha(1f)
                .translationY(0f)
                .setStartDelay(600)
                .setDuration(600)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();

        subText.animate()
                .alpha(1f)
                .translationY(0f)
                .setStartDelay(800)
                .setDuration(600)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();

        scanButton.animate()
                .alpha(1f)
                .translationY(0f)
                .setStartDelay(1000)
                .setDuration(700)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();

        // 🔹 Button click
        scanButton.setOnClickListener(v -> {
            Toast.makeText(this, "Opening scanner...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ScanIntroActivity.this, CameraPermissionActivity.class);
            startActivity(intent);
        });
    }
}
