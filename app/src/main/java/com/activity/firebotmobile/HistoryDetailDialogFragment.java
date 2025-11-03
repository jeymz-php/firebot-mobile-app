package com.activity.firebotmobile;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class HistoryDetailDialogFragment extends DialogFragment {

    public static final String TAG = "HistoryDetailDialogFragment";



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        return inflater.inflate(R.layout.dialog_history_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((TextView) view.findViewById(R.id.dialog_title)).setText("SMS Alert Sent");
        ((TextView) view.findViewById(R.id.value_date_time)).setText("Today, 10:19 AM");
        ((TextView) view.findViewById(R.id.value_type)).setText("SMS Alert");
        ((TextView) view.findViewById(R.id.value_description)).setText("Fire Emergency alert sent to near fire station");
        ((TextView) view.findViewById(R.id.value_recipient)).setText("Fire station");
        ((TextView) view.findViewById(R.id.value_status)).setText("Delivered");
        ((TextView) view.findViewById(R.id.value_message_content)).setText("Fire emergency detected in kitchen area. Robot has responded. Fire extinguished at 10:18 AM.");

        ImageView closeButton = view.findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> dismiss()); // Close the dialog
    }
}