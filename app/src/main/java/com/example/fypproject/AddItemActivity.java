package com.example.fypproject;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fypproject.managers.PermissionsManager;
import com.example.fypproject.models.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;



public class AddItemActivity extends AppCompatActivity {

    private DatabaseReference db;
    private EditText editTextName, editTextDescription, editTextQuantity, editTextBrand,
            editTextCategory, editTextBarcode, editTextPrice, editTextNote, editTextMinQuantity;
    private EditText editTextApproval;
    private ImageView imageProfile;
    private Button buttonComplete, buttonCamera, buttonGallery;
    private Button buttonMinus, buttonPlus;
    private Button buttonBarcode;

    private int randomBRCodeNumber = 1000;

    private TextView textViewResponse;
    private ActivityResultLauncher<Intent> barcodeActivityLauncher;

    //threads
    //===
    //https://programmerworld.co/android/how-to-generate-bar-code-for-any-text-in-your-android-app-android-studio-source-code/#:~:text=%E2%80%93%20Android%20Studio%20Source%20code%20In%20this%20video,bar%20code%20format%20image%20for%20the%20entered%20Text.
    public void generateANewBarcode() {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(editTextName.getText().toString(), BarcodeFormat.CODE_128, editTextName.getWidth(), editTextName.getHeight());
            Bitmap bitmap = Bitmap.createBitmap(editTextName.getWidth(), editTextName.getHeight(), Bitmap.Config.RGB_565);
            for (int i = 0; i < editTextName.getWidth(); i++) {
                for (int j = 0; j < editTextName.getHeight(); j++) {
                    bitmap.setPixel(i, j, bitMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                }
            }
            imageProfile.setImageBitmap(bitmap);//heretest
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);


        db = FirebaseDatabase.getInstance().getReference();


//        //Add data from db
//        FirebaseDatabase database = FirebaseDatabase.getInstance();//duplicate
//        DatabaseReference myRef = database.getReference("inventory");

        //checkAllPermissions();
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        editTextQuantity = (EditText) findViewById(R.id.editTextQuantity);
        editTextBrand = (EditText) findViewById(R.id.editTextBrand);
        editTextCategory = (EditText) findViewById(R.id.editTextCategory);
        editTextBarcode = (EditText) findViewById(R.id.editTextBarcode);
        editTextPrice = (EditText) findViewById(R.id.editTextPrice);
        editTextNote = (EditText) findViewById(R.id.editTextNote1);
        editTextApproval = (EditText) findViewById(R.id.editTextApproval);
        editTextMinQuantity = (EditText) findViewById(R.id.editTextMinQuantity);
        imageProfile = (ImageView) findViewById(R.id.imageProfile);
        imageProfile.setImageResource(R.drawable.image_placeholder);

        buttonComplete = findViewById(R.id.buttonComplete);



        //readData();
        initialisation();
        testInputs();
        buttonComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent itemIntent = new Intent(AddItemActivity.this, InventoryActivity.class);
//                startActivity(itemIntent);
                //here//inputItemDetails();
//                generateANewBarcode();
//                collateData();
                //PermissionsManager.checkAllPermissions();
            }
        });

        buttonCamera = findViewById(R.id.buttonCamera);
        buttonGallery = findViewById(R.id.buttonGallery);
        buttonPlus = findViewById(R.id.buttonPlus);
        buttonMinus = findViewById(R.id.buttonMinus);

        //Add Camera Activity?
        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                checkPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE, IMAGE_PICK_CODE);
