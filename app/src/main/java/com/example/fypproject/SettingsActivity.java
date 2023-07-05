package com.example.fypproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collections;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner skillLevelSpinner;
    TextView skillLevelTextView;
    Button settingsSaveButton;
    String[] skillLevelValues = {"Beginner", "Novice", "Pro"};
    SharedPreferences sharedPreferences;
    String currentSkillLevel;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        skillLevelSpinner = findViewById(R.id.skill_level_spinner);
        skillLevelTextView = findViewById(R.id.skill_level_tv);
        settingsSaveButton = findViewById(R.id.setting_save_button);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, skillLevelValues);

        skillLevelSpinner.setAdapter(adapter);
        skillLevelSpinner.setOnItemSelectedListener(this);

        sharedPreferences = getSharedPreferences("skillLevel", Context.MODE_PRIVATE);


        retrieveSkillLevel();


        settingsSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeSkillLevel();
                finish();
            }
        });
    }


    private void retrieveSkillLevel(){
        currentSkillLevel = sharedPreferences.getString("skillLevel", currentSkillLevel);
        skillLevelTextView.setText(currentSkillLevel);
    }

    private void writeSkillLevel(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        skillLevelTextView.setText(chosenSkillLevel);
        editor.putString("skillLevel", chosenSkillLevel);
        editor.apply();
    }


    String chosenSkillLevel;
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        chosenSkillLevel = adapterView.getItemAtPosition(i).toString();
        Log.d("currentSkillLevelTag", chosenSkillLevel);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}