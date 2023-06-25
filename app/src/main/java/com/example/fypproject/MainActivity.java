package com.example.fypproject;

import static com.example.fypproject.globals.Globals.INVENTORY_WORD;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Button;

import com.example.fypproject.managers.PermissionsManager;
import com.example.fypproject.models.Item;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.fypproject.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


//TODO: LIST HERE
//More Complexity?
//Connect All Activity
//Move expenses to Report
//

//TODO: Low-level

//TODO: Other Subject
//EA-mark, ES-lab + test soon, AI-lab, GE-LAB,

public class MainActivity extends PermissionsManager {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private Button inventoryBtn, transactionsBtn, reportsBtn, expensesBtn;
    private Button registerUserBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);//problem


        checkAllPermissions();
        inventoryBtn = findViewById(R.id.inventory_button);// change name
        transactionsBtn = findViewById(R.id.transactions_button);// change name

//        startActivityForResult(AddItemActivity);
//        AddItemActivity.checkAllPermissions();

        registerUserBtn = findViewById(R.id.registerUserBtn);

        inventoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent itemIntent = new Intent(MainActivity.this, InventoryActivity.class);
                startActivity(itemIntent);
            }
        });

        transactionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent transactionsIntent = new Intent(MainActivity.this, TransactionActivity.class);
                startActivity(transactionsIntent);
            }
        });




        registerUserBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
//               Intent registerUserIntent = new Intent(MainActivity.this, RegisterActivity.class);
//               startActivity(registerUserIntent);
               populateFirestoreWithRealtimeDatabase();
           }
       });

//        reportsBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent reportsIntent = new Intent(MainActivity.this, TransactionActivity.class);
//                startActivity(reportsIntent);
//            }
//        });
    }

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(INVENTORY_WORD);
    ArrayList<Item> charityInventory = new ArrayList<Item>();
    private void populateFirestoreWithRealtimeDatabase(){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Retrieve the data from the dataSnapshot
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Item retrievedItem = userSnapshot.getValue(Item.class);

                    // Process the retrieved data
                    if (retrievedItem != null) {
                        // Perform operations with the retrieved values
                        // Example: Create a user object and add it to a list

                        charityInventory.add(retrievedItem);
                    }
                }
                // Write the data to Cloud Firestore
                writeToFirestore();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors that occur during data retrieval
            }
        });
    }

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    CollectionReference collectionReference = firestore.collection(INVENTORY_WORD);
    private void writeToFirestore() {
        for (Item item : charityInventory) {
            DocumentReference documentReference = collectionReference.document(); // Auto-generated document ID
            documentReference.set(item)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Data has been successfully written to Firestore
                            Log.d("Firestore", "Data written successfully");
                            //return false;
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle any errors that occur during data writing
                            Log.e("Firestore", "Error writing data", e);
                        }
                    });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}