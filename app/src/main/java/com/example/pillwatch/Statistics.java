package com.example.pillwatch;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Statistics extends AppCompatActivity {

    Button goBackBtn;
    private StatisticsDbHelper dbHelper;
    private RecyclerView recyclerView;
    private StatisticsAdapter adapter;

    private TextView weeklyComparisonText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_statistics);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dbHelper = new StatisticsDbHelper(this);
        recyclerView = findViewById(R.id.statisticsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch and display statistics
        loadStatistics();

        weeklyComparisonText = findViewById(R.id.weeklyComparisonText);
        loadWeeklyComparison();

        goBackBtn = findViewById(R.id.goBackToAlarmFromStatisticsBtn);

        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Statistics.this, Alarm.class);
                startActivity(i);
            }
        });
    }

    private void loadStatistics() {
        Cursor cursor = dbHelper.getAllStats();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Nincs elérhető statisztika", Toast.LENGTH_SHORT).show();
        } else {
            adapter = new StatisticsAdapter(this, cursor);
            recyclerView.setAdapter(adapter);
        }
    }

    private void loadWeeklyComparison() {
        int thisWeek = dbHelper.getFullCountForWeek(0);
        int lastWeek = dbHelper.getFullCountForWeek(-1);
        float change = dbHelper.getFullChangePercentageWeekOverWeek();

        String changeText = String.format("%.1f%%", change);
        String comparisonText;

        if (lastWeek == 0 && thisWeek > 0) {
            comparisonText = "Ez az első hét, amikor adat elérhető.";
        } else if (thisWeek == lastWeek) {
            comparisonText = "A héten ugyanannyi teljes kuka volt, mint múlt héten.";
        } else if (thisWeek > lastWeek) {
            comparisonText = "Több teljes kuka volt ezen a héten (+ " + changeText + ").";
        } else {
            comparisonText = "Kevesebb teljes kuka volt ezen a héten (− " + changeText + ").";
        }

        weeklyComparisonText.setText(comparisonText);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}