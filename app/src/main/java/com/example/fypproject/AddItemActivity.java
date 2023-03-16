package com.example.fypproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fypproject.models.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AddItemActivity extends AppCompatActivity {

    private DatabaseReference db;
    private EditText editTextName, editTextDescription, editTextQuantity, editTextBrand,
    editTextCategory, editTextBarcode, editTextPrice, editTextNote, editTextMinQuantity, editTextApproval;
    private ImageView imageProfile;
    private Button buttonComplete;

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        db = FirebaseDatabase.getInstance().getReference();

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextDescription= (EditText) findViewById(R.id.editTextDescription);
        editTextQuantity = (EditText) findViewById(R.id.editTextQuantity);
        editTextBrand = (EditText) findViewById(R.id.editTextBrand);
        editTextCategory = (EditText) findViewById(R.id.editTextCategory);
        editTextBarcode = (EditText) findViewById(R.id.editTextBarcode);
        editTextPrice = (EditText) findViewById(R.id.editTextPrice);
        editTextNote = (EditText) findViewById(R.id.editTextNote1);
        editTextApproval =  (EditText) findViewById(R.id.editTextApproval);
        editTextMinQuantity = (EditText) findViewById(R.id.editTextMinQuantity);
        imageProfile = (ImageView) findViewById(R.id.imageProfile);
        imageProfile.setImageResource(R.drawable.image_placeholder);

        buttonComplete = findViewById(R.id.buttonComplete);
        buttonComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent itemIntent = new Intent(AddItemActivity.this, InventoryActivity.class);
//                startActivity(itemIntent);
                inputItemDetails();
                checkPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
                checkPermission(android.Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
//                if(ContextCompat.checkSelfPermission(thisActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        != PackageManager.PERMISSION_GRANTED)
//                {
//                    // Permission is not granted
//                }
            }
        });
    }

    int ID;
    String name, description, brand, category,
            barcode, notes, approval;
    String defaultUri ,newUri;
    int quantity, minQuantity;
    float price;

    private void initialisation(){
        //initialisation with default - declare here
        ID = 0;
        name = "null";
        description ="null";
        quantity = 0;
        minQuantity =0;
        brand = "brand";
        barcode ="00000";
        price = 0.0f;
        notes = "null";
        approval = "null";

        //just use drawable
        defaultUri = "@drawable/image_placeholder.jpeg";
        int defaultImageResource = R.drawable.image_placeholder;
        Drawable defaultImage = getResources().getDrawable(defaultImageResource);
        imageProfile.setImageDrawable(defaultImage);
    }

    private void inputItemDetails() {


        Item item = new Item(ID, name, description, category, quantity,
                minQuantity, brand, barcode, defaultUri, notes, price, approval);
        System.out.println(item.toString());

        //test
        ID = 112;
        editTextName.setText("testname");
        editTextDescription.setText("testDesc");
        editTextQuantity.setText("111");
        editTextMinQuantity.setText("11");
        editTextBrand.setText("test");
        editTextCategory.setText("testcategory");
        editTextBarcode.setText("aaaaaaa");
        editTextPrice.setText("10.00");
        editTextNote.setText("testnotes");
        editTextApproval.setText("no");


        String name = editTextName.getText().toString();
        String description = editTextDescription.getText().toString();
        int quantity = Integer.parseInt(editTextQuantity.getText().toString());
        int minQuantity = Integer.parseInt(editTextMinQuantity.getText().toString());
        String brand = editTextBrand.getText().toString();
        String category = editTextCategory.getText().toString();
        String barcode = editTextBarcode.getText().toString();
        float price = Float.parseFloat(editTextPrice.getText().toString());
        String notes = editTextNote.getText().toString();
        String approval = editTextApproval.getText().toString();

//        String newUri="";
//        int newImageResource = 1;
//        Drawable newImage = getResources().getDrawable(newImageResource);
//        imageProfile.setImageDrawable(newImage);

        Item item2 = new Item(ID, name, description, category, quantity,
                minQuantity, brand, barcode, defaultUri, notes, price, approval);

        System.out.println(item2.toString());

        //add to database
        db.child("inventory").child(String.valueOf(ID)).setValue(item2).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Void snapshot = task.getResult();
                    //Log.d("TAG", "creator: " + creator);
                    Log.d("TAG","WORKING LETS CHILL");
                } else {
                    Log.d("TAG", task.getException().getMessage()); //Don't ignore potential errors!
                }
            }
        });

        // create transaction


        //Get real data
//        name = editTextName.getText().toString();
//        description = editTextDescription.getText().toString();
//        quantity = 0;//Integer.parseInt(editTextQuantity.getText().toString());
//        minQuantity = Integer.parseInt(editTextMinQuantity.getText().toString());
//        brand = editTextBrand.getText().toString();
//        category = editTextCategory.getText().toString();
//        barcode = editTextBarcode.getText().toString();
//        price = 0.0f; //Float.parseFloat(editTextPrice.getText().toString());
//        note1 = editTextNote1.getText().toString();
//        approval = editTextApproval.getText().toString();

//        String newUri="";
//        int newImageResource = 1;
//        Drawable newImage = getResources().getDrawable(newImageResource);
//        imageProfile.setImageDrawable(newImage);
        //From https://stackoverflow.com/questions/41396194/how-to-convert-image-to-string-in-android





        //"/storage/self/primary/Download/placeholder.jpeg"




        //else we have an defaultImage path use that
        //ask user for permission
        //String fileName = "image_placeholderDF.jpeg";
        // Get the path of the defaultImage
        //File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
        //String path = file.getAbsolutePath();
        //       imageProfile.setImageURI(Uri.parse(path));
        //Picasso.get().load(path).into(imageProfile);


        //Item item = new Item(1, name, description, category, quantity, 0, brand, barcode, null, note1, price );

        /* use git */


        /*add transaction
        Database
        Images a)- permissions
              b)- how to store
        output*/

        /*
            Complex
            Generate Graphs?
            Messaging
            Look at cookbook



         */



        //generateTransaction() call activity

        //use a provider?

        //Intent transactionIntent = new Intent(AddItemActivity.this, TransactionActivity.class);
        //startActivityForResult(transactionIntent, 2);// Activity is started with requestCode 2
        //https://www.geeksforgeeks.org/android-how-to-request-permissions-in-android-application/
        //https://stackoverflow.com/questions/41066484/call-function-from-another-activity-class-in-android-studio
        //https://www.youtube.com/watch?v=HgYIhNZ2kaw
    }

    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(AddItemActivity.this, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(AddItemActivity.this, new String[] { permission }, requestCode);
        }
        else {
            Toast.makeText(AddItemActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Showing the toast message
                Toast.makeText(AddItemActivity.this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(AddItemActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(AddItemActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(AddItemActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}