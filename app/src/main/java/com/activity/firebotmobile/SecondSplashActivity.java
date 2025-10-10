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

public class SecondSplashActivity extends AppCompatActivity {

    private TextView[] letters;
    private ImageView logoImage;
    private TextView subtitle;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_splash);

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
        subtitle = findViewById(R.id.splashSubtitle); // subtitle view
    }

    private void startAnimationSequence() {

        handler.postDelayed(this::animateLogoEntrance, 400);

        handler.postDelayed(this::startTextAnimation, 1200);

        handler.postDelayed(this::showSubtitle, 2500);

        handler.postDelayed(this::startFinalSyncAnimation, 3000);

        handler.postDelayed(() -> {
            Intent intent = new Intent(SecondSplashActivity.this, ScanIntroActivity.class);
            startActivity(intent);

            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            finish();
        }, 5000);

    }

    private void animateLogoEntrance() {
        logoImage.setVisibility(ImageView.VISIBLE);
        logoImage.setAlpha(0f);
        logoImage.setScaleX(0.5f);
        logoImage.setScaleY(0.5f);
        logoImage.setRotation(-15f);

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

        letter.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .translationY(0f)
                .setDuration(600)
                .setInterpolator(new AnticipateOvershootInterpolator(1.0f))
                .withEndAction(() -> {
                    if (index == letters.length - 1) {
                        startLettersCelebration();
                    }
                })
                .start();
    }

    private void startLettersCelebration() {
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

    private void showSubtitle() {
        if (subtitle != null) {
            subtitle.setAlpha(0f);
            subtitle.animate()
                    .alpha(1f)
                    .setDuration(600)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .start();
        }
    }

    private void startFinalSyncAnimation() {
        handler.postDelayed(() -> {
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

        handler.postDelayed(this::startSyncHeadShake, 1000);
    }

    private void startSyncHeadShake() {
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
