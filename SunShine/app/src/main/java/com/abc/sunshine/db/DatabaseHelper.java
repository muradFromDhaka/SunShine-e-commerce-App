package com.abc.sunshine.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "sunshine.db";
    public static final int DB_VERSION = 1;

    // 🔹 Brand table
    public static final String TABLE_BRAND = "brands";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_BRAND_TABLE =
                "CREATE TABLE " + TABLE_BRAND + " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT," +
                        "description TEXT," +
                        "logo_url TEXT," +
                        "deleted INTEGER DEFAULT 0," +
                        "created_at TEXT," +
                        "updated_at TEXT" +
                        ")";

        db.execSQL(CREATE_BRAND_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BRAND);
        onCreate(db);
    }
}

