package com.example.fypproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

    private String parentDbName = "volunteers";//change in future
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

        chkBoxRememberMe = findViewById(R.id.remember_me_chkb);//Checkbox
        Paper.init(this);


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
//                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
//                startActivity(intent);
            }
        });

        managerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                loginButton.setText("Manager Login");
                managerLink.setVisibility(View.INVISIBLE);
                volunteerLink.setVisibility(View.VISIBLE);
                parentDbName = "managers";
            }
        });

        volunteerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                loginButton.setText("Login");
                managerLink.setVisibility(View.VISIBLE);
                volunteerLink.setVisibility(View.INVISIBLE);
                parentDbName = "volunteers";
            }
        });

    }

    private void LoginUser() {
        String phone = inputPhoneNumber.getText().toString();
        String password = inputPassword.getText().toString();

        //If this field is empty
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            AccessGranted(phone, password);
        }
    }

    private void AccessGranted(final String phone, final String password) {

        //If the check me box is ticked
        if(chkBoxRememberMe.isChecked()){
            Paper.book().write(Prevalent.ACCOUNT_PHONE_KEY, phone);
            Paper.book().write(Prevalent.ACCOUNT_PASSWORD_KEY, password);
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
                           if(parentDbName.equals("managers")){
                               Toast.makeText(LoginActivity.this, "Welcome manager, you are logged in successfully...", Toast.LENGTH_LONG).show();
                               loadingBar.dismiss();

                               Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                               startActivity(intent);
                           } else if(parentDbName.equals("volunteers")){
                               Toast.makeText(LoginActivity.this, "Logged in successfully...", Toast.LENGTH_LONG).show();
                               loadingBar.dismiss();

                               Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                               Prevalent.currentOnlineUser = accountData;
                               startActivity(intent);
                           }
                        }else{
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Password is incorrect.",Toast.LENGTH_SHORT).show();
                        }

                    }



                }else{
                    Toast.makeText(LoginActivity.this, "Account with this " + phone + " number do not exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}