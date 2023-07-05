package com.example.fypproject;

import static com.example.fypproject.globals.Globals.ACCOUNT_TYPE_WORD;
import static com.example.fypproject.globals.Globals.INVENTORY_WORD;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fypproject.managers.PermissionsManager;
import com.example.fypproject.models.Item;
import com.example.fypproject.models.Prevalent;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


//TODO: LIST HERE
//More Complexity?
//Connect All Activity
//Move expenses to Report
//

//TODO: Low-level

//TODO: Other Subject
//EA-mark, ES-lab + test soon, AI-lab, GE-LAB,

public class MainActivity extends PermissionsManager implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private Button inventoryBtn, transactionsBtn, reportsBtn,
            helpBtn, settingsBtn, quickBtn;
    private Button backupBtn;

    private String typeOfAccount;
    public static String accountTypeInMain;
    private TextView nameTextView;
    private ImageView userImageView;
    private ActivityResultLauncher<Intent> barcodeActivityLauncher;
    //private int numItemsInInventory=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = getIntent();
        typeOfAccount = intent.getStringExtra(ACCOUNT_TYPE_WORD);
        accountTypeInMain = typeOfAccount;

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inventory_displayer, R.id.nav_transaction_displayer, R.id.nav_report_displayer)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);//problem




        checkAllPermissions();
        inventoryBtn = findViewById(R.id.inventory_button);//change names?
        transactionsBtn = findViewById(R.id.transactions_button);
        reportsBtn = findViewById(R.id.reports_button);
        helpBtn = findViewById(R.id.help_button);
        settingsBtn = findViewById(R.id.settings_button);
        quickBtn = findViewById(R.id.quick_button);


        View headerView = navigationView.getHeaderView(0);
        nameTextView= headerView.findViewById(R.id.usernameTextView);
        userImageView = headerView.findViewById(R.id.userImageView);

        foundItem=null;
//        AddItemActivity.checkAllPermissions();

        //backupBtn = findViewById(R.id.backupBtn);

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

        helpBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent helpIntent = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(helpIntent);
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
            }
        });

        quickBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent barcodeIntent = new Intent(MainActivity.this, BarcodeActivity.class);
                barcodeActivityLauncher.launch(barcodeIntent);
            }
        });

        barcodeActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                    String barcodeResponse = result.getData().getStringExtra(BarcodeActivity.EXTRA_SCANNED_RESULT);
                    Log.d("TagBarcodeReponse", barcodeResponse);
                    searchItemUsingBarcode(barcodeResponse);
                    if(foundItem!= null){
                        Log.d("ifstatementcalledtag", "onActivityResult - ifstatementcalled");
                        Intent viewIntent = new Intent(MainActivity.this, ViewItemActivity.class);
                        viewIntent.putExtra(ViewItemActivity.EXTRA_ITEM, foundItem);
                        startActivity(viewIntent);
                    }
                }
            }
        });


//        backupBtn.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View view) {
//               populateFirestoreWithRealtimeDatabase();
//           }
//       });

        reportsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reportsIntent = new Intent(MainActivity.this, ReportsActivity.class);
                startActivity(reportsIntent);
            }
        });
        if(typeOfAccount.equals("VOLUNTEER") || typeOfAccount.equals("MANAGER")){
            nameTextView.setText(Prevalent.currentOnlineUser.getName());
            Picasso.get()
                    .load(R.drawable.ic_user_image)
                    .resize(400, 400)
                    .placeholder(R.drawable.ic_user_image)
                    .into(userImageView);
        }
    }

    Item foundItem;
    private void searchItemUsingBarcode(String barcode){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child(INVENTORY_WORD);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot newDataSnapshot : snapshot.getChildren()) {
                    Item item = newDataSnapshot.getValue(Item.class);//here
                    Log.d("StartHeregetBarcode()Tag", item.getBarcode());
                    if(item.getBarcode().equals(barcode)){
                        foundItem = new Item();
                        foundItem.setID(item.getID());
                        foundItem.setName(item.getName());
                        foundItem.setDesc(item.getDesc());
                        foundItem.setCategory(item.getCategory());
                        foundItem.setQuantity(item.getQuantity());
                        foundItem.setMinQuantity(item.getMinQuantity());

                        foundItem.setBrand(item.getBrand());
                        foundItem.setBarcode(item.getBarcode());
                        foundItem.setImage(item.getImage());
                        foundItem.setNotes(item.getNotes());
                        foundItem.setPrice(item.getPrice());
                        foundItem.setApproved(item.getApproved());


                        Log.d("FoundItemTag", foundItem.toString());
                        return;
                    }
                }
                Toast.makeText(MainActivity.this, "Item with barcode not found", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Barcode search cancelled", Toast.LENGTH_SHORT).show();
            }

        });
    }

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(INVENTORY_WORD);
    ArrayList<Item> charityInventory = new ArrayList<Item>();
    private void populateFirestoreWithRealtimeDatabase(){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //numItemsInInventory = (int) dataSnapshot.getChildrenCount();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Item retrievedItem = userSnapshot.getValue(Item.class);

                    if (retrievedItem != null) {
                        charityInventory.add(retrievedItem);
                    }
                }
                writeToFirestore();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.action_settings){
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            //Toast.makeText(this, "THIS in mainactivity setting INCOMPLETE", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Log.d("TAGitem.getItemId()", String.valueOf(item.getItemId()));

        if(id == R.id.nav_inventory_displayer){
            Toast.makeText(this, "THIS in mainactivity inventory should have worked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, InventoryActivity.class);
            startActivity(intent);
        }else if(id == R.id.nav_transaction_displayer){
            Intent intent = new Intent(MainActivity.this, TransactionActivity.class);
            startActivity(intent);
        }else if(id == R.id.nav_report_displayer){
//            Intent intent = new Intent(MainActivity.this, ReportActivity.class);
//            startActivity(intent);
            Toast.makeText(this, "THIS in mainactivity report INCOMPLETE", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}