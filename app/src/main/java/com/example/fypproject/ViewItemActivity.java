package com.example.fypproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.fypproject.models.Item;

public class ViewItemActivity extends AppCompatActivity {
    public static final String EXTRA_ITEM = "extra_item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);
        Item item = (Item) getIntent().getSerializableExtra(EXTRA_ITEM);
        Toast.makeText(this, item.toString(), Toast.LENGTH_SHORT).show();
    }
}