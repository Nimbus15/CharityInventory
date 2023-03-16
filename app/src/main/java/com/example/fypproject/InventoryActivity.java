package com.example.fypproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



//TODO: Search
// Filter?
// Ordering AZ
// New folder
//Copy
// Qty:4
public class InventoryActivity extends AppCompatActivity {

    private Button addItemButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        addItemButton = (Button) findViewById(R.id.add_item);

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addItemIntent = new Intent(InventoryActivity.this, AddItemActivity.class);
                startActivity(addItemIntent);
            }
        });
    }


}