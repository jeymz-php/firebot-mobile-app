package com.activity.firebotmobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class MapActivity extends AppCompatActivity {
    private MapView map = null;
    private TextView coordinatesText;
    private RequestQueue requestQueue;
    private HashMap<Integer, Marker> robotMarkers = new HashMap<>();
    private final String BASE_URL = "https://firebot.ucc-bsit.org/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        setContentView(R.layout.activity_map);

        // --- THE FIX: ADD THIS TO REMOVE THE TOP RED ACTION BAR ---
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        coordinatesText = findViewById(R.id.coordinates_text);
        requestQueue = Volley.newRequestQueue(this);

        initMap();
        setupButtons();
        setupBottomNavigation();
        fetchFireStations();
        fetchBarangays();
        startRobotPolling();
    }

    private void initMap() {
        map = findViewById(R.id.mapview);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        GeoPoint caloocanCenter = new GeoPoint(14.6545, 120.9840);
        map.getController().setZoom(15.0);
        map.getController().setCenter(caloocanCenter);
    }

    private void startRobotPolling() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchRobots();
                handler.postDelayed(this, 10000);
            }
        }, 0);
    }

    private String formatToManilaTime(String timestamp) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = sdf.parse(timestamp);
            SimpleDateFormat displayFormat = new SimpleDateFormat("MMM dd, yyyy - hh:mm:ss a", Locale.getDefault());
            displayFormat.setTimeZone(TimeZone.getTimeZone("Asia/Manila"));
            return displayFormat.format(date);
        } catch (Exception e) { return timestamp; }
    }

    private void fetchRobots() {
        String url = BASE_URL + "controls/get_robots.php";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject robot = response.getJSONObject(i);
                            int id = robot.getInt("id");
                            double lat = robot.getDouble("latitude");
                            double lon = robot.getDouble("longitude");
                            String name = robot.getString("name");
                            String status = robot.getString("status");
                            String lastUpdated = robot.getString("last_updated");
                            String manilaTime = formatToManilaTime(lastUpdated);
                            GeoPoint pos = new GeoPoint(lat, lon);

                            if (robotMarkers.containsKey(id)) {
                                Marker m = robotMarkers.get(id);
                                m.setPosition(pos);
                                m.setSnippet("Status: " + status + "\nLast Sync: " + manilaTime);
                            } else {
                                Marker m = new Marker(map);
                                m.setPosition(pos);
                                m.setTitle(name);
                                m.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_robot_marker));
                                map.getOverlays().add(m);
                                robotMarkers.put(id, m);
                            }
                            coordinatesText.setText("Robot: " + name + "\nStatus: " + status + "\nUpdated: " + manilaTime);
                        }
                        map.invalidate();
                    } catch (JSONException e) { e.printStackTrace(); }
                }, error -> Toast.makeText(MapActivity.this, "Sync Failed", Toast.LENGTH_SHORT).show());
        requestQueue.add(request);
    }

    private void fetchFireStations() {
        String url = BASE_URL + "api/get_fire_stations.php";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject station = response.getJSONObject(i);
                            Marker m = new Marker(map);
                            m.setPosition(new GeoPoint(station.getDouble("latitude"), station.getDouble("longitude")));
                            m.setTitle(station.getString("station_name"));
                            m.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_fire_station_marker));
                            map.getOverlays().add(m);
                        }
                        map.invalidate();
                    } catch (JSONException e) { e.printStackTrace(); }
                }, null);
        requestQueue.add(request);
    }

    private void fetchBarangays() {
        String url = BASE_URL + "api/get_barangays.php";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject brgy = response.getJSONObject(i);
                            Marker m = new Marker(map);
                            m.setPosition(new GeoPoint(brgy.getDouble("latitude"), brgy.getDouble("longitude")));
                            m.setTitle(brgy.getString("barangay_name"));
                            m.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_barangay_marker));
                            map.getOverlays().add(m);
                        }
                        map.invalidate();
                    } catch (JSONException e) { e.printStackTrace(); }
                }, null);
        requestQueue.add(request);
    }

    private void setupButtons() {
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
        findViewById(R.id.notificationButton).setOnClickListener(v -> {
            NotificationsBottomSheetDialogFragment bottomSheet = new NotificationsBottomSheetDialogFragment();
            bottomSheet.show(getSupportFragmentManager(), NotificationsBottomSheetDialogFragment.TAG);
        });
    }

    private void setupBottomNavigation() {
        findViewById(R.id.nav_monitoring).setOnClickListener(v -> { startActivity(new Intent(this, LiveMonitoringActivity.class)); finish(); });
        // Removed fire and battery navigation
        findViewById(R.id.nav_history).setOnClickListener(v -> { startActivity(new Intent(this, HistoryActivity.class)); finish(); });
        findViewById(R.id.nav_chat).setOnClickListener(v -> { startActivity(new Intent(this, MessageActivity.class)); finish(); });
        findViewById(R.id.nav_logs).setOnClickListener(v -> { startActivity(new Intent(this, LogsActivity.class)); finish(); });
        findViewById(R.id.nav_map).setOnClickListener(v -> map.getController().animateTo(new GeoPoint(14.6545, 120.9840)));
        findViewById(R.id.nav_profile).setOnClickListener(v -> { startActivity(new Intent(this, ProfileActivity.class)); finish(); });
    }

    @Override public void onResume() { super.onResume(); map.onResume(); }
    @Override public void onPause() { super.onPause(); map.onPause(); }
}