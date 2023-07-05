package com.example.fypproject;

import static com.example.fypproject.globals.Globals.TRANSACTION_WORD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.fypproject.models.Transaction;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class TransactionActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextInputLayout til;
    private TextInputEditText tilEt;
    private RadioGroup filterRg;
    private RadioButton allRb, inRb, outRb;
    private Spinner searchBySpinner;
    private TransactionListAdapter transactionListAdapter;

    private List<Transaction> transactionList;

    static String currentDate = "27/04/23";
    public class TransactionData {

        public static List<Transaction> getAllTransactions() {
            List<Transaction> transactionData = new ArrayList<>();
            transactionData.add(new Transaction(1,  "First Item", "IN", currentDate,1));
            transactionData.add(new Transaction(2, "Second Item", "OUT", currentDate,1));
            transactionData.add(new Transaction(3,  "Third Item", "IN", currentDate,1));
            transactionData.add(new Transaction(4,  "Fourth Item", "OUT", currentDate,1));
            return transactionData;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        //Initialize Views
        recyclerView = findViewById(R.id.recyclerView);
        til = findViewById(R.id.til);
        tilEt = findViewById(R.id.tilEt);
        filterRg = findViewById(R.id.filterRg);
        allRb = findViewById(R.id.allRb);
        inRb = findViewById(R.id.inRb);
        outRb = findViewById(R.id.outRb);
        searchBySpinner = findViewById(R.id.searchBySpinner);

        //Initialize List--------------(FIREBASE OUTPUT LIST)--------
        // ------------
        //transactionList = new ArrayList<>();
        transactionList = TransactionData.getAllTransactions(); //Firebase output data for all items
        retrievedFromDatabase();
        //Initialize List--------------(FIREBASE OUTPUT LIST)--------------------

        //Initialize List Adapter
        transactionListAdapter = new TransactionListAdapter();

        //Set adapter to recycler view
        recyclerView.setAdapter(transactionListAdapter);

        //Set data for adapter
        transactionListAdapter.setList(transactionList);


        //Filter Data and Search based on action or name
        setupFilterAndSearch();
    }

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference transactionRef = database.getReference().child(TRANSACTION_WORD);
    private void retrievedFromDatabase(){
        transactionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                transactionList.clear();
                for(DataSnapshot newDataSnapshot : snapshot.getChildren()){
                    Transaction transaction = newDataSnapshot.getValue(Transaction.class);
                    transactionList.add(transaction);
                }
                transactionListAdapter.setList(transactionList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setupFilterAndSearch() {
        List<Transaction> currentList = new ArrayList<>(transactionList);
        filterRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int desc) {
                clearSearchField();
                filterListBasedOnAction(desc, currentList);
            }
        });
        searchBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                clearSearchField();
                if (position == 0) {
                    til.setHint("Search By Desc");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        tilEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null && editable.length() > 0) {
                    if (searchBySpinner.getSelectedItemPosition() == 0) {
                        List<Transaction> newList = currentList.stream().filter(item -> item.getDesc().toUpperCase(Locale.getDefault()).contains(editable.toString().toUpperCase(Locale.getDefault()))).collect(Collectors.toList());
                        transactionListAdapter.setList(newList);
                    } else {
                        List<Transaction> newList = currentList.stream().filter(item -> item.getDesc().toUpperCase(Locale.getDefault()).contains(editable.toString().toUpperCase(Locale.getDefault()))).collect(Collectors.toList());
                        transactionListAdapter.setList(newList);
                    }
                } else {
                    filterListBasedOnAction(filterRg.getCheckedRadioButtonId(), currentList);
                }
            }
        });

    }

    private void filterListBasedOnAction(int id, List<Transaction> currentList) {
        if (id == R.id.allRb) {
            transactionListAdapter.setList(transactionList);
        } else if (id == R.id.inRb) {
            currentList.clear();
            List<Transaction> newList = transactionList.stream().filter(transaction -> transaction.getDesc().equals("IN")).collect(Collectors.toList());
            currentList.addAll(newList);
            transactionListAdapter.setList(currentList);
        } else if (id == R.id.outRb) {
            currentList.clear();
            List<Transaction> newList = transactionList.stream().filter(transaction -> transaction.getDesc().equals("OUT")).collect(Collectors.toList());
            currentList.addAll(newList);
            transactionListAdapter.setList(currentList);
        }
    }

    private void clearSearchField(){
        tilEt.setText("");
    }

    public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.ViewHolder> {
        protected List<Transaction> tList;


        @SuppressLint("NotifyDataSetChanged")
        public void setList(List<Transaction> newList) {
            tList = new ArrayList<>(newList);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_row, parent, false);
            return new ViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.bind(tList.get(position));
        }

        @Override
        public int getItemCount() {
            if (tList == null) return 0;
            return tList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            protected TextView tidTv, itemIdTv, descTv, dateTv, quantityTv;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tidTv = itemView.findViewById(R.id.tidTv);
                itemIdTv = itemView.findViewById(R.id.itemIdTv);
                descTv = itemView.findViewById(R.id.descTv);
                dateTv = itemView.findViewById(R.id.dateTv);
                quantityTv = itemView.findViewById(R.id.quantityTv);
            }

            public void bind(Transaction transaction) {
                tidTv.setText("TID: " + transaction.gettID());
                itemIdTv.setText("Related Item: " + transaction.getItemId());
                descTv.setText("Description of Action: " +transaction.getDesc());
                dateTv.setText("Date of Transaction: " + transaction.getDate());
                quantityTv.setText("Quantity: " + transaction.getQuantity());
            }
        }
    }

}

