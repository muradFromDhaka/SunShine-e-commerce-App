package com.abc.sunshine.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.abc.sunshine.entity.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProductDao {

    private final DatabaseHelper dbHelper;
    private final Gson gson = new Gson();

    public ProductDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // INSERT
    public long insert(Product p) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("name", p.getName());
        cv.put("description", p.getDescription());
        cv.put("price", p.getPrice());
        cv.put("discount_price", p.getDiscountPrice()); // fix
        cv.put("sku", p.getSku());
        cv.put("reviews_count", p.getReviewsCount()); // fix
        cv.put("quantity", p.getQuantity());
        cv.put("categoryId", p.getCategoryId());
        cv.put("brandId", p.getBrandId());
        cv.put("image_urls", gson.toJson(p.getImageUrls())); // fix
        cv.put("is_active", p.isActive() ? 1 : 0); // fix

        long id = db.insert(DatabaseHelper.TABLE_PRODUCT, null, cv);
        db.close();
        return id;
    }

    // UPDATE
    public int update(Product p) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("name", p.getName());
        cv.put("description", p.getDescription());
        cv.put("price", p.getPrice());
        cv.put("discount_price", p.getDiscountPrice()); // fix
        cv.put("sku", p.getSku());
        cv.put("reviews_count", p.getReviewsCount()); // fix
        cv.put("quantity", p.getQuantity());
        cv.put("categoryId", p.getCategoryId());
        cv.put("brandId", p.getBrandId());
        cv.put("image_urls", gson.toJson(p.getImageUrls())); // fix
        cv.put("is_active", p.isActive() ? 1 : 0); // fix

        int rows = db.update(
                DatabaseHelper.TABLE_PRODUCT,
                cv,
                "id=" + p.getId(),
                null
        );

        db.close();
        return rows;
    }

    // DELETE
    public void delete(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM products WHERE id=" + id);
        db.close();
    }

    // GET ALL
    public List<Product> getAll() {
        List<Product> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT * FROM products ORDER BY id DESC",
                null
        );

        while (c.moveToNext()) {
            list.add(cursorToProduct(c));
        }

        c.close();
        db.close();
        return list;
    }

    // GET BY ID
    public Product getById(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT * FROM products WHERE id=" + id,
                null
        );

        Product p = null;
        if (c.moveToFirst()) {
            p = cursorToProduct(c);
        }

        c.close();
        db.close();
        return p;
    }

    // Cursor → Product
    private Product cursorToProduct(Cursor c) {

        Type listType = new TypeToken<List<String>>() {}.getType();

        Product p = new Product();
        p.setId(c.getLong(c.getColumnIndexOrThrow("id")));
        p.setName(c.getString(c.getColumnIndexOrThrow("name")));
        p.setDescription(c.getString(c.getColumnIndexOrThrow("description")));
        p.setPrice(c.getDouble(c.getColumnIndexOrThrow("price")));
        p.setDiscountPrice(c.getDouble(c.getColumnIndexOrThrow("discount_price")));
        p.setSku(c.getString(c.getColumnIndexOrThrow("sku")));
        p.setReviewsCount(c.getInt(c.getColumnIndexOrThrow("reviews_count")));
        p.setQuantity(c.getInt(c.getColumnIndexOrThrow("quantity")));
        p.setCategoryId(c.getLong(c.getColumnIndexOrThrow("categoryId")));
        p.setBrandId(c.getLong(c.getColumnIndexOrThrow("brandId")));
        p.setImageUrls(
                gson.fromJson(
                        c.getString(c.getColumnIndexOrThrow("image_urls")),
                        listType
                )
        );
        p.setActive(c.getInt(c.getColumnIndexOrThrow("is_active")) == 1);

        return p;
    }
}
