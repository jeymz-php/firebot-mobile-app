package com.activity.firebotmobile;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MapActivity extends AppCompatActivity {
    private MapView map = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

        setContentView(R.layout.activity_map);

        map = findViewById(R.id.mapview);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(false);
        map.setMultiTouchControls(true);
        GeoPoint startPoint = new GeoPoint(14.6416, 120.9762);
        map.getController().setZoom(18.0);
        map.getController().setCenter(startPoint);

        Marker startMarker = new Marker(map);
        startMarker.setPosition(startPoint);

        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);

        startMarker.setTitle("FireBot Location");

        startMarker.setIcon(ContextCompat.getDrawable(this, R.drawable.marker));

        map.getOverlays().add(startMarker);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        ImageButton notificationButton = findViewById(R.id.notificationButton);
        notificationButton.setOnClickListener(v -> {
            NotificationsBottomSheetDialogFragment bottomSheet = new NotificationsBottomSheetDialogFragment();
            bottomSheet.show(getSupportFragmentManager(), NotificationsBottomSheetDialogFragment.TAG);
        });

        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        LinearLayout navMonitoring = findViewById(R.id.nav_monitoring);
        LinearLayout navFire = findViewById(R.id.nav_fire);
        LinearLayout navBattery = findViewById(R.id.nav_battery);
        LinearLayout navHistory = findViewById(R.id.nav_history);
        LinearLayout navChat = findViewById(R.id.nav_chat);
        LinearLayout navLogs = findViewById(R.id.nav_logs);
        LinearLayout navMap = findViewById(R.id.nav_map);
        LinearLayout navProfile = findViewById(R.id.nav_profile);

        navMonitoring.setOnClickListener(v -> { startActivity(new Intent(this, LiveMonitoringActivity.class)); finish(); });
        navFire.setOnClickListener(v -> { startActivity(new Intent(this, FireExtinguisherMonitoringActivity.class)); finish(); });
        navBattery.setOnClickListener(v -> { startActivity(new Intent(this, BatteryActivity.class)); finish(); });
        navHistory.setOnClickListener(v -> { startActivity(new Intent(this, HistoryActivity.class)); finish(); });
        navChat.setOnClickListener(v -> { startActivity(new Intent(this, MessageActivity.class)); finish(); });
        navLogs.setOnClickListener(v -> { startActivity(new Intent(this, LogsActivity.class)); finish(); });

        navMap.setOnClickListener(v -> map.getController().animateTo(new GeoPoint(14.6416, 120.9762)));
        navProfile.setOnClickListener(v -> { startActivity(new Intent(this, ProfileActivity.class)); finish(); });
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }
}