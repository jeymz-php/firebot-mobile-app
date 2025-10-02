package com.activity.firebotmobile;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LandingPagerAdapter extends RecyclerView.Adapter<LandingPagerAdapter.ViewHolder> {

    private final LandingActivity activity;

    // CORRECT IMAGE ORDER BASED ON YOUR PAGES
    private final int[] images = {
            R.drawable.cctv,           // Page 1: Early Detection
            R.drawable.alert,          // Page 2: Automatic alerts
            R.drawable.monitor         // Page 3: Real-Time Monitoring
    };

    private final String[] titles = {
            "Early Detection",
            "Automatic alerts",
            "Real-Time Monitoring"
    };

    private final String[] descriptions = {
            "Detect fire, smoke & gas leaks indoors\nfor early warning and safety.",
            "Activates the extinguisher for small fires\nand alerts the fire station if fire grows.",
            "Provides lives camera view to track and\nmonitor fire situations."
    };

    public LandingPagerAdapter(LandingActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_landing_page, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // SET THE CORRECT IMAGE FOR EACH PAGE
        holder.imageView.setImageResource(images[position]);
        holder.title.setText(titles[position]);
        holder.description.setText(descriptions[position]);

        // Show Terms & Conditions only on last page
        if (position == 2) {
            holder.terms.setVisibility(View.VISIBLE);
            setupTermsAndConditions(holder.terms);
        } else {
            holder.terms.setVisibility(View.GONE);
        }

        // Set initial state for animations
        holder.imageView.setAlpha(0f);
        holder.title.setAlpha(0f);
        holder.description.setAlpha(0f);
        holder.terms.setAlpha(0f);
    }

    private void setupTermsAndConditions(TextView termsText) {
        String fullText = "By continuing you agree to our Terms & Conditions";
        SpannableString spannable = new SpannableString(fullText);

        int start = fullText.indexOf("Terms & Conditions");
        int end = start + "Terms & Conditions".length();

        ForegroundColorSpan blueColor = new ForegroundColorSpan(Color.parseColor("#2196F3"));
        spannable.setSpan(blueColor, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                activity.showTermsAndConditions();
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(Color.parseColor("#2196F3"));
            }
        };
        spannable.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        termsText.setText(spannable);
        termsText.setMovementMethod(LinkMovementMethod.getInstance());
        termsText.setHighlightColor(0x00000000);
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;
        TextView description;
        TextView terms;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.pageImage);
            title = itemView.findViewById(R.id.pageTitle);
            description = itemView.findViewById(R.id.pageDescription);
            terms = itemView.findViewById(R.id.pageTerms);
        }
    }
}