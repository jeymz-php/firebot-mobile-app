package com.activity.firebotmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ProfileActivity extends AppCompatActivity {

    View liveIcon, fireIcon, batteryIcon, historyIcon, chatIcon, logsIcon, profileIcon;
    ImageButton backButton, notificationButton;
    Button forgetRobotButton;

    // Profile TextViews
    TextView profileName, profileEmail, profileContact, profileAddress, profileDeviceId, profileDeviceModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Hide action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Header buttons
        backButton = findViewById(R.id.back_button);
        notificationButton = findViewById(R.id.notificationButton);

        // Forget robot button
        forgetRobotButton = findViewById(R.id.forget_robot_button);

        // Profile TextViews
        profileName = findViewById(R.id.profile_name);
        profileEmail = findViewById(R.id.profile_email);
        profileContact = findViewById(R.id.profile_contact);
        profileAddress = findViewById(R.id.profile_address);
        profileDeviceId = findViewById(R.id.profile_device_id);
        profileDeviceModel = findViewById(R.id.profile_device_model);

        // Bottom navigation
        liveIcon = findViewById(R.id.nav_live);
        fireIcon = findViewById(R.id.nav_fire);
        batteryIcon = findViewById(R.id.nav_battery);
        historyIcon = findViewById(R.id.nav_history);
        chatIcon = findViewById(R.id.nav_chat);
        logsIcon = findViewById(R.id.nav_logs);
        profileIcon = findViewById(R.id.nav_profile);

        // Back button
        backButton.setOnClickListener(v -> finish());

        // Notification bottom sheet
        notificationButton.setOnClickListener(v -> {
            NotificationsBottomSheetDialogFragment bottomSheet = new NotificationsBottomSheetDialogFragment();
            bottomSheet.show(getSupportFragmentManager(), NotificationsBottomSheetDialogFragment.TAG);
        });

        // Forget robot button - WITH VALIDATION
        forgetRobotButton.setOnClickListener(v -> {
            showForgetRobotConfirmationDialog();
        });

        // Bottom navigation
        liveIcon.setOnClickListener(v -> {
            startActivity(new Intent(this, LiveMonitoringActivity.class));
            finish();
        });

        fireIcon.setOnClickListener(v -> {
            startActivity(new Intent(this, FireExtinguisherMonitoringActivity.class));
            finish();
        });

        batteryIcon.setOnClickListener(v -> {
            startActivity(new Intent(this, BatteryActivity.class));
            finish();
        });

        historyIcon.setOnClickListener(v -> {
            startActivity(new Intent(this, HistoryActivity.class));
            finish();
        });

        chatIcon.setOnClickListener(v -> {
            startActivity(new Intent(this, ChatActivity.class));
            finish();
        });

        logsIcon.setOnClickListener(v -> {
            startActivity(new Intent(this, LogsActivity.class));
            finish();
        });

        // profileIcon listener
        profileIcon.setOnClickListener(v -> {
            // Already on profile page, do nothing
        });
    }

    private void showForgetRobotConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_forget_robot_confirmation, null);
        builder.setView(dialogView);

        builder.setPositiveButton("FORGET ROBOT", (dialog, which) -> {
            forgetRobot();
        });

        builder.setNegativeButton("CANCEL", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        // Customize button colors
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(0xFF8E1616); // #8E1616 red
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(android.R.color.black));
    }

    private void forgetRobot() {
        String deviceId = profileDeviceId.getText().toString().trim();
        String userId = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                .getString("user_id", null);

        if (userId == null) {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
            return;
        }

        String apiUrl = "https://firebot.ucc-bsit.org/firebot/api/reset_qr.php";

        new Thread(() -> {
            try {
                URL url = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                String postData = "device_id=" + URLEncoder.encode(deviceId, "UTF-8")
                        + "&user_id=" + URLEncoder.encode(userId, "UTF-8");

                OutputStream os = conn.getOutputStream();
                os.write(postData.getBytes());
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    in.close();

                    JSONObject jsonResponse = new JSONObject(response.toString());
                    runOnUiThread(() -> {
                        if (jsonResponse.optString("status").equals("success")) {
                            showForgetRobotSuccessDialog();
                        } else {
                            Toast.makeText(this, "Error: " + jsonResponse.optString("message"), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(this, "Server error: " + responseCode, Toast.LENGTH_LONG).show()
                    );
                }
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(this, "Network error: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
            }
        }).start();
    }

    private void showForgetRobotSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Robot Forgotten");
        builder.setMessage("The robot has been successfully forgotten. You can now reconnect or add a new robot.");

        builder.setPositiveButton("OK", (dialog, which) -> {
            // Dito pwedeng mag-navigate sa setup screen
            // startActivity(new Intent(this, SetupActivity.class));
            // finish();
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}