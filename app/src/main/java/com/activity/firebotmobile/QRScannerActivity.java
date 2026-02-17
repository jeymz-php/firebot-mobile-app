package com.activity.firebotmobile;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.BarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;
import org.json.JSONObject;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class QRScannerActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST = 100;
    private BarcodeView barcodeScannerView;
    private String scannedText = null;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("FireBOT_Prefs", MODE_PRIVATE);
        if (prefs.contains("device_id")) {
            startActivity(new Intent(this, SecondActivityLanding.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_qr_scanner);
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        requestQueue = Volley.newRequestQueue(this);
        barcodeScannerView = findViewById(R.id.barcodeScannerView);
        Button btnContinue = findViewById(R.id.btnContinue);
        ImageButton btnClose = findViewById(R.id.btnClose);

        barcodeScannerView.setDecoderFactory(new DefaultDecoderFactory(Collections.singletonList(BarcodeFormat.QR_CODE)));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
        }

        btnContinue.setOnClickListener(v -> {
            if (scannedText != null) {
                // NEW: Verify with server before saving
                verifyQRWithServer(scannedText);
            } else {
                Toast.makeText(this, "Please scan a QR code first.", Toast.LENGTH_SHORT).show();
            }
        });

        btnClose.setOnClickListener(v -> finish());
    }

    private void verifyQRWithServer(String qrData) {
        // Change to your laptop's IP address
        String url = "https://firebot.ucc-bsit.org/controls/verify_qr.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");

                        if (status.equals("valid_user") || status.equals("valid_device")) {
                            // If server says valid, save data and proceed
                            saveAndProceed(qrData);
                        } else if (status.equals("locked")) {
                            Toast.makeText(this, "Device already locked to another user.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this, "Invalid QR Code.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Network error: Check server connection.", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("qr_data", qrData);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void saveAndProceed(String data) {
        try {
            SharedPreferences.Editor editor = getSharedPreferences("FireBOT_Prefs", MODE_PRIVATE).edit();
            String[] lines = data.split("\n");

            for (String line : lines) {
                // Using split(":", 2) ensures we only split at the first colon
                if (line.startsWith("Name:")) editor.putString("full_name", line.split(":", 2)[1].trim());
                if (line.startsWith("Email:")) editor.putString("email", line.split(":", 2)[1].trim());
                if (line.startsWith("DeviceID:")) editor.putString("device_id", line.split(":", 2)[1].trim());
                if (line.startsWith("Model:")) editor.putString("device_model", line.split(":", 2)[1].trim());
            }

            editor.apply();
            startActivity(new Intent(QRScannerActivity.this, SecondActivityLanding.class));
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Save Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void startCamera() {
        barcodeScannerView.decodeContinuous(callback);
        barcodeScannerView.resume();
    }

    private final BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() != null && !result.getText().equals(scannedText)) {
                scannedText = result.getText();
                Toast.makeText(QRScannerActivity.this, "QR Scanned! Tap Continue.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override protected void onResume() { super.onResume(); barcodeScannerView.resume(); }
    @Override protected void onPause() { super.onPause(); barcodeScannerView.pause(); }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            finish();
        }
    }
}