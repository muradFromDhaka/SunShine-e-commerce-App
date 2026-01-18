package com.abc.sunshine.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.abc.sunshine.db.DatabaseHelper;
import com.abc.sunshine.entity.Brand;

import java.util.ArrayList;
import java.util.List;

public class BrandDao {

    private final DatabaseHelper dbHelper;

    public BrandDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // 1️⃣ INSERT
    public long insertBrand(Brand brand) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("name", brand.getName());
        cv.put("description", brand.getDescription());
        cv.put("logo_url", brand.getLogoUrl());
        cv.put("created_at", brand.getCreatedAt());
        cv.put("updated_at", brand.getUpdatedAt());
        cv.put("deleted", 0);

        return db.insert(DatabaseHelper.TABLE_BRAND, null, cv);
    }

    // 2️⃣ GET ALL (not deleted)
    public List<Brand> getAllBrands() {

        List<Brand> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT * FROM brands WHERE deleted = 0",
                null
        );

        if (c.moveToFirst()) {
            do {
                Brand b = new Brand();
                b.setId(c.getLong(c.getColumnIndexOrThrow("id")));
                b.setName(c.getString(c.getColumnIndexOrThrow("name")));
                b.setDescription(c.getString(c.getColumnIndexOrThrow("description")));
                b.setLogoUrl(c.getString(c.getColumnIndexOrThrow("logo_url")));

                list.add(b);
            } while (c.moveToNext());
        }

        c.close();
        return list;
    }

    // 3️⃣ GET BY ID
    public Brand getBrandById(long id) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT * FROM brands WHERE id = ?",
                new String[]{String.valueOf(id)}
        );

        if (c.moveToFirst()) {
            Brand b = new Brand();
            b.setId(c.getLong(c.getColumnIndexOrThrow("id")));
            b.setName(c.getString(c.getColumnIndexOrThrow("name")));
            b.setDescription(c.getString(c.getColumnIndexOrThrow("description")));
            b.setLogoUrl(c.getString(c.getColumnIndexOrThrow("logo_url")));
            c.close();
            return b;
        }

        c.close();
        return null;
    }

    // 4️⃣ UPDATE
    public int updateBrand(Brand brand) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        brand.touch(); // 🔥 updated_at auto update

        ContentValues cv = new ContentValues();
        cv.put("name", brand.getName());
        cv.put("description", brand.getDescription());
        cv.put("logo_url", brand.getLogoUrl());
        cv.put("updated_at", brand.getUpdatedAt());

        return db.update(
                DatabaseHelper.TABLE_BRAND,
                cv,
                "id = ?",
                new String[]{String.valueOf(brand.getId())}
        );
    }

    // 5️⃣ DELETE (Soft delete)
    public int deleteBrand(long id) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("deleted", 1);

        return db.update(
                DatabaseHelper.TABLE_BRAND,
                cv,
                "id = ?",
                new String[]{String.valueOf(id)}
        );
    }
}

