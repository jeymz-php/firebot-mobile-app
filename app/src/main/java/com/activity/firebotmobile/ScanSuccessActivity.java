package com.activity.firebotmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class ScanSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_success);

        // ADD THIS ONE LINE TO HIDE THE ACTION BAR
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        ImageButton btnClose = findViewById(R.id.btnClose);
        Button btnProceed = findViewById(R.id.btnProceed);
        ImageView imgSuccess = findViewById(R.id.imgSuccess);
        LinearLayout successCard = findViewById(R.id.successCard);

        Animation popBounce = AnimationUtils.loadAnimation(this, R.anim.pop_bounce);
        Animation slideUpFade = AnimationUtils.loadAnimation(this, R.anim.slide_up_fade_in);

        successCard.startAnimation(slideUpFade);
        imgSuccess.startAnimation(popBounce);

        btnClose.setOnClickListener(v -> finish());

        btnProceed.setOnClickListener(v -> {
            Intent intent = new Intent(ScanSuccessActivity.this, SecondActivityLanding.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out); // smooth transition
            finish();
        });
    }
}
