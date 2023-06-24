package com.example.fypproject;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fypproject.models.Item;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class ViewItemActivity extends AppCompatActivity {
    public static final String EXTRA_ITEM = "extra_item";
    //TODO: ALLOW NEED EDIT
    //TODO: ADD CATEGORY?

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
        setContentView(R.layout.activity_view_item);

        db = FirebaseDatabase.getInstance().getReference();
        
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

        Item item = (Item) getIntent().getSerializableExtra(EXTRA_ITEM);
        Toast.makeText(this, item.toString(), Toast.LENGTH_SHORT).show();
        
        populateFromItem(item);

        buttonComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Snackbar.make(view ,"buttonComplete was clicked", Snackbar.LENGTH_SHORT).show();
            }
        });
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
        editTextNote.setText(_item.getNotes());
        editTextApproval.setText(_item.getApproved());
    }
}