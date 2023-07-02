package com.example.fypproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BarcodeActivity extends AppCompatActivity {
    interface LuminosityAnalyzer {
        void inputImage(InputImage inputImage);
    }
    public static final String EXTRA_SCANNED_RESULT = "Extra_scanned_result";
    private PreviewView previewView;
    private BarcodeScanner scanner;

    private static final int PERMISSION_CAMERA_CODE = 1;
    private ExecutorService cameraExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        previewView = findViewById(R.id.previewView);
        if(isCameraPermissionGranted()){
            setupCamera();
        }else{
            //
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},PERMISSION_CAMERA_CODE);
        }

        cameraExecutor = Executors.newSingleThreadExecutor();

        BarcodeScannerOptions options = new BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_CODE_128).build();
        scanner = BarcodeScanning.getClient(options);
    }

    private void setupCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture = ProcessCameraProvider.getInstance(this);
        LuminosityAnalyzer analyzer = new LuminosityAnalyzer() {
            @Override
            public void inputImage(InputImage inputImage) {
                scanner.process(inputImage).addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                    @Override
                    public void onSuccess(List<Barcode> barcodes) {
                        for (Barcode barcode : barcodes
                        ) {
                            String value = barcode.getRawValue();
                            if (value != null) {
                                Intent intent = new Intent();
                                Log.d("TAGbarcode", value);
                                intent.putExtra(EXTRA_SCANNED_RESULT, value);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        }
                        //return false;
                    }
                });
            }
        };
        cameraProviderListenableFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderListenableFuture.get();

                    //Perview
                    Preview preview = new Preview.Builder().build();
                    preview.setSurfaceProvider(previewView.getSurfaceProvider());


                    ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().build();
                    imageAnalysis.setAnalyzer(cameraExecutor, new ImageAnalysis.Analyzer() {

                        @Override
                        public void analyze(@NonNull ImageProxy imageProxy) {
                            Image mediaImage = imageProxy.getImage();
                            if (mediaImage != null) {
                                InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
                                analyzer.inputImage(image);
                            }
                            imageProxy.close();
                        }
                    });

                    CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
                    cameraProvider.unbindAll();
                    cameraProvider.bindToLifecycle(BarcodeActivity.this, cameraSelector, preview, imageAnalysis);
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }, ContextCompat.getMainExecutor(this));//
    }

    private boolean isCameraPermissionGranted() {
        return ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
