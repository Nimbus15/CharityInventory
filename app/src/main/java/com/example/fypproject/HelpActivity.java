package com.example.fypproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {

    private TextView inventory_help_tv, reports_help_tv, transactions_help_tv;
    private String inventoryHelpText, reportsHelpText, transactionsHelpText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        inventory_help_tv = findViewById(R.id.inventory_help_tv);
        reports_help_tv = findViewById(R.id.reports_help_tv);
        transactions_help_tv = findViewById(R.id.transaction_help_tv);

        inventoryHelpText="" +
                "The inventory page is where you can see a " +
                "list of all the items in the charity shop inventory";
        reportsHelpText="" +
                "The report page provides a breakdown of key statistic in your account";

        transactionsHelpText = ""+
                "The transaction page provides a list of actions taken on an item";

        inventory_help_tv.setText(inventoryHelpText);
        reports_help_tv.setText(reportsHelpText);
        transactions_help_tv.setText(transactionsHelpText);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}