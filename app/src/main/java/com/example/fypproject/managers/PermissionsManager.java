package com.example.fypproject.managers;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.fypproject.AddItemActivity;

public abstract class PermissionsManager extends AppCompatActivity {
    public static final int IMAGE_PICK_CODE = 1000;
    public static final int STORAGE_PERMISSION_CODE = 101;
    public static final int INTERNET_CODE = 102;
    public static final int NETWORK_CODE = 103;
    public static final int CAMERA_PERMISSION_CODE = 100;//?
    public static final int CAMERA_REQUEST_CODE = 2000;
    public static final int SMS_REQUEST_CODE = 3000;



    public void checkAllPermissions() {
        checkPermission(android.Manifest.permission.INTERNET, INTERNET_CODE);
        checkPermission(android.Manifest.permission.ACCESS_NETWORK_STATE, NETWORK_CODE);
        Log.d("taghere", "did pass here");
        checkPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE, IMAGE_PICK_CODE);

//        checkPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
        checkPermission(android.Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
        checkPermission(Manifest.permission.SEND_SMS, SMS_REQUEST_CODE);
    }

    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(PermissionsManager.this, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(PermissionsManager.this, new String[]{permission}, requestCode);
        } else {
            Toast.makeText(PermissionsManager.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PermissionsManager.CAMERA_PERMISSION_CODE) {
            // Checking whether user granted the permission or not.
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Showing the toast message
                Toast.makeText(PermissionsManager.this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PermissionsManager.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(PermissionsManager.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PermissionsManager.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == IMAGE_PICK_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(PermissionsManager.this, "Gallery Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PermissionsManager.this, "Gallery Permission Denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == INTERNET_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(PermissionsManager.this, "Internet Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PermissionsManager.this, "Internet Permission Denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == NETWORK_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(PermissionsManager.this, "Network Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PermissionsManager.this, "Network Permission Denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == SMS_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, send the SMS
                Toast.makeText(PermissionsManager.this, "SMS permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                // Permission denied
                Toast.makeText(PermissionsManager.this, "SMS permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
