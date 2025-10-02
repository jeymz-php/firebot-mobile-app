package com.activity.firebotmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

public class LandingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private LandingPagerAdapter adapter;
    private TextView btnSkip;
    private ImageView btnNext;
    private ImageView[] indicators;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_landing);

        initializeViews();
        setupViewPager();
        setupIndicators();
        updateButtonStates();
    }

    private void initializeViews() {
        viewPager = findViewById(R.id.viewPager);
        btnSkip = findViewById(R.id.btnSkip);
        btnNext = findViewById(R.id.btnNext);

        btnSkip.setOnClickListener(v -> onSkipClicked());
        btnNext.setOnClickListener(v -> onNextClicked());
    }

    private void setupViewPager() {
        adapter = new LandingPagerAdapter(this);
        viewPager.setAdapter(adapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateIndicators(position);
                updateButtonStates();
                // DIRECT ANIMATION - NO DELAY
                animateCurrentPageDirect(position);
            }
        });
    }

    private void setupIndicators() {
        indicators = new ImageView[3];
        int[] indicatorIds = {R.id.indicator1, R.id.indicator2, R.id.indicator3};

        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = findViewById(indicatorIds[i]);
        }
        updateIndicators(0);
    }

    private void updateIndicators(int position) {
        for (int i = 0; i < indicators.length; i++) {
            if (i == position) {
                indicators[i].setImageResource(R.drawable.indicator_active);
            } else {
                indicators[i].setImageResource(R.drawable.indicator_inactive);
            }
        }
    }

    private void updateButtonStates() {
        int currentItem = viewPager.getCurrentItem();

        if (currentItem == adapter.getItemCount() - 1) {
            btnSkip.setVisibility(View.GONE);
        } else {
            btnSkip.setVisibility(View.VISIBLE);
        }
    }

    private void animateCurrentPageDirect(int position) {
        // DIRECT APPROACH - ANIMATE THE CURRENT VISIBLE PAGE
        View currentPage = getCurrentPageView(position);
        if (currentPage != null) {
            ImageView image = currentPage.findViewById(R.id.pageImage);
            TextView title = currentPage.findViewById(R.id.pageTitle);
            TextView description = currentPage.findViewById(R.id.pageDescription);
            TextView terms = currentPage.findViewById(R.id.pageTerms);

            animateImage(image);
            animateTitle(title);
            animateDescription(description);

            if (terms != null && terms.getVisibility() == View.VISIBLE) {
                animateTerms(terms);
            }
        }
    }

    private View getCurrentPageView(int position) {
        RecyclerView recyclerView = (RecyclerView) viewPager.getChildAt(0);
        if (recyclerView != null && recyclerView.getLayoutManager() != null) {
            return recyclerView.getLayoutManager().findViewByPosition(position);
        }
        return null;
    }

    private void animateImage(ImageView image) {
        if (image != null) {
            image.setAlpha(0f);
            image.setScaleX(0.5f);
            image.setScaleY(0.5f);
            image.setRotation(-15f);

            image.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .rotation(0f)
                    .setDuration(600)
                    .setInterpolator(new OvershootInterpolator(1.2f))
                    .withEndAction(() -> startImageBreathing(image))
                    .start();
        }
    }

    private void animateTitle(TextView title) {
        if (title != null) {
            title.setAlpha(0f);
            title.setTranslationY(50f);
            title.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(500)
                    .start();
        }
    }

    private void animateDescription(TextView description) {
        if (description != null) {
            description.setAlpha(0f);
            description.setTranslationY(50f);
            description.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(500)
                    .setStartDelay(100)
                    .start();
        }
    }

    private void animateTerms(TextView terms) {
        if (terms != null) {
            terms.setAlpha(0f);
            terms.setTranslationY(30f);
            terms.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(500)
                    .setStartDelay(300)
                    .start();
        }
    }

    private void startImageBreathing(ImageView image) {
        if (image != null) {
            image.animate()
                    .scaleX(1.03f)
                    .scaleY(1.03f)
                    .setDuration(400)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .withEndAction(() -> {
                        image.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(400)
                                .start();
                    })
                    .start();
        }
    }

    private void onSkipClicked() {
        proceedToMain();
    }

    private void onNextClicked() {
        int currentItem = viewPager.getCurrentItem();
        int nextItem = currentItem + 1;

        if (nextItem < adapter.getItemCount()) {
            // Force refresh and animation
            viewPager.setCurrentItem(nextItem, true);

            // Manual refresh after a short delay
            new android.os.Handler().postDelayed(() -> {
                updateIndicators(nextItem);
                updateButtonStates();
                animateCurrentPageDirect(nextItem);
            }, 100);
        } else {
            proceedToMain();
        }
    }

    private void proceedToMain() {
        Intent intent = new Intent(LandingActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void showTermsAndConditions() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Terms & Conditions");
        builder.setMessage("TERMS OF USE\n\n" +
                "1. Acceptance of Terms\n" +
                "By using Firebot Mobile, you agree to these terms and conditions.\n\n" +
                "2. Privacy Policy\n" +
                "We respect your privacy and are committed to protecting your personal data.\n\n" +
                "3. User Responsibilities\n" +
                "You are responsible for maintaining the confidentiality of your account.\n\n" +
                "4. Limitation of Liability\n" +
                "Firebot Mobile is provided as-is without any warranties.\n\n" +
                "5. Contact Information\n" +
                "For questions about these terms, contact us at support@firebot.com");

        builder.setPositiveButton("AGREE", (dialog, which) -> {
            dialog.dismiss();
        });

        builder.setNegativeButton("CLOSE", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}