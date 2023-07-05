package com.example.fypproject;

import static com.example.fypproject.globals.Globals.ACCOUNT_TYPE_WORD;
import static com.example.fypproject.globals.Globals.MANAGER_WORD;
import static com.example.fypproject.globals.Globals.VOLUNTEER_WORD;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.fypproject.models.Account;
import com.example.fypproject.models.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText inputPhoneNumber, inputPassword;
    private Button loginButton;
    private ProgressDialog loadingBar;
    private TextView managerLink, volunteerLink, forgetPasswordLink;

    private String parentDbName = VOLUNTEER_WORD;
    private CheckBox chkBoxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.login_btn);
        inputPassword = findViewById(R.id.login_password_input);
        inputPhoneNumber = findViewById(R.id.login_phone_number_input);
        managerLink = findViewById(R.id.manager_panel_link);
        volunteerLink = findViewById(R.id.not_manager_panel_link);
        forgetPasswordLink = findViewById(R.id.forget_password_link);
        loadingBar = new ProgressDialog(this);


        chkBoxRememberMe = findViewById(R.id.remember_me_chkb);
        Paper.init(this);

        Intent intent = getIntent();
        parentDbName = intent.getStringExtra(ACCOUNT_TYPE_WORD);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                LoginUser();
            }
        });

        forgetPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(LoginActivity.this, "Need to add this", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);

            }
        });

        managerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                loginButton.setText("Manager Login");
                managerLink.setVisibility(View.INVISIBLE);
                volunteerLink.setVisibility(View.VISIBLE);
                parentDbName = MANAGER_WORD;
            }
        });

        volunteerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                loginButton.setText("Login");
                managerLink.setVisibility(View.VISIBLE);
                volunteerLink.setVisibility(View.INVISIBLE);
                parentDbName = VOLUNTEER_WORD;
            }
        });

    }

    private void LoginUser() {
        String phone = inputPhoneNumber.getText().toString();
        String password = inputPassword.getText().toString();

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            AccessGranted(phone, password);
        }
    }

    private void AccessGranted(final String phone, final String password) {

        if(chkBoxRememberMe.isChecked()){
            Paper.book().write(Prevalent.ACCOUNT_PHONE_KEY, phone);
            Paper.book().write(Prevalent.ACCOUNT_PASSWORD_KEY, password);
        }else{
            Paper.book().destroy();
        }

        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child(parentDbName).child(phone).exists()){
                    Account accountData = snapshot.child(parentDbName).child(phone).getValue(Account.class);

                    if(accountData.getPhone().equals(phone)){
                        if(accountData.getPassword().equals(password)){
                           if(parentDbName.equals(MANAGER_WORD)){
                               Toast.makeText(LoginActivity.this, "Welcome manager, you are logged in successfully...", Toast.LENGTH_LONG).show();
                               loadingBar.dismiss();

                               Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                               Prevalent.currentOnlineUser = accountData;
                               intent.putExtra(ACCOUNT_TYPE_WORD, parentDbName);
                               startActivity(intent);
                               //finish();
                           } else if(parentDbName.equals(VOLUNTEER_WORD)){
                               Toast.makeText(LoginActivity.this, "Logged in successfully...", Toast.LENGTH_LONG).show();
                               loadingBar.dismiss();

                               Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                               Prevalent.currentOnlineUser = accountData;
                               intent.putExtra(ACCOUNT_TYPE_WORD, parentDbName);
                               startActivity(intent);
                               //finish();
                           }
                        }else{
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Password is incorrect.",Toast.LENGTH_SHORT).show();
                        }

                    }



                }else{
                    Toast.makeText(LoginActivity.this, "Account with this phone number does not exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}