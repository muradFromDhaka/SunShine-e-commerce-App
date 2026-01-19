package com.abc.sunshine.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.abc.sunshine.entity.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryDao {

    private DatabaseHelper dbHelper;

    public CategoryDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // 1️⃣ Insert new Category
    public long insertCategory(Category category) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", category.getName());
        cv.put("description", category.getDescription());
        cv.put("brandId", category.getBrandId());
        cv.put("imageUrl", category.getImageUrl());
        long id = db.insert("category", null, cv);
        db.close();
        return id;
    }

    // 2️⃣ Update existing Category
    public int updateCategory(Category category) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", category.getName());
        cv.put("description", category.getDescription());
        cv.put("brandId", category.getBrandId());
        cv.put("imageUrl", category.getImageUrl());
        int rows = db.update("category", cv, "id=?", new String[]{String.valueOf(category.getId())});
        db.close();
        return rows;
    }

    // 3️⃣ Delete Category by id
    public int deleteCategory(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete("category", "id=?", new String[]{String.valueOf(id)});
        db.close();
        return rows;
    }

    // 4️⃣ Get Category by id
    public Category getCategoryById(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM category WHERE id=?", new String[]{String.valueOf(id)});
        Category category = null;
        if (c.moveToFirst()) {
            category = cursorToCategory(c);
        }
        c.close();
        db.close();
        return category;
    }

    // 5️⃣ Get all Categories (optionally by brandId)
    public List<Category> getAllCategories(Long brandId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Category> list = new ArrayList<>();
        Cursor c;

        if (brandId != null) {
            c = db.rawQuery("SELECT * FROM category WHERE brandId=?", new String[]{String.valueOf(brandId)});
        } else {
            c = db.rawQuery("SELECT * FROM category", null);
        }

        while (c.moveToNext()) {
            Category category = cursorToCategory(c);
            list.add(category);
        }

        c.close();
        db.close();
        return list;
    }

    // 🔹 Helper: Cursor → Category
    private Category cursorToCategory(Cursor c) {
        Category category = new Category();
        category.setId(c.getLong(c.getColumnIndexOrThrow("id")));
        category.setName(c.getString(c.getColumnIndexOrThrow("name")));
        category.setDescription(c.getString(c.getColumnIndexOrThrow("description")));
        category.setBrandId(c.getLong(c.getColumnIndexOrThrow("brandId")));
        category.setImageUrl(c.getString(c.getColumnIndexOrThrow("imageUrl")));
        return category;
    }
}
