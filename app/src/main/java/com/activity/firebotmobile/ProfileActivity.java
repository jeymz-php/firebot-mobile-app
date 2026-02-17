package com.activity.firebotmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ProfileActivity extends AppCompatActivity {

    // Removed fire and battery icons
    View liveIcon, historyIcon, chatIcon, logsIcon, mapIcon, profileIcon;
    ImageButton backButton, notificationButton;
    Button forgetRobotButton;
    TextView profileName, profileEmail, profileContact, profileAddress, profileDeviceId, profileDeviceModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        profileName = findViewById(R.id.profile_name);
        profileEmail = findViewById(R.id.profile_email);
        profileContact = findViewById(R.id.profile_contact);
        profileAddress = findViewById(R.id.profile_address);
        profileDeviceId = findViewById(R.id.profile_device_id);
        profileDeviceModel = findViewById(R.id.profile_device_model);
        forgetRobotButton = findViewById(R.id.forget_robot_button);

        backButton = findViewById(R.id.back_button);
        notificationButton = findViewById(R.id.notificationButton);

        loadProfileData();

        liveIcon = findViewById(R.id.nav_monitoring);
        historyIcon = findViewById(R.id.nav_history);
        chatIcon = findViewById(R.id.nav_chat);
        logsIcon = findViewById(R.id.nav_logs);
        mapIcon = findViewById(R.id.nav_map);
        profileIcon = findViewById(R.id.nav_profile);

        setupListeners();
    }

    private void loadProfileData() {
        SharedPreferences prefs = getSharedPreferences("FireBOT_Prefs", MODE_PRIVATE);
        profileName.setText(prefs.getString("full_name", "No Name Available"));
        profileEmail.setText(prefs.getString("email", "No Email Available"));
        profileDeviceId.setText(prefs.getString("device_id", "Not Bound"));
        profileDeviceModel.setText(prefs.getString("device_model", "Standard Model"));
        profileContact.setText(prefs.getString("contact", "N/A"));
        profileAddress.setText(prefs.getString("address", "N/A"));
    }

    private void setupListeners() {
        backButton.setOnClickListener(v -> finish());

        notificationButton.setOnClickListener(v -> {
            NotificationsBottomSheetDialogFragment bottomSheet = new NotificationsBottomSheetDialogFragment();
            bottomSheet.show(getSupportFragmentManager(), NotificationsBottomSheetDialogFragment.TAG);
        });

        forgetRobotButton.setOnClickListener(v -> showForgetRobotConfirmationDialog());

        liveIcon.setOnClickListener(v -> navigateTo(LiveMonitoringActivity.class));
        // Removed fire and battery navigation
        historyIcon.setOnClickListener(v -> navigateTo(HistoryActivity.class));
        chatIcon.setOnClickListener(v -> navigateTo(ChatActivity.class));
        logsIcon.setOnClickListener(v -> navigateTo(LogsActivity.class));
        mapIcon.setOnClickListener(v -> navigateTo(MapActivity.class));
    }

    private void navigateTo(Class<?> cls) {
        startActivity(new Intent(this, cls));
        finish();
    }

    private void showForgetRobotConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Forget this Robot?");
        builder.setMessage("This will remove the robot's details. Are you sure?");

        builder.setPositiveButton("FORGET", (dialog, which) -> forgetRobot());
        builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(0xFF8E1616);
    }

    private void forgetRobot() {
        SharedPreferences prefs = getSharedPreferences("FireBOT_Prefs", MODE_PRIVATE);
        String deviceId = prefs.getString("device_id", null);

        if (deviceId == null || deviceId.equals("Not Bound")) {
            Toast.makeText(this, "No bound robot found.", Toast.LENGTH_SHORT).show();
            return;
        }

        String apiUrl = "https://firebot.ucc-bsit.org/api/reset_qr.php";

        new Thread(() -> {
            try {
                URL url = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                String postData = "device_id=" + URLEncoder.encode(deviceId, "UTF-8");
                OutputStream os = conn.getOutputStream();
                os.write(postData.getBytes());
                os.flush();
                os.close();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.clear();
                    editor.apply();

                    runOnUiThread(() -> {
                        Toast.makeText(ProfileActivity.this, "Robot forgotten.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ProfileActivity.this, QRScannerActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    });
                }
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}