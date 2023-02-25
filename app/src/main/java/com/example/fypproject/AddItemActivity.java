package com.example.fypproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.fypproject.models.Item;

public class AddItemActivity extends AppCompatActivity {

    private ImageView _profileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        _profileImage = (ImageView) findViewById(R.id.profileImage);

        _profileImage.setImageResource(R.drawable.image_placeholder);
    }

    private void inputItemDetails(){
        Item item = new Item();
    }
}