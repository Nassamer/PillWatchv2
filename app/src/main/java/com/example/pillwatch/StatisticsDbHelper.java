package com.example.pillwatch;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StatisticsDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "statistics.db";
    private static final int DB_VERSION = 1;

    public StatisticsDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE stats (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "binId TEXT, " +
                "timestamp TEXT, " +
                "wasFull INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS stats");
        onCreate(db);
    }

    public void insertStat(String binId, String timestamp, boolean wasFull) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("binId", binId);
        values.put("timestamp", timestamp);
        values.put("wasFull", wasFull ? 1 : 0);
        db.insert("stats", null, values);
        db.close();
    }

    public Cursor getAllStats() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM stats ORDER BY timestamp DESC", null);
    }

    public int getFullCountForWeek(int weekOffset) {
        SQLiteDatabase db = this.getReadableDatabase();
        int count = 0;

        // Define date format matching the stored timestamp
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Get current date and calculate start/end of desired week
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.add(Calendar.WEEK_OF_YEAR, weekOffset);
        Date startOfWeek = calendar.getTime();

        calendar.add(Calendar.DAY_OF_WEEK, 7);
        Date endOfWeek = calendar.getTime();

        @SuppressLint("DefaultLocale")
        String query = String.format(
                "SELECT COUNT(*) FROM stats WHERE wasFull = 1 AND timestamp >= ? AND timestamp < ?"
        );

        Cursor cursor = db.rawQuery(query, new String[] {
                dateFormat.format(startOfWeek),
                dateFormat.format(endOfWeek)
        });

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return count;
    }

    public float getFullChangePercentageWeekOverWeek() {
        int thisWeek = getFullCountForWeek(0);
        int lastWeek = getFullCountForWeek(-1);

        if (lastWeek == 0) return thisWeek > 0 ? 100 : 0;

        return ((float)(thisWeek - lastWeek) / lastWeek) * 100;
    }

}
