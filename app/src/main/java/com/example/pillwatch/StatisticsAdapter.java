package com.example.pillwatch;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.ViewHolder>{
    private final Context context;
    private final Cursor cursor;

    public StatisticsAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_statistics, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (cursor.moveToPosition(position)) {
            String binId = cursor.getString(cursor.getColumnIndex("binId"));
            String timestamp = cursor.getString(cursor.getColumnIndex("timestamp"));
            boolean wasFull = cursor.getInt(cursor.getColumnIndex("wasFull")) == 1;

            holder.binIdTextView.setText(binId);
            holder.timestampTextView.setText(timestamp);
            holder.statusTextView.setText(wasFull ? "Késett/kihagyott" : "Bevett");
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView binIdTextView;
        TextView timestampTextView;
        TextView statusTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            binIdTextView = itemView.findViewById(R.id.binId);
            timestampTextView = itemView.findViewById(R.id.timestamp);
            statusTextView = itemView.findViewById(R.id.status);
        }
    }
}
