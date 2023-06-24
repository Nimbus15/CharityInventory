package com.example.fypproject;

import static com.example.fypproject.globals.Globals.INVENTORY_WORD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.fypproject.models.Item;
import com.example.fypproject.viewholders.ItemViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;


//TODO: Search
//Filter?
//Ordering AZ
//New folder
//Copy
//Qty:4
public class InventoryActivity extends AppCompatActivity {

    private Button addItemButton;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    //offline?
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child(INVENTORY_WORD);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        addItemButton = (Button) findViewById(R.id.add_item);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addItemIntent = new Intent(InventoryActivity.this, AddItemActivity.class);
                startActivity(addItemIntent);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        //readData();
        //TODO: CONVERT THIS INTO VIEWING INSTEAD OF DELETION
        FirebaseRecyclerOptions<Item> options =
                new FirebaseRecyclerOptions.Builder<Item>()
                        .setQuery(myRef, Item.class)//null?
                        .build();
        FirebaseRecyclerAdapter<Item, ItemViewHolder> adapter = new FirebaseRecyclerAdapter<Item, ItemViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull Item model) {
                holder.itemIcon.setImageURI(Uri.parse(model.getImage()));
                holder.textViewItemName.setText(model.getName());
                Log.d("tagonBindViewHolder", "onBindViewHolder: 0");
                holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final int itemID = model.getID();//TODO: I Made this static

                        CharSequence[] options = new CharSequence[]{
                                "Yes",
                                "No"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(InventoryActivity.this);
                        builder.setTitle("Do you want to delete this product. Are you sure?");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which == 0){
                                    deleteItem(itemID);
                                    //deleteTransactionAssociated(itemID);
                                    Log.d("TAGonClick0", "onClick: 0");
                                }
                                if(which == 1){
                                    Log.d("TAGonClick1", "onClick: 1");
                                }
                            }
                        });
                        builder.show();
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(InventoryActivity.this,ViewItemActivity.class);
                        intent.putExtra(ViewItemActivity.EXTRA_ITEM,model);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
                ItemViewHolder holder = new ItemViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void deleteItem(int itemID) {
        myRef.child(String.valueOf(itemID))
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(InventoryActivity.this, "This item has been deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private ArrayList<Item> retrievedItems;
    String idTemp;
    Item itemCaptured;
    //
    private void readData(){

        retrievedItems = new ArrayList<Item>();

        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {//Can use query instead
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