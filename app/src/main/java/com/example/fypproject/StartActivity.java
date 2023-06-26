package com.example.fypproject;

import static com.example.fypproject.globals.Globals.ACCOUNT_TYPE_WORD;
import static com.example.fypproject.globals.Globals.MANAGER_WORD;
import static com.example.fypproject.globals.Globals.VOLUNTEER_WORD;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class StartActivity extends AppCompatActivity {
    private Button registerBtn, loginBtn;
    private Button managerRegisterBtn, managerLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        registerBtn = (Button) findViewById(R.id.registerBtn);
        loginBtn = (Button) findViewById(R.id.loginBtn);

        managerRegisterBtn = (Button) findViewById(R.id.managerRegisterBtn);
        managerLoginBtn = (Button) findViewById(R.id.managerLoginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(StartActivity.this, LoginActivity.class);
                loginIntent.putExtra(ACCOUNT_TYPE_WORD, VOLUNTEER_WORD);
                startActivity(loginIntent);
                //finish();
            }
        });

        managerLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent managerLoginIntent = new Intent(StartActivity.this, LoginActivity.class);
                managerLoginIntent.putExtra(ACCOUNT_TYPE_WORD, MANAGER_WORD);
                startActivity(managerLoginIntent);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(StartActivity.this, RegisterActivity.class);
                registerIntent.putExtra(ACCOUNT_TYPE_WORD, VOLUNTEER_WORD);
                startActivity(registerIntent);
            }
        });


        managerRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent managerRegisterIntent = new Intent(StartActivity.this, RegisterActivity.class);
                managerRegisterIntent.putExtra(ACCOUNT_TYPE_WORD, MANAGER_WORD);
                startActivity(managerRegisterIntent);
            }
        });
    }
}