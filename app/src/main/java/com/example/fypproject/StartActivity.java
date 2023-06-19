package com.example.fypproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//
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
                startActivity(loginIntent);
                finish();
            }
        });

        managerLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent managerLoginIntent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(managerLoginIntent);
                finish();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(StartActivity.this, RegisterActivity.class);
                registerIntent.putExtra("accountType", "VOLUNTEER");
                startActivity(registerIntent);
                finish();
            }
        });

        //Register
        managerRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent managerRegisterIntent = new Intent(StartActivity.this, RegisterActivity.class);
                managerRegisterIntent.putExtra("accountType", "MANAGER");
                startActivity(managerRegisterIntent);
                finish();
            }
        });
    }
}