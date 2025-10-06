package com.activity.firebotmobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private TextView[] letters;
    private ImageView logoImage;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initializeViews();
        startAnimationSequence();
    }

    private void initializeViews() {
        letters = new TextView[]{
                findViewById(R.id.letterF),
                findViewById(R.id.letterI),
                findViewById(R.id.letterR),
                findViewById(R.id.letterE),
                findViewById(R.id.letterB),
                findViewById(R.id.letterO),
                findViewById(R.id.letterT)
        };

        logoImage = findViewById(R.id.logoImage);
    }

    private void startAnimationSequence() {
        // 1. LOGO MUNA - Elegant entrance
        handler.postDelayed(this::animateLogoEntrance, 400);

        // 2. FONT SUNOD - After logo settles
        handler.postDelayed(this::startTextAnimation, 1200);

        // 3. FINAL SYNC ANIMATION - Logo and text dance together
        handler.postDelayed(this::startFinalSyncAnimation, 2800);

        // 4. Navigate to LANDING ACTIVITY (not MainActivity)
        // Sa startAnimationSequence method, palitan ang:
        handler.postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, LandingActivity.class);
            startActivity(intent);
            finish();
        }, 5000);
    }

    private void animateLogoEntrance() {
        logoImage.setVisibility(ImageView.VISIBLE);
        logoImage.setAlpha(0f);
        logoImage.setScaleX(0.5f);
        logoImage.setScaleY(0.5f);
        logoImage.setRotation(-15f);

        // ELEGANT ENTRANCE: Scale + Rotate + Fade
        logoImage.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .rotation(0f)
                .setDuration(800)
                .setInterpolator(new OvershootInterpolator(1.2f))
                .withEndAction(this::startLogoBreathing)
                .start();
    }

    private void startLogoBreathing() {
        // BREATHING EFFECT - Subtle pulse
        logoImage.animate()
                .scaleX(1.05f)
                .scaleY(1.05f)
                .setDuration(600)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .withEndAction(() -> {
                    logoImage.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(600)
                            .start();
                })
                .start();
    }

    private void startTextAnimation() {
        // STAGGERED ENTRANCE with bounce
        for (int i = 0; i < letters.length; i++) {
            final int index = i;
            handler.postDelayed(() -> animateLetterSpring(letters[index], index), i * 120);
        }
    }

    private void animateLetterSpring(TextView letter, int index) {
        letter.setAlpha(0f);
        letter.setScaleX(0.3f);
        letter.setScaleY(0.3f);
        letter.setTranslationY(50f);

        // SPRING ENTRANCE with overshoot
        letter.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .translationY(0f)
                .setDuration(600)
                .setInterpolator(new AnticipateOvershootInterpolator(1.0f))
                .withEndAction(() -> {
                    // Small celebration bounce after all letters appear
                    if (index == letters.length - 1) {
                        startLettersCelebration();
                    }
                })
                .start();
    }

    private void startLettersCelebration() {
        // ALL LETTERS BOUNCE TOGETHER
        for (int i = 0; i < letters.length; i++) {
            final TextView letter = letters[i];
            handler.postDelayed(() -> {
                letter.animate()
                        .translationY(-15f)
                        .setDuration(200)
                        .withEndAction(() -> {
                            letter.animate()
                                    .translationY(0f)
                                    .setDuration(200)
                                    .setInterpolator(new BounceInterpolator())
                                    .start();
                        })
                        .start();
            }, i * 80);
        }
    }

    private void startFinalSyncAnimation() {
        // LOGO AND TEXT DANCE TOGETHER
        handler.postDelayed(() -> {
            // Logo bounce
            logoImage.animate()
                    .scaleX(1.1f)
                    .scaleY(1.1f)
                    .setDuration(300)
                    .withEndAction(() -> {
                        logoImage.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(300)
                                .start();
                    })
                    .start();

            // Text wave in sync
            for (int i = 0; i < letters.length; i++) {
                final TextView letter = letters[i];
                handler.postDelayed(() -> {
                    letter.animate()
                            .scaleX(1.05f)
                            .scaleY(1.05f)
                            .setDuration(200)
                            .withEndAction(() -> {
                                letter.animate()
                                        .scaleX(1f)
                                        .scaleY(1f)
                                        .setDuration(200)
                                        .start();
                            })
                            .start();
                }, i * 60);
            }
        }, 200);

        // SECOND SYNC CYCLE - Head shake together
        handler.postDelayed(this::startSyncHeadShake, 1000);
    }

    private void startSyncHeadShake() {
        // LOGO HEAD SHAKE
        logoImage.animate()
                .rotation(-8f)
                .setDuration(150)
                .withEndAction(() -> {
                    logoImage.animate()
                            .rotation(8f)
                            .setDuration(150)
                            .withEndAction(() -> {
                                logoImage.animate()
                                        .rotation(0f)
                                        .setDuration(150)
                                        .start();
                            })
                            .start();
                })
                .start();

        // TEXT HEAD SHAKE IN SYNC (delayed slightly)
        handler.postDelayed(() -> {
            for (TextView letter : letters) {
                letter.animate()
                        .rotation(-5f)
                        .setDuration(120)
                        .withEndAction(() -> {
                            letter.animate()
                                    .rotation(5f)
                                    .setDuration(120)
                                    .withEndAction(() -> {
                                        letter.animate()
                                                .rotation(0f)
                                                .setDuration(120)
                                                .start();
                                    })
                                    .start();
                        })
                        .start();
            }
        }, 100);
    }
}