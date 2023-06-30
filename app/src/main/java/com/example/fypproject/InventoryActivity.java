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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fypproject.globals.Globals;
import com.example.fypproject.models.Item;
import com.example.fypproject.viewholders.ItemViewHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class InventoryActivity extends AppCompatActivity {
    public static final String COPY_WORD= "Copy_It";
    public static final String ITEM_WORD= "Copy_Item";

    private Button addItemButton, filterButton, executeButton, sortButton, copyButton;
    private TextView numItemTextView;
    private EditText filterBar;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child(INVENTORY_WORD);

    public static int numOfItemInInventory =0;
    //stringFormOfNumItems
    List<Item> dataListCopy = new ArrayList<>();
    InventoryActivity.InventoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        addItemButton = (Button) findViewById(R.id.add_item);
        filterButton = (Button) findViewById(R.id.filter_button);
        sortButton = (Button) findViewById(R.id.sort_button);
        copyButton = (Button) findViewById(R.id.copy_button);

        filterBar = (EditText) findViewById(R.id.filter_bar);
        executeButton= (Button) findViewById(R.id.execute_button);

        numItemTextView = (TextView) findViewById(R.id.num_item_tv);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if(dataListCopy != null && dataListCopy.size() > 0)
            selectedItemDetailsToCopy = dataListCopy.get(0);

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addItemIntent = new Intent(InventoryActivity.this, AddItemActivity.class);
                startActivity(addItemIntent);
            }
        });


        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterBar.setVisibility(View.VISIBLE);
                executeButton.setVisibility(View.VISIBLE);
            }
        });

        executeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(AddItemActivity.this, "Adding Item Successful", Toast.LENGTH_SHORT).show();
                String filterTerm = String.valueOf(filterBar.getText());
                if(!filterTerm.equals("")){
                    filterItemsBasedOnName(filterTerm);
                }
            }
        });

        filterBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable != null && editable.length() > 0) {

                }else{
                    adapter.setItemList(dataListCopy);
                }
            }
        });

        adapter = new InventoryAdapter();
        recyclerView.setAdapter(adapter);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataListCopy.clear();
                for(DataSnapshot newDataSnapshot : snapshot.getChildren()){
                    Item item = newDataSnapshot.getValue(Item.class);
                    dataListCopy.add(item);
                }
                adapter.setItemList(dataListCopy);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortItemsBasedOnName();
            }
        });

        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedItemDetailsToCopy == null && dataListCopy.size() >0)
                    selectedItemDetailsToCopy = dataListCopy.get(0);
                Intent copyIntent = new Intent(InventoryActivity.this, AddItemActivity.class);

                copyIntent.putExtra(COPY_WORD, "COPY");
                copyIntent.putExtra(ITEM_WORD,  selectedItemDetailsToCopy);
                startActivity(copyIntent);
            }
        });

        numItemTextView.setText("Qty: " + String.valueOf(adapter.getItemCount()));
        numOfItemInInventory = adapter.getItemCount();

    }


    private List<Item> filteredList = new ArrayList<>();

    private void filterItemsBasedOnName(String name){
        List<Item> filteredList = dataListCopy.stream().filter(item -> item.getName().toUpperCase(Locale.ENGLISH).equals(name.toUpperCase(Locale.ENGLISH))).collect(Collectors.toList());
        adapter.setItemList(filteredList);
        Log.d("filterItemsBasedOnName", "filterItemsBasedOnName called: ");
    }

    boolean sortFromAtoZ = true;
    private void sortItemsBasedOnName(){
        List<Item> sortedList = new ArrayList<>();
        sortedList.addAll(dataListCopy);
        sortFromAtoZ = !sortFromAtoZ;
        //sortedList.sort();
        if(sortFromAtoZ)
            Collections.sort(sortedList, highComparator);
        else
            Collections.sort(sortedList, lowComparator);
        adapter.setItemList(sortedList);
    }
    Comparator<Item> highComparator = new Comparator<Item>() {
        @Override
        public int compare(Item item1, Item item2) {
            return item1.getName().compareToIgnoreCase(item2.getName());
        }
    };

    Comparator<Item> lowComparator = new Comparator<Item>() {
        @Override
        public int compare(Item item1, Item item2) {
            return item2.getName().compareToIgnoreCase(item1.getName());
        }
    };
    @Override
    protected void onRestart() {
        super.onRestart();
    }


    @Override
    protected void onStart() {
        super.onStart();

//        FirebaseRecyclerOptions<Item> options =
//                new FirebaseRecyclerOptions.Builder<Item>()
//                        .setQuery(myRef, Item.class)
//                        .build();

    }

    private void deleteItem(int itemID) {
        myRef.child(String.valueOf(itemID))
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(InventoryActivity.this, "Item deleted successfully!!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onPause() {
        filterBar.setVisibility(View.INVISIBLE);
        executeButton.setVisibility(View.INVISIBLE);
        super.onPause();
    }

    Item selectedItemDetailsToCopy;
    class InventoryAdapter extends RecyclerView.Adapter<ItemViewHolder> {
        private List<Item> itemList;
        public void setItemList(List<Item> itemList) {
            this.itemList = itemList;
            notifyDataSetChanged();
            numOfItemInInventory = getItemCount();
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
            ItemViewHolder holder = new ItemViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            Item model = itemList.get(position);
            if(itemList.get(holder.getAbsoluteAdapterPosition()).getImage() != null
            ){
                holder.itemIcon.setImageURI(Uri.parse(itemList.get(holder.getAbsoluteAdapterPosition()).getImage()));
            }
            holder.textViewItemName.setText(itemList.get(holder.getAbsoluteAdapterPosition()).getName());
            Log.d("tagonBindViewHolder", "onBindViewHolder: 0");
            holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int itemID = itemList.get(holder.getAbsoluteAdapterPosition()).getID();
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
                    selectedItemDetailsToCopy = itemList.get(holder.getAbsoluteAdapterPosition());
                    Intent intent = new Intent(InventoryActivity.this,ViewItemActivity.class);
                    intent.putExtra(ViewItemActivity.EXTRA_ITEM, itemList.get(holder.getAbsoluteAdapterPosition()));
                    startActivity(intent);
                }
            });
            numItemTextView.setText("Qty: " + getItemCount());

        }

        @Override
        public int getItemCount() {
            if(itemList == null)
                return 0;
            return itemList.size();
        }
    }

}

