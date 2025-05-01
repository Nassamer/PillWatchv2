package com.example.pillwatch;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Alarm extends AppCompatActivity {

    //List of days
    private final String[] binLabels = {
            "Hétfő Reggel", "Hétfő Dél", "Hétfő Este",
            "Kedd Reggel", "Kedd Dél", "Kedd Este",
            "Szerda Reggel", "Szerda Dél", "Szerda Este",
            "Csütörtök Reggel", "Csütörtök Dél", "Csütörtök Este",
            "Péntek Reggel", "Péntek Dél", "Péntek Este",
            "Szombat Reggel", "Szombat Dél", "Szombat Este",
            "Vasárnap Reggel", "Vasárnap Dél", "Vasárnap Este"
    };

    // Corresponding Firebase bins
    private final String[] binKeys = {
            "Bin0", "Bin1", "Bin2", "Bin3", "Bin4", "Bin5", "Bin6",
            "Bin7", "Bin8", "Bin9", "Bin10", "Bin11", "Bin12", "Bin13",
            "Bin14", "Bin15", "Bin16", "Bin17", "Bin18", "Bin19", "Bin20"
    };

    Button  goBackBtn, checkStatisticBtn, saveButton;
    Switch alarmSwitch, phoneAlarmSwitch;
    TimePicker timePicker;
    private Spinner binSelector;
    private String selectedBin = "Bin0";

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
        saveButton = findViewById(R.id.saveButton);
        goBackBtn = findViewById(R.id.goBackToDirFromAlarmButton);
        checkStatisticBtn = findViewById(R.id.goToStatisticsButton);

        alarmSwitch = findViewById(R.id.alarmSwitch);
        timePicker = findViewById(R.id.timePicker);
        phoneAlarmSwitch = findViewById(R.id.phoneAlarmSwitch);
        timePicker.setIs24HourView(true);

        //Setting up Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
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

        //Choosing which day

        binSelector = findViewById(R.id.binSelector);
        List<String> binList = new ArrayList<>();
        for (int i = 0; i <= 20; i++) {
            binList.add("Bin" + i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, binLabels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binSelector.setAdapter(adapter);

        binSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedBin = parent.getItemAtPosition(position).toString();
                loadAlarmData(); // Load values for the selected bin
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        //Phone Alarm setup
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(
                    "alarmChannel",
                    "Alarm Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (!alarmManager.canScheduleExactAlarms()) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        }

        loadAlarmData();

        saveButton.setOnClickListener(v -> saveAlarmData());
    }

    private void loadAlarmData() {
        databaseReference.child(selectedBin).child("alarmState").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String state = snapshot.getValue(String.class);
                alarmSwitch.setChecked("ON".equals(state));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        databaseReference.child(selectedBin).child("alarmTime").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String time = snapshot.getValue(String.class);
                if (time != null && time.contains(":")) {
                    String[] parts = time.split(":");
                    timePicker.setHour(Integer.parseInt(parts[0]));
                    timePicker.setMinute(Integer.parseInt(parts[1]));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void saveAlarmData() {
        String newState = alarmSwitch.isChecked() ? "ON" : "OFF";
        String newTime = String.format(Locale.getDefault(), "%02d:%02d", timePicker.getHour(), timePicker.getMinute());

        int selectedIndex = binSelector.getSelectedItemPosition();
        String firebaseBinKey = binKeys[selectedIndex];

        Map<String, Object> updates = new HashMap<>();
        updates.put("alarmState", newState);
        updates.put("alarmTime", newTime);


        //Phone alarm update
        if (phoneAlarmSwitch.isChecked()) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
            calendar.set(Calendar.MINUTE, timePicker.getMinute());
            calendar.set(Calendar.SECOND, 0);

            Intent intent = new Intent(this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

        //Firebase update
        databaseReference.child(firebaseBinKey).updateChildren(updates).addOnSuccessListener(unused -> {
            Toast.makeText(Alarm.this, "Riasztás Frissítve", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(Alarm.this, "Nem sikerült frissíteni", Toast.LENGTH_SHORT).show();
        });
    }
}