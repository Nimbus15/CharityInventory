package com.example.fypproject;


import static com.example.fypproject.InventoryActivity.COPY_WORD;
import static com.example.fypproject.InventoryActivity.ITEM_WORD;
import static com.example.fypproject.InventoryActivity.numOfItemInInventory;
import static com.example.fypproject.globals.Globals.INVENTORY_WORD;
import static com.example.fypproject.globals.Globals.MANAGER_WORD;
import static com.example.fypproject.globals.Globals.TRANSACTION_WORD;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fypproject.managers.PermissionsManager;
import com.example.fypproject.models.Account;
import com.example.fypproject.models.Item;
import com.example.fypproject.models.Transaction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



public class AddItemActivity extends AppCompatActivity {

    private DatabaseReference db;

    private EditText editTextName, editTextDescription, editTextQuantity, editTextBrand,
            editTextCategory, editTextBarcode, editTextPrice, editTextNotes, editTextMinQuantity;
    private EditText editTextApproval;
    private ImageView imageProfile;
    private Button buttonComplete, buttonCamera, buttonGallery;
    private Button buttonMinus, buttonPlus;
    private Button buttonBarcode;

    private int randomBRCodeNumber = 1000;

    private TextView textViewResponse;
    private ActivityResultLauncher<Intent> barcodeActivityLauncher;
    private ProgressBar progressBar;

