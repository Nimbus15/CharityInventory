package com.example.fypproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.fypproject.models.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


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
        readData();
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addItemIntent = new Intent(InventoryActivity.this, AddItemActivity.class);
                startActivity(addItemIntent);
            }
        });
    }

    private ArrayList<Item> retrievedItems;
    private Item retrievedItem;
    String idTemp;
    Item itemCaptured;
    private void readData(){

        //offline
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("inventory");//
        ArrayList<String> inventoryIdList = new ArrayList();

        retrievedItems = new ArrayList<Item>();

        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if(task.getResult().exists()){
                        Toast.makeText(InventoryActivity.this, "Successfully Read", Toast.LENGTH_SHORT).show();
                        DataSnapshot dataSnapshot = task.getResult();
                        Log.d("TAGdataSnapshot", "onComplete: dataSnapshot " + dataSnapshot.getValue());

                        for(DataSnapshot itemSnapshot: dataSnapshot.getChildren()){
                            idTemp = String.valueOf(itemSnapshot.getKey());
                             Log.d("TAG idTemp", String.valueOf(idTemp));
                            itemCaptured = itemSnapshot.getValue(Item.class);
                            Log.d("TAG itemCaptured", String.valueOf(itemCaptured));
                            retrievedItems.add(itemCaptured);
                        }
                        for(Item r : retrievedItems){
                            Log.d("TAG retrievedItems Now", r.toString());
                        }
                    }else{
                        Toast.makeText(InventoryActivity.this, "Item Doesn't Exist", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(InventoryActivity.this, "Failed To Read", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}