package com.example.fypproject;

import static com.example.fypproject.globals.Globals.ACCOUNT_TYPE_WORD;
import static com.example.fypproject.globals.Globals.MANAGER_WORD;
import static com.example.fypproject.globals.Globals.VOLUNTEER_WORD;
import static com.example.fypproject.security.Security.VERIFICATION_KEY_WORD;

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
import androidx.fragment.app.FragmentActivity;

import com.example.fypproject.enums.Type;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends FragmentActivity {
    private Button buttonCreateAccount;
    private EditText editTextName, editTextPhone, editTextPassword;
    private EditText managerEditTextName, managerEditTextPhone, managerEditTextPassword, managerEditTextVerification;
    private ProgressDialog progressDialog;

    //private Type accountType;//?
    private String typeOfAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        buttonCreateAccount = (Button) findViewById(R.id.buttonRegister);//
        editTextName = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);

        managerEditTextName = (EditText) findViewById(R.id.managerEditTextUsername);
        managerEditTextPhone = (EditText) findViewById(R.id.managerEditTextPhone);
        managerEditTextPassword = (EditText) findViewById(R.id.managerEditTextPassword);

        managerEditTextVerification  = (EditText) findViewById(R.id.managerEditTextVerification);




        progressDialog = new ProgressDialog(this);
        //Log.d("Type.VOLUNTEER", String.valueOf(Type.VOLUNTEER));
        Log.d("working", "working");

        //TODO: CONTINUE HERE
        System.out.println("COMPARISON");

        Intent intent = getIntent();
        typeOfAccount = intent.getStringExtra(ACCOUNT_TYPE_WORD); // Replace "key" with the same key used in the sending activity
        Log.d("LogTag", "the account type is:" +  typeOfAccount);

        if(Objects.equals(typeOfAccount, String.valueOf(Type.VOLUNTEER))){
            Log.d("Tag\"Volunteer works\"", "Volunteer works");

            editTextName.setVisibility(View.VISIBLE);
            editTextPhone.setVisibility(View.VISIBLE);
            editTextPassword.setVisibility(View.VISIBLE);

            managerEditTextName.setVisibility(View.INVISIBLE);
            managerEditTextPhone.setVisibility(View.INVISIBLE);
            managerEditTextPassword.setVisibility(View.INVISIBLE);
            managerEditTextVerification.setVisibility(View.INVISIBLE);

        }else if(Objects.equals(typeOfAccount,  String.valueOf(Type.MANAGER))){
            Log.d("TagManager works", "Manager works");

            editTextName.setVisibility(View.INVISIBLE);
            editTextPhone.setVisibility(View.INVISIBLE);
            editTextPassword.setVisibility(View.INVISIBLE);

            managerEditTextName.setVisibility(View.VISIBLE);
            managerEditTextPhone.setVisibility(View.VISIBLE);
            managerEditTextPassword.setVisibility(View.VISIBLE);
            managerEditTextVerification.setVisibility(View.VISIBLE);
        }

        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateAccountSuccessful()){
                    createAccount(name, phone, password);
                }

            }
        });
    }
    private static final int ZERO_WORD = 0;
    String name, phone, password, verificationKey;

    private boolean validateAccountSuccessful() {
        int numOfErrors=ZERO_WORD;
        if(typeOfAccount.equals(VOLUNTEER_WORD)){
            name = editTextName.getText().toString();
            phone = editTextPhone.getText().toString();
            password = editTextPassword.getText().toString();

            if (TextUtils.isEmpty(name)) {
                Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
                numOfErrors++;
            } else if (TextUtils.isEmpty(phone)) {
                Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
                numOfErrors++;
            } else if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
                numOfErrors++;
            }

            if(numOfErrors==ZERO_WORD) {
                progressDialog.setTitle("Creating Account");
                progressDialog.setMessage("Please wait, while we are checking the credentials.");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                return true;
                //HOW DOES IT MOVE TO NEXT FUNCTION?
            }
        } else if(typeOfAccount.equals(MANAGER_WORD)){
            name = managerEditTextName.getText().toString();
            phone = managerEditTextPhone.getText().toString();
            password = managerEditTextPassword.getText().toString();
            verificationKey = managerEditTextVerification.getText().toString();

            if (TextUtils.isEmpty(name)) {
                Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
                numOfErrors++;
            } else if (TextUtils.isEmpty(phone)) {
                Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
                numOfErrors++;
            } else if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
                numOfErrors++;
            } else if (TextUtils.isEmpty(password)){
                Toast.makeText(this, "Please write your verification key...", Toast.LENGTH_SHORT).show();
                numOfErrors++;
            } else {
                if (Integer.parseInt(verificationKey) != VERIFICATION_KEY_WORD){
                    Toast.makeText(this, "Verification key declined", Toast.LENGTH_SHORT).show();
                    numOfErrors++;
                }
            }
            Log.d("verificationKey", verificationKey);

            if(numOfErrors==ZERO_WORD) {
                progressDialog.setTitle("Creating Manager Account");
                progressDialog.setMessage("Please wait, while we are checking the credentials.");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                //HOW DOES IT MOVE TO NEXT FUNCTION?
                return true;
            }
        }
        return false;
    }


    //Check phone and then use that phone number to create an account.
    private void createAccount(final String name, final String phone, final String password) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = database.getReference();


        DatabaseReference childRef = rootRef.child(typeOfAccount);


        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            //This happens if the data in the field is modified
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                accessDatabase(dataSnapshot, phone, password, name, rootRef);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RegisterActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();

                Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                mainIntent.putExtra(ACCOUNT_TYPE_WORD, typeOfAccount);
                startActivity(mainIntent);
                //finish();
            }
        });
    }

    private void accessDatabase(@NonNull DataSnapshot dataSnapshot, String phone, String password, String name, DatabaseReference rootRef) {
        //TODO: IF IS MANAGER BUTTON SELECTED DO THIS:
        //TODO: USE A FUNCTION TO REPEAT LOGIC
        //If there is a phone number associated
        if (!(dataSnapshot.child(typeOfAccount).child(phone).exists())) {
            HashMap<String, Object> accountDatamap = new HashMap<>();
            accountDatamap.put("phone", phone);
            accountDatamap.put("password", password);
            accountDatamap.put("name", name);

            Log.d("TAGaccountDatamap", "onDataChange: " + accountDatamap);
            //TODO: add the phone again

            //Add to list of volunteers's details
            rootRef.child(typeOfAccount).child(phone).updateChildren(accountDatamap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Congratulations, your account has been created", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                intent.putExtra(ACCOUNT_TYPE_WORD, typeOfAccount);
                                startActivity(intent);
                                //finish();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "Network Error: Please try again after some time", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            } else {//TODO: Make sure this does not affect the application
                Toast.makeText(RegisterActivity.this, "This number: " + phone + " already exists.", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "Please try again using another phone number.", Toast.LENGTH_SHORT).show();

                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                loginIntent.putExtra(ACCOUNT_TYPE_WORD, typeOfAccount);
                startActivity(loginIntent);
                //finish();
            }
    }
}