//                checkPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
                //pm.checkPermission(android.Manifest.permission.CAMERA, PermissionsManager.CAMERA_PERMISSION_CODE);
                Toast.makeText(AddItemActivity.this, "CAMERA WORKING", Toast.LENGTH_SHORT).show();

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException e) {
                        Log.e("TAG", e.getMessage());
                    }
                    if (photoFile != null) {
                        Uri photoUri = FileProvider.getUriForFile(AddItemActivity.this, "com.example.fypproject.fileProvider", photoFile);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        startActivityForResult(cameraIntent, PermissionsManager.CAMERA_REQUEST_CODE);
                    }
                }

            }
        });

        buttonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pm.checkPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE, PermissionsManager.IMAGE_PICK_CODE);
                pickImageFromGallery();
            }
        });

        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity += 1;
                System.out.println("quantity: " + quantity);
            }
        });

        buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity -= 1;
                System.out.println("quantity: " + quantity);
            }
        });

        buttonBarcode = findViewById(R.id.buttonBarcode);
        barcodeActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String response = result.getData().getStringExtra(BarcodeActivity.EXTRA_SCANNED_RESULT);
                    //textViewResponse.setText(response);
                    editTextBarcode.setText(response);
                }
            }
        });

        buttonBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddItemActivity.this, BarcodeActivity.class);
                barcodeActivityLauncher.launch(intent);
            }
        });
    }

    int ID;
    String name, description, brand, category,
            barcode, notes, approval;
    String defaultUri, newUri;
    int quantity, minQuantity;
    float price;

    private void initialisation() {
        //initialisation with default - declare here
        ID = 0;
        name = "null";
        description = "null";
        quantity = 0;
        minQuantity = 0;
        brand = "brand";
        category = "null";
        barcode = "00000";
        price = 0.0f;
        notes = "null";
        approval = "null";
        testInputs();

        imageFile = new File("/storage/self/primary/Pictures/new_image.jpeg");
        setFields();
    }

    File imageFile;

    private void testInputs() {
        ID = 113;
        name = "testname";
        description = "testdesc";
        quantity += 2;
        minQuantity += 1;
        brand = "testbrand";
        category = "testcategory";
        barcode = "00000000";
        price = 0.0f;
        notes = "testnotes";
        approval = "no";
        imageFile = new File("/storage/self/primary/Pictures/new_image.jpeg");
        setFields();
    }

    private void setFields() {
        editTextName.setText(name);
        editTextDescription.setText(description);
        editTextQuantity.setText(String.valueOf(quantity));
        editTextMinQuantity.setText(String.valueOf(minQuantity));
        editTextBrand.setText(brand);
        editTextCategory.setText(category);
        editTextBarcode.setText(barcode);
        editTextBrand.setText(brand);
        editTextPrice.setText(String.valueOf(price));
        editTextNote.setText(notes);
        editTextApproval.setText(approval);

        if (imageFile.exists()) {
            Log.d("Image", "Image Working");
            Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            //here//imageProfile.setImageBitmap(imageBitmap);
        }
    }

    private void getInputs() {
        name = editTextName.getText().toString();
        description = editTextDescription.getText().toString();
        quantity = Integer.parseInt(editTextQuantity.getText().toString());
        minQuantity = Integer.parseInt(editTextMinQuantity.getText().toString());
        brand = editTextBrand.getText().toString();
        category = editTextCategory.getText().toString();
        barcode = editTextBarcode.getText().toString();
        price = Float.parseFloat(editTextPrice.getText().toString());
        notes = editTextNote.getText().toString();
        approval = editTextApproval.getText().toString();
    }

    private String choosenPhotoPath;

    private void collateData() {
        //initialisation();

//        Item item = new Item(ID, name, description, category, quantity,
//                minQuantity, brand, barcode, defaultUri, notes, price, approval);
//        System.out.println(item.toString());

        getInputs();

        Item item2 = new Item(ID, name, description, category, quantity,
                minQuantity, brand, barcode, choosenPhotoPath, notes, price, approval);

        System.out.println(item2.toString());

        //add to database
        //writeToDatabase(item2);
    }

    private void writeToDatabase(Item item2) {
        db.child("inventory").child(String.valueOf(ID)).setValue(item2).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Void snapshot = task.getResult();
                    //Log.d("TAG", "creator: " + creator);
                    Log.d("TAG", "WORKING LETS CHILL");
                } else {
                    Log.d("TAG", task.getException().getMessage()); //Don't ignore potential errors!
                }
            }
        });
    }

    private void getDataFromDatabase() {
        int _ID = 112;
        String _name = "testname";
        String _description = "testdesc";
        int _quantity = 2;
        int _minQuantity = 1;
        String _brand = "testbrand";
        String _category = "testcategory";
        String _barcode = "00000000";
        float _price = 0.0f;
        String _notes = "testnotes";
        String _approval = "no";
        //File _imageFile = new File("/storage/self/primary/Pictures/new_image.jpeg");
        String _imageString = "/storage/self/primary/Pictures/new_image.jpeg";

        //Item item2 = new Item(ID, name, description, category, quantity,
        // minQuantity, brand, barcode, choosenPhotoPath, notes, price, approval);

        Item newItem = new Item(_ID, _name, _description, _category, _quantity,
                _minQuantity, _brand, _barcode, _imageString, _notes, _price, _approval);
    }

    private String currentPhotoPath;

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void pickImageFromGallery() {
        //Intent to pick image
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PermissionsManager.IMAGE_PICK_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*  Log.d("TAG",currentPhotoPath);*/
        if (requestCode == PermissionsManager.CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Log.d("TAGCameraphoto", currentPhotoPath);

            Glide
                    .with(AddItemActivity.this)
                    .load(currentPhotoPath)
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_foreground)//
                    .into(imageProfile);
            choosenPhotoPath = currentPhotoPath;//
        } else if (requestCode == PermissionsManager.IMAGE_PICK_CODE && resultCode == RESULT_OK && data != null) {
            Log.d("TAGGalleryphoto", String.valueOf(data.getData()));
            Glide
                    .with(AddItemActivity.this)
                    .load(data.getData())
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_foreground)//
                    .into(imageProfile);
            choosenPhotoPath = String.valueOf(data.getData());
        }
    }
}