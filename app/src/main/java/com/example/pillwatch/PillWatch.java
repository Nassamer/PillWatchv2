package com.example.pillwatch;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pillwatch.databinding.ActivityMainBinding;
import com.example.pillwatch.databinding.ActivityPillWatchBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import android.view.View;
import android.widget.Button;

public class PillWatch extends AppCompatActivity {

    Button goBackBtn;

    private ArrayList<Database> databases;
    private RecyclerView recyclerView;
    private DatabaseAdapter databaseAdapter;
    private ActivityPillWatchBinding binding;

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pill_watch);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pill_watch);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Dispenser App").child("SmartDispenserB95D00").child("Status");

        recyclerView = binding.recycleview;
        goBackBtn = binding.goBackToDirFromPillWatchBtn;

        databases = new ArrayList<>();

        recyclerView.setAdapter(databaseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseReference.addValueEventListener(new ValueEventListener() {

            int index = 0;
            String[] days = {"Hétfő", "Kedd", "Szerda", "Csütörtök", "Péntek", "Szombat", "Vasárnap"};
            String[] periods = {"Reggel", "Dél", "Este"};
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Database database = dataSnapshot.getValue(Database.class);
                    int dayIndex = index / 3;
                    int periodIndex = index % 3;

                    if (dayIndex < days.length) {
                        database.setDayOfWeek(days[dayIndex]);
                        database.setPeriod(periods[periodIndex]);
                    }

                    databases.add(database);
                    index++;
                }
                databaseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseAdapter = new DatabaseAdapter(this, databases);
        recyclerView.setAdapter(databaseAdapter);

        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PillWatch.this, Direction.class);
                startActivity(i);
            }
        });

    }
}