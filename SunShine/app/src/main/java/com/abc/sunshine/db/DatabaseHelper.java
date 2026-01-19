package com.abc.sunshine.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "sunshine.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_BRAND = "brands";
    public static final String TABLE_CATEGORY = "category";
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true); // Enable foreign key enforcement
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_BRAND_TABLE =
                "CREATE TABLE " + TABLE_BRAND + " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT NOT NULL," +
                        "description TEXT" +
                        ");";

        db.execSQL(CREATE_BRAND_TABLE);

        String CREATE_CATEGORY_TABLE =
                "CREATE TABLE " + TABLE_CATEGORY + " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT NOT NULL," +
                        "description TEXT," +
                        "brandId INTEGER NOT NULL," +
                        "imageUrl TEXT," +
                        "FOREIGN KEY(brandId) REFERENCES " + TABLE_BRAND + "(id) ON DELETE CASCADE" +
                        ");";

        db.execSQL(CREATE_CATEGORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY); // drop Category first
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BRAND);
        onCreate(db);
    }
}

