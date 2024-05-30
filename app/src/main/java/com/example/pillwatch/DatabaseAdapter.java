package com.example.pillwatch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pillwatch.databinding.ItemDatabaseBinding;

import java.util.ArrayList;

public class DatabaseAdapter extends RecyclerView.Adapter<DatabaseAdapter.DatabaseViewHolder> {

    private Context context;
    private ArrayList<Database> databaseArrayList;

    public DatabaseAdapter(Context context, ArrayList<Database> databaseArrayList) {
        this.context = context;
        this.databaseArrayList = databaseArrayList;
    }

    @NonNull
    @Override
    public DatabaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemDatabaseBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_database,
                parent,
                false
        );

        return new DatabaseViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DatabaseViewHolder holder, int position) {
        Database database = databaseArrayList.get(position);
        holder.itemDatabaseBinding.setDatabase(database);

    }

    @Override
    public int getItemCount() {
        return databaseArrayList.size();
    }

    public class DatabaseViewHolder extends RecyclerView.ViewHolder{
        private ItemDatabaseBinding itemDatabaseBinding;


        public DatabaseViewHolder(ItemDatabaseBinding itemDatabaseBinding) {
            super(itemDatabaseBinding.getRoot());
            this.itemDatabaseBinding = itemDatabaseBinding;
        }
    }
}
