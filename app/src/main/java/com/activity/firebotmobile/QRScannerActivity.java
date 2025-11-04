package com.activity.firebotmobile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import java.util.Collections;

public class QRScannerActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST = 100;
    private BarcodeView barcodeScannerView;
    private String scannedText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);

        // Hide the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        barcodeScannerView = findViewById(R.id.barcodeScannerView);
        Button btnContinue = findViewById(R.id.btnContinue);
        ImageButton btnClose = findViewById(R.id.btnClose);

        // QR-only scanning
        barcodeScannerView.setDecoderFactory(
                new DefaultDecoderFactory(Collections.singletonList(BarcodeFormat.QR_CODE))
        );

        // Check camera permission
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
                // Validate scanned QR
                if (scannedText.startsWith("FireBOT_User") || scannedText.startsWith("FireBOT_Device")) {
                    Intent intent = new Intent(QRScannerActivity.this, ScanSuccessActivity.class);
                    intent.putExtra("qr_result", scannedText);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Invalid QR Code. Only FireBOT codes are accepted.", Toast.LENGTH_LONG).show();
                }
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
                scannedText = result.getText().replace("\\n", "\n"); // 👈 this converts literal \n to newline
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
}
