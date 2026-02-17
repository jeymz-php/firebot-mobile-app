package com.activity.firebotmobile;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class LiveMonitoringActivity extends AppCompatActivity {
    View liveDot, barFront, barLeft, barRight;
    Handler handler = new Handler();
    RequestQueue requestQueue;
    String apiURL = "https://firebot.ucc-bsit.org/api/get_flame_status.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_monitoring);

        requestQueue = Volley.newRequestQueue(this);
        barFront = findViewById(R.id.flame_front_bar);
        barLeft = findViewById(R.id.flame_left_bar);
        barRight = findViewById(R.id.flame_right_bar);
        liveDot = findViewById(R.id.live_indicator_dot);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        setupNavigation();
        startLivePulse();
        startPolling();
    }

    private void setupNavigation() {
        findViewById(R.id.nav_map).setOnClickListener(v -> {
            startActivity(new Intent(this, MapActivity.class));
            finish();
        });
        // Add other navigation listeners (History, Profile, etc.) here
    }

    private void startLivePulse() {
        ObjectAnimator pulse = ObjectAnimator.ofFloat(liveDot, View.ALPHA, 1.0f, 0.2f);
        pulse.setDuration(1000);
        pulse.setRepeatMode(ValueAnimator.REVERSE);
        pulse.setRepeatCount(ValueAnimator.INFINITE);
        pulse.start();
    }

    private void startPolling() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchFlameData();
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    private void fetchFlameData() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiURL, null,
                response -> {
                    try {
                        // 1 = Fire detected (Full bar), 0 = No fire (Empty bar)
                        float frontVal = response.getInt("front") == 1 ? 1.0f : 0.01f;
                        float leftVal = response.getInt("left_side") == 1 ? 1.0f : 0.01f;
                        float rightVal = response.getInt("right_side") == 1 ? 1.0f : 0.01f;

                        animateBar(barFront, frontVal);
                        animateBar(barLeft, leftVal);
                        animateBar(barRight, rightVal);
                    } catch (Exception e) { e.printStackTrace(); }
                }, error -> {});
        requestQueue.add(request);
    }

    private void animateBar(final View bar, float percentage) {
        View container = (View) bar.getParent();
        int targetWidth = (int) (container.getWidth() * percentage);
        if (targetWidth == bar.getWidth()) return;

        ValueAnimator anim = ValueAnimator.ofInt(bar.getWidth(), targetWidth);
        anim.setDuration(500);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.addUpdateListener(animation -> {
            bar.getLayoutParams().width = (int) animation.getAnimatedValue();
            bar.requestLayout();
        });
        anim.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null); // Stop polling when activity closes
    }
}