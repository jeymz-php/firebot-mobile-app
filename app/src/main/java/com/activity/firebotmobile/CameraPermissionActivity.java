package com.activity.firebotmobile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class CameraPermissionActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_permission);

        ImageView cameraIcon = findViewById(R.id.cameraIcon);
        TextView permissionTitle = findViewById(R.id.permissionTitle);
        TextView permissionDescription = findViewById(R.id.permissionDescription);
        TextView permissionFooterTitle = findViewById(R.id.permissionFooterTitle);
        TextView permissionFooterDesc = findViewById(R.id.permissionFooterDesc);
        LinearLayout buttonContainer = findViewById(R.id.buttonContainer);

        Button btnAllow = findViewById(R.id.btnAllow);
        Button btnDecline = findViewById(R.id.btnDecline);

        Animation bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);
        Animation slideUpFade = AnimationUtils.loadAnimation(this, R.anim.slide_up_fade_in);

        cameraIcon.startAnimation(bounce);

        permissionTitle.startAnimation(slideUpFade);
        permissionDescription.startAnimation(slideUpFade);
        permissionFooterTitle.startAnimation(slideUpFade);
        permissionFooterDesc.startAnimation(slideUpFade);
        buttonContainer.startAnimation(slideUpFade);

        btnAllow.setOnClickListener(v -> requestCameraPermission());
        btnDecline.setOnClickListener(v -> {
            Toast.makeText(this, "Permission denied. QR scanning disabled.", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            openQRScanner();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_CODE);
        }
    }

    private void openQRScanner() {
        Intent intent = new Intent(CameraPermissionActivity.this, QRScannerActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openQRScanner();
            } else {
                Toast.makeText(this, "Camera permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
