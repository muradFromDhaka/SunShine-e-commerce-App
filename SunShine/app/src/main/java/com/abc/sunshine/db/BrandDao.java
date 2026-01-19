package com.abc.sunshine.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.abc.sunshine.entity.Brand;

import java.util.ArrayList;
import java.util.List;

public class BrandDao {

    private DatabaseHelper dbHelper;

    public BrandDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // 1. Insert a new brand
    public long insertBrand(Brand brand) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", brand.getName());
        values.put("description", brand.getDescription());

        long id = db.insert(DatabaseHelper.TABLE_BRAND, null, values);
        db.close();
        return id;
    }

    // 4. Update a brand
    public int updateBrand(long id, Brand brand) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", brand.getName());
        values.put("description", brand.getDescription());
        String[] arg = new String[]{String.valueOf(id)};

        int rowsAffected = db.update(DatabaseHelper.TABLE_BRAND, values, "id=?", arg);

        db.close();
        return rowsAffected;
    }

    // 2. Get a brand by ID
    public Brand getBrandById(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] arg = new String[]{String.valueOf(id)};
        Cursor cursor = db.rawQuery("SELECT * FROM " + dbHelper.TABLE_BRAND + " WHERE id=?", arg);

        Brand brand = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                brand = cursorToBrand(cursor); // populate brand using helper
            }
            cursor.close();
        }
        db.close();
        return brand;
    }

    // 3. Get all brands
    public List<Brand> getAllBrands() {
        List<Brand> brandList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_BRAND, null);

        if (cursor.moveToFirst()) {
            do {
                Brand brand = cursorToBrand(cursor); // populate from cursor
                brandList.add(brand);               // add to list
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return brandList;
    }


    public int deleteBrand(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(dbHelper.TABLE_BRAND, "id=?", new String[]{String.valueOf(id)});
    }



    // 🔹 Cursor → Brand
    private Brand cursorToBrand(Cursor c) {
        Brand b = new Brand();
        b.setId(c.getLong(c.getColumnIndexOrThrow("id")));
        b.setName(c.getString(c.getColumnIndexOrThrow("name")));
        b.setDescription(c.getString(c.getColumnIndexOrThrow("description")));
        return b;
    }


}
