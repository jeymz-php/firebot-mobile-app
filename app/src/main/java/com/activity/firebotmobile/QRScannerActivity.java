package com.activity.firebotmobile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.BarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collections;

public class QRScannerActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST = 100;
    private BarcodeView barcodeScannerView;
    private String scannedText = null;

    // Replace this with your server endpoint
    private final String QR_VERIFY_URL = "https://firebot.ucc-bsit.org/controls/verify_qr.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        barcodeScannerView = findViewById(R.id.barcodeScannerView);
        Button btnContinue = findViewById(R.id.btnContinue);
        ImageButton btnClose = findViewById(R.id.btnClose);

        // QR-only scanning
        barcodeScannerView.setDecoderFactory(
                new DefaultDecoderFactory(Collections.singletonList(BarcodeFormat.QR_CODE))
        );

        // Enable camera autofocus
        barcodeScannerView.getCameraSettings().setAutoFocusEnabled(true);
        barcodeScannerView.getCameraSettings().setContinuousFocusEnabled(true);

        // Start camera if permission granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST);
        }

        btnContinue.setOnClickListener(v -> {
            if (scannedText != null) {
                // Verify QR via server
                new VerifyQRTask().execute(scannedText);
            } else {
                Toast.makeText(this, "Please scan a QR code first.", Toast.LENGTH_SHORT).show();
            }
        });

        btnClose.setOnClickListener(v -> finish());
    }

    private void startCamera() {
        barcodeScannerView.decodeContinuous(callback);
        barcodeScannerView.resume();
    }

    private final BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() != null && !result.getText().equals(scannedText)) {
                scannedText = result.getText(); // Remove literal \n replacement
                Toast.makeText(QRScannerActivity.this,
                        "QR Scanned: " + scannedText, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            barcodeScannerView.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeScannerView.pause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(this, "Camera permission is required to scan QR codes.", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    // === AsyncTask to verify QR code on server with JSON parsing ===
    private class VerifyQRTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            Toast.makeText(QRScannerActivity.this, "Verifying QR code...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            String qrCode = params[0];
            try {
                URL url = new URL(QR_VERIFY_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                // URL-encode the QR code
                String postData = "qr_data=" + URLEncoder.encode(qrCode, "UTF-8");

                OutputStream os = conn.getOutputStream();
                os.write(postData.getBytes("UTF-8"));
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                in.close();

                // Log raw response for debugging
                String rawResponse = sb.toString();
                System.out.println("RAW SERVER RESPONSE: " + rawResponse);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    return new JSONObject(rawResponse);
                } else {
                    JSONObject errorObj = new JSONObject();
                    errorObj.put("status", "error");
                    errorObj.put("message", "Server returned response code: " + responseCode + "\n" + rawResponse);
                    return errorObj;
                }

            } catch (Exception e) {
                e.printStackTrace();
                try {
                    JSONObject errorObj = new JSONObject();
                    errorObj.put("status", "error");
                    errorObj.put("message", e.getMessage());
                    return errorObj;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return null;
                }
            }
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if (result == null) {
                Toast.makeText(QRScannerActivity.this, "Failed to verify QR code.", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                String status = result.getString("status");
                String message = result.optString("message", "No message from server");

                switch (status) {
                    case "valid_user":
                    case "valid_device":
                        Toast.makeText(QRScannerActivity.this, message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(QRScannerActivity.this, ScanSuccessActivity.class);
                        intent.putExtra("qr_result", scannedText);
                        startActivity(intent);
                        finish();
                        break;

                    case "locked":
                    case "invalid":
                    case "error":
                        Toast.makeText(QRScannerActivity.this, message, Toast.LENGTH_LONG).show();
                        break;

                    default:
                        Toast.makeText(QRScannerActivity.this, "Unknown status: " + status, Toast.LENGTH_LONG).show();
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(QRScannerActivity.this, "Error parsing server response.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
