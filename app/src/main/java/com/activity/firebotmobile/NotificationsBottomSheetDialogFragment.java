package com.activity.firebotmobile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.util.ArrayList;
import java.util.List;

public class NotificationsBottomSheetDialogFragment extends BottomSheetDialogFragment {

    public static final String TAG = "NotificationsBottomSheetDialogFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.notifications_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView clearAllButton = view.findViewById(R.id.clear_all_button);
        clearAllButton.setOnClickListener(v -> {
        });

        RecyclerView notificationsRecyclerView = view.findViewById(R.id.notifications_recycler_view);
        notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<NotificationItem> dummyNotifications = createDummyNotifications();

        NotificationAdapter adapter = new NotificationAdapter(dummyNotifications);
        notificationsRecyclerView.setAdapter(adapter);
    }
    private List<NotificationItem> createDummyNotifications() {
        List<NotificationItem> list = new ArrayList<>();
        list.add(new NotificationItem("FIRE DETECTED!", "Robot has detected flames in Zone A. Moving into position and activating extinguisher.", "2m ago", R.drawable.ic_fire2));
        list.add(new NotificationItem("GAS DETECTED!", "High gas concentration detected by MQ-9 sensor. Please check for leaks.", "2s ago", R.drawable.ic_gas_smoke));
        list.add(new NotificationItem("LOW BATTERY", "Battery at 18%. Please connect charger to avoid shutdown.", "2m ago", R.drawable.ic_battery_white));
        list.add(new NotificationItem("EXTINGUISHER LOW LEVEL", "Remaining agent is at 25%. Refill soon to maintain effectiveness.", "2m ago", R.drawable.ic_fire_extinguisher_white));
        return list;
    }
}