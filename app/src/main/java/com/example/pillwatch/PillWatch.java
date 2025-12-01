package com.example.pillwatch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pillwatch.databinding.ActivityPillWatchBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class PillWatch extends AppCompatActivity {

    Button goBackBtn;

    private ArrayList<Database> databases;
    RecyclerView recyclerView;
    private DatabaseAdapter databaseAdapter;
    private ActivityPillWatchBinding binding;

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_pill_watch);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Dispenser App")
                .child("SmartDispenserB95D00")
                .child("Status");

        recyclerView = binding.recycleview;
        goBackBtn = binding.goBackToDirFromPillWatchBtn;

        databases = new ArrayList<>();
        databaseAdapter = new DatabaseAdapter(this, databases);

        recyclerView.setAdapter(databaseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Firebase Listener
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                databases.clear();
                ArrayList<DataSnapshot> ordered = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ordered.add(ds);
                }

                Collections.sort(ordered, (a, b) -> {
                    int ai = Integer.parseInt(a.getKey().replace("Bin", ""));
                    int bi = Integer.parseInt(b.getKey().replace("Bin", ""));
                    return Integer.compare(ai, bi);
                });

                String[] days = {"Hétfő", "Kedd", "Szerda", "Csütörtök", "Péntek", "Szombat", "Vasárnap"};
                String[] periods = {"Reggel", "Dél", "Este"};

                for (int i = 0; i < ordered.size(); i++) {

                    Database db = ordered.get(i).getValue(Database.class);

                    int dayIndex = i / 3;     // 0..6
                    int periodIndex = i % 3;  // 0..2

                    db.setDayOfWeek(days[dayIndex]);
                    db.setPeriod(periods[periodIndex]);

                    databases.add(db);
                }

                // 5. RecyclerView frissítése
                databaseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        goBackBtn.setOnClickListener(view -> {
            Intent i = new Intent(PillWatch.this, Direction.class);
            startActivity(i);
        });
    }
}