    /*
    Programmerworld
    "How to generate bar code for any text in your Android App?"
    https://programmerworld.co/android/how-to-generate-bar-code-for-any-text-in-your-android-app-android-studio-source-code/#:~:text=%E2%80%93%20Android%20Studio%20Source%20code%20In%20this%20video,bar%20code%20format%20image%20for%20the%20entered%20Text.
    Accessed: March 25,2023
     */
    //===START===
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
            imageProfile.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
    //===FINISH===

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);


        db = FirebaseDatabase.getInstance().getReference();

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        editTextQuantity = (EditText) findViewById(R.id.editTextQuantity);
        editTextBrand = (EditText) findViewById(R.id.editTextBrand);
        editTextCategory = (EditText) findViewById(R.id.editTextCategory);
        editTextBarcode = (EditText) findViewById(R.id.editTextBarcode);
        editTextPrice = (EditText) findViewById(R.id.editTextPrice);
        editTextNotes = (EditText) findViewById(R.id.editTextNote1);
        editTextApproval = (EditText) findViewById(R.id.editTextApproval);
        editTextMinQuantity = (EditText) findViewById(R.id.editTextMinQuantity);
        imageProfile = (ImageView) findViewById(R.id.imageProfile);
        imageProfile.setImageResource(R.drawable.image_placeholder);

        buttonComplete = findViewById(R.id.buttonComplete);
        buttonCamera = findViewById(R.id.buttonCamera);
        buttonGallery = findViewById(R.id.buttonGallery);
        buttonPlus = findViewById(R.id.buttonPlus);
        buttonMinus = findViewById(R.id.buttonMinus);

        buttonBarcode = findViewById(R.id.buttonBarcode);

        editTextApproval.setEnabled(!MainActivity.accountTypeInMain.equals("VOLUNTEER"));

        ID = numOfItemInInventory;


        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

        barcodeActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String response = result.getData().getStringExtra(BarcodeActivity.EXTRA_SCANNED_RESULT);

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

        buttonComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 writeToDatabase();
                 Intent itemIntent = new Intent(AddItemActivity.this, InventoryActivity.class);
                 startActivity(itemIntent);
                 finish();
            }
        });

        progressBar = findViewById(R.id.progressBar);

        Intent intent = getIntent();
        String copyCommand = intent.getStringExtra(COPY_WORD);
        Item copyItem  = (Item) intent.getSerializableExtra(ITEM_WORD);
        if(copyCommand != null){
            copyExistingItem(copyItem);
        } else{
            setVariablesWithTestData();
        }
    }

    int ID;
    String name, description, brand, category,
            barcode, notes, approval;
    String defaultUri, newUri;
    int quantity, minQuantity;
    float price;

    File imageFile;

    private void setVariablesWithTestData() {
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
        setTextFromVariables();
    }

    private void setTextFromVariables() {
        editTextName.setText(name);
        editTextDescription.setText(description);
        editTextQuantity.setText(String.valueOf(quantity));
        editTextMinQuantity.setText(String.valueOf(minQuantity));
        editTextBrand.setText(brand);
        editTextCategory.setText(category);
        editTextBarcode.setText(barcode);
        editTextBrand.setText(brand);
        editTextPrice.setText(String.valueOf(price));
        editTextNotes.setText(notes);
        editTextApproval.setText(approval);

        if (imageFile.exists()) {
            Log.d("Image", "Image Working");
            Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        }
    }

    private void getTextFromFields() {
        name = editTextName.getText().toString();
        description = editTextDescription.getText().toString();
        quantity = Integer.parseInt(editTextQuantity.getText().toString());
        minQuantity = Integer.parseInt(editTextMinQuantity.getText().toString());
        brand = editTextBrand.getText().toString();
        category = editTextCategory.getText().toString();
        barcode = editTextBarcode.getText().toString();
        price = Float.parseFloat(editTextPrice.getText().toString());
        notes = editTextNotes.getText().toString();

        approval = editTextApproval.getText().toString();
    }

    private void copyExistingItem(Item _item) {
        editTextName.setText(_item.getName());
        editTextDescription.setText(_item.getDesc());
        editTextQuantity.setText(String.valueOf(_item.getQuantity()));
        editTextMinQuantity.setText(String.valueOf(_item.getMinQuantity()));
        editTextBrand.setText(_item.getBrand());



        editTextCategory.setText(_item.getCategory());
        editTextBarcode.setText(_item.getBarcode());
        editTextBrand.setText(_item.getBrand());
        editTextPrice.setText(String.valueOf(_item.getPrice()));
        editTextNotes.setText(_item.getNotes());
        editTextApproval.setText(_item.getApproved());

        //Change the variables
        ID = _item.getID();
        getTextFromFields();
    }


    private String choosenPhotoPath;
    private void createCorrespondingTransaction(){
        progressBar.setVisibility(View.VISIBLE);
        Transaction t1 = new Transaction();
        String tid = "Add" + String.valueOf(ID);

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String currentDateToString = dateFormat.format(currentDate);


        t1.setItemId(ID);
        t1.settID(tid);
        t1.setDesc("IN");
        t1.setDate(currentDateToString);
        t1.setQuantity(1);
        progressBar.setVisibility(View.VISIBLE);

        Log.d("TAGt1.toString()", t1.toString());
        db.child(TRANSACTION_WORD).child(tid).setValue(t1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);

                    Log.d("TAGTransactionAdd", "Transaction WORKING ");

                } else {
                    progressBar.setVisibility(View.GONE);
                    Log.d("TAG", task.getException().getMessage());
                }
            }
        });
    }
    private void writeToDatabase() {
        getTextFromFields();
        ID = numOfItemInInventory++;
        Item item2 = new Item(ID, name, description, category, quantity,
                minQuantity, brand, barcode, choosenPhotoPath, notes, price, approval);

        
        db.child(INVENTORY_WORD).child(String.valueOf(ID)).setValue(item2).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    createCorrespondingTransaction();
                    progressBar.setVisibility(View.GONE);
                    Void snapshot = task.getResult();
                    Toast.makeText(AddItemActivity.this, "Adding Item Successful", Toast.LENGTH_SHORT).show();
                    Log.d("TAG", "WORKING LETS CHILL");
                    sendAApprovalRequest();
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(AddItemActivity.this, "Adding Item Failed", Toast.LENGTH_SHORT).show();
                    Log.d("TAG", task.getException().getMessage());
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
        String _imageString = "/storage/self/primary/Pictures/new_image.jpeg";



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
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PermissionsManager.IMAGE_PICK_CODE);
        Toast.makeText(AddItemActivity.this, "Image-picking Successful", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PermissionsManager.CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Glide
                    .with(AddItemActivity.this)
                    .load(currentPhotoPath)
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(imageProfile);
            choosenPhotoPath = currentPhotoPath;
        } else if (requestCode == PermissionsManager.IMAGE_PICK_CODE && resultCode == RESULT_OK && data != null) {
            Glide
                    .with(AddItemActivity.this)
                    .load(data.getData())
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(imageProfile);
            choosenPhotoPath = String.valueOf(data.getData());
        }
    }

    private String approvalRequestMsg;
    private void sendAApprovalRequest(){
        approvalRequestMsg= "A new item has been added, " +
                "please approve the item";
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference rootDf = db.getReference();
        DatabaseReference managersDf = rootDf.child(MANAGER_WORD);
        managersDf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot subDataSnapshot :  snapshot.getChildren()){
                        Account accountRetrieved = subDataSnapshot.getValue(Account.class);
                        sendSMS(accountRetrieved.getPhone(), approvalRequestMsg);
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendSMS( String phoneNumber,  String message) {

        String phoneNumberConcatenated = "+" + phoneNumber;
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.
                    sendTextMessage(
                            phoneNumberConcatenated,
                            null,
                            message,
                            null,
                            null);
            Toast.makeText(this, "SMS sent successfully.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "SMS sending failed.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }



    @Override
    protected void onRestart() {
        super.onRestart();
        editTextApproval.setEnabled(!MainActivity.accountTypeInMain.equals("VOLUNTEER"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        editTextApproval.setEnabled(!MainActivity.accountTypeInMain.equals("VOLUNTEER"));
    }
}