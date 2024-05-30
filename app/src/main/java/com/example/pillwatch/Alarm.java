package com.example.pillwatch;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Alarm extends AppCompatActivity {

    Button setAlarmBtn, goBackBtn, checkStatisticBtn, removeAlarmBtn;
    private int dynamicday = 0;
    private int radionButtonInt;
    private EditText alarmTimeText;
    RadioGroup radioGroup;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_alarm);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Setting up buttons
        setAlarmBtn = findViewById(R.id.setAlarmButton);
        goBackBtn = findViewById(R.id.goBackToDirFromAlarmButton);
        checkStatisticBtn = findViewById(R.id.goToStatisticsButton);
        removeAlarmBtn = findViewById(R.id.turnOffAlarmButton);
        radioGroup = findViewById(R.id.dayCycleRadioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = findViewById(i);
                if(radioButton.getText() == "Reggel"){
                    radionButtonInt = 0;
                } else if (radioButton.getText() == "Dél") {
                    radionButtonInt = 1;
                }
                else{
                    radionButtonInt = 2;
                }
            }
        });



        //Setting up texts
        alarmTimeText = findViewById(R.id.editTextTime);

        //Setting up Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        //TODO make it dynamic
        databaseReference = firebaseDatabase.getReference("Dispenser App").
                child("SmartDispenserB95D00").child("Status");

        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Alarm.this, Direction.class);
                startActivity(i);
            }
        });

        checkStatisticBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Alarm.this, Statistics.class);
                startActivity(i);
            }
        });
    }
}