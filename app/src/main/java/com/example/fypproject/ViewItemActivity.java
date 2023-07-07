package com.example.fypproject;

import static com.example.fypproject.globals.Globals.INVENTORY_WORD;
import static com.example.fypproject.globals.Globals.TRANSACTION_WORD;
import static com.example.fypproject.globals.Globals.VOLUNTEER_WORD;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fypproject.models.Item;
import com.example.fypproject.models.Transaction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ViewItemActivity extends AppCompatActivity {
    public static final String EXTRA_ITEM = "extra_item";
    //TODO: ALLOW NEED EDIT
    //TODO: ADD CATEGORY?

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

    public void decodeABarcode() {
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

    Item item;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);

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
        progressBar = findViewById(R.id.progressBar2);

        item = (Item) getIntent().getSerializableExtra(EXTRA_ITEM);
        Toast.makeText(this, item.toString(), Toast.LENGTH_SHORT).show();
        
        populateFromItem(item);

        buttonComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDetailsInDatabase();
                Snackbar.make(view ,"buttonComplete was clicked", Snackbar.LENGTH_SHORT).show();
            }
        });

        editTextApproval.setEnabled(!MainActivity.accountTypeInMain.equals(VOLUNTEER_WORD));//"VOLUNTEER"
    }

    private void populateFromItem(Item _item) {
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
    }

    int ID;
    String name, description, brand, category,
            barcode, notes, approval;
    String defaultUri, newUri;
    int quantity, minQuantity;
    float price;

    private void getTextFromField(){
        ID = item.getID();

        approval = editTextApproval.getText().toString();
        barcode = editTextBarcode.getText().toString();
        brand = editTextBrand.getText().toString();
        category = editTextCategory.getText().toString();
        description = editTextDescription.getText().toString();



        minQuantity = Integer.parseInt(editTextMinQuantity.getText().toString());
        name = editTextName.getText().toString();
        notes = editTextNotes.getText().toString();
        price = Float.parseFloat(editTextPrice.getText().toString());
        quantity = Integer.parseInt(String.valueOf(editTextQuantity.getText()));
    }

    private void addAnChangedTransactionInDatabase() {
        Transaction t1 = new Transaction();
        String tid = "Updated" + String.valueOf(ID);

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

                    Log.d("TAGTransactionUpdate", "Transaction WORKING ");

                } else {
                    progressBar.setVisibility(View.GONE);
                    Log.d("TAG", task.getException().getMessage());
                }
            }
        });

    }

    private void updateDetailsInDatabase() {
        getTextFromField();
        // Obtain a reference to the location of the record you want to update
        DatabaseReference databaseRef =
                FirebaseDatabase.getInstance().getReference(INVENTORY_WORD).child(String.valueOf(ID));

        // Create a map to hold the updated data
        Map<String, Object> changes = new HashMap<>();
        changes.put("approved", approval);
        changes.put("barcode",  barcode);
        changes.put("brand", brand);
        changes.put("category", category);
        changes.put("desc", description);


        changes.put("minQuantity", minQuantity);
        changes.put("name", name);
        changes.put("notes",notes);
        changes.put("price", price);
        changes.put("quantity", quantity);

        databaseRef.updateChildren(changes)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    addAnChangedTransactionInDatabase();
                    Toast.makeText(getApplicationContext(), "Data updated successfully", Toast.LENGTH_SHORT).show();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // An error occurred while updating data
                    Toast.makeText(getApplicationContext(), "Data update failed", Toast.LENGTH_SHORT).show();
                }
            });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        editTextApproval.setEnabled(!MainActivity.accountTypeInMain.equals(VOLUNTEER_WORD));
    }

    @Override
    protected void onResume() {
        super.onResume();
        editTextApproval.setEnabled(!MainActivity.accountTypeInMain.equals(VOLUNTEER_WORD));
    }
}