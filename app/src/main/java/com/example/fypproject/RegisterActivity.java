package com.example.fypproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button buttonCreateAccount;
    private EditText editTextName, editTextPhone, editTextPassword;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        buttonCreateAccount = (Button) findViewById(R.id.buttonRegister);//
        editTextName = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);

        progressDialog = new ProgressDialog(this);

        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });
    }

    private void createAccount() {

        String name = editTextName.getText().toString();
        String phone = editTextPhone.getText().toString();
        String password = editTextPassword.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.setTitle("Create Account");
            progressDialog.setMessage("Please wait, while we are checking the credentials.");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            validatePhoneNumber(name, phone, password);

            //HOW TO CANCEL PROGRESSDIALOG?
            //HOW DOES IT MOVE TO NEXT FUNCTIONS

        }
    }

    //Check phone and then use that phone number to create an account.
    private void validatePhoneNumber(final String name, final String phone, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            //This happens if the data in the field is modified
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //If there is a phone number associated
                if (!(dataSnapshot.child("volunteers").child(phone).exists())) {
                    HashMap<String, Object> accountDatamap = new HashMap<>();
                    accountDatamap.put("phone", phone);
                    accountDatamap.put("password", password);
                    accountDatamap.put("name", name);

                    Log.d("TAGaccountDatamap", "onDataChange: " + accountDatamap);
                    //HERE: TODO: add the phone again

                    //Add to list of volunteers's details
                    //
                    RootRef.child("volunteers").child(phone).updateChildren(accountDatamap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();

                                    //Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    //startActivity(intent);
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(RegisterActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    //

                    //after this create a login page
                    //create a login button on this page
                    //progressDialog.dismiss();//temp//here only chnage this
                } else {//TODO: Make sure this does not affect the application
                    Toast.makeText(RegisterActivity.this, "This " + phone + " already exists.", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please try again using another phone number.", Toast.LENGTH_SHORT).show();

                    Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RegisterActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();

                Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(mainIntent);
            }
        });
    }
}
