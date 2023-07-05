package com.example.fypproject;

import static com.example.fypproject.globals.Globals.MANAGER_WORD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fypproject.models.Account;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ForgetPasswordActivity extends AppCompatActivity {
    EditText phoneToHelpEt;
    Button requestResetButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        phoneToHelpEt = findViewById(R.id.phone_to_help_et);
        requestResetButton = findViewById(R.id.request_reset_button);

        requestResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSMS();
            }
        });
    }

    private void sendSMS() {
        String phoneNumberTemp = "";
        String messageTemp = "Reset pass of" + phoneToHelpEt.getText();

        String phoneNumberConcatenated = "+" + phoneNumberTemp;

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumberConcatenated, null, messageTemp, null, null);
            Toast.makeText(this, "Reset request sent successfully.", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Reset request sending failed.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            finish();
        }
    }
}