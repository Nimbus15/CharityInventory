package com.example.fypproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ReportsActivity extends AppCompatActivity {

    public static int numItemsInInventory;
    private TextView inventory_title_tv, qty_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        inventory_title_tv = (TextView) findViewById(R.id.inventory_title_tv);
        qty_tv = (TextView) findViewById(R.id.qty_tv);

        qty_tv.setText(String.valueOf("Qty: " +numItemsInInventory));
    }
}