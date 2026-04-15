package com.imraniman.projet_android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class GpsDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "gps_tracking.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_POSITIONS = "positions";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private static final String TABLE_CREATE =
            "CREATE TABLE "
                    + TABLE_POSITIONS
                    + " ("
                    + COLUMN_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_LATITUDE
                    + " REAL, "
                    + COLUMN_LONGITUDE
                    + " REAL, "
                    + COLUMN_TIMESTAMP
                    + " INTEGER"
                    + ");";

    public GpsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSITIONS);
        onCreate(db);
    }

    public void addPosition(double latitude, double longitude, long timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LATITUDE, latitude);
        values.put(COLUMN_LONGITUDE, longitude);
        values.put(COLUMN_TIMESTAMP, timestamp);
        db.insert(TABLE_POSITIONS, null, values);
        db.close();
    }

    public List<GpsPosition> getAllPositions() {
        List<GpsPosition> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =
                db.query(TABLE_POSITIONS, null, null, null, null, null, COLUMN_TIMESTAMP + " ASC");

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                double lat = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LATITUDE));
                double lng = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LONGITUDE));
                long ts = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP));
                list.add(new GpsPosition(id, lat, lng, ts));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public void deletePositions(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return;
        SQLiteDatabase db = this.getWritableDatabase();
        StringBuilder idList = new StringBuilder();
        for (int i = 0; i < ids.size(); i++) {
            idList.append(ids.get(i));
            if (i < ids.size() - 1) idList.append(",");
        }
        db.delete(TABLE_POSITIONS, COLUMN_ID + " IN (" + idList.toString() + ")", null);
        db.close();
    }

    public static class GpsPosition {
        public int id;
        public double latitude;
        public double longitude;
        public long timestamp;

        public GpsPosition(int id, double latitude, double longitude, long timestamp) {
            this.id = id;
            this.latitude = latitude;
            this.longitude = longitude;
            this.timestamp = timestamp;
        }
    }
}
