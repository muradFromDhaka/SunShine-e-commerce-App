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

    private DatabaseHelper dbHelper;
    private Gson gson;

    public ProductDao(Context context) {
        dbHelper = new DatabaseHelper(context);
        gson = new Gson();
    }

    // 1️⃣ Insert a new product
    public long insert(Product product) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", product.getName());
        cv.put("description", product.getDescription());
        cv.put("price", product.getPrice());
        cv.put("discount_price", product.getDiscountPrice());
        cv.put("sku", product.getSku());
        cv.put("reviews_count", product.getReviewsCount());
        cv.put("quantity", product.getQuantity());
        cv.put("categoryId", product.getCategoryId());
        cv.put("brandId", product.getBrandId());
        cv.put("image_urls", gson.toJson(product.getImageUrls())); // Convert list to JSON
        cv.put("is_active", product.isActive() ? 1 : 0);

        long id = db.insert(DatabaseHelper.TABLE_PRODUCT, null, cv);
        db.close();
        return id;
    }

    // 2️⃣ Update an existing product
    public int update(Product product) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", product.getName());
        cv.put("description", product.getDescription());
        cv.put("price", product.getPrice());
        cv.put("discount_price", product.getDiscountPrice());
        cv.put("sku", product.getSku());
        cv.put("reviews_count", product.getReviewsCount());
        cv.put("quantity", product.getQuantity());
        cv.put("categoryId", product.getCategoryId());
        cv.put("brandId", product.getBrandId());
        cv.put("image_urls", gson.toJson(product.getImageUrls()));
        cv.put("is_active", product.isActive() ? 1 : 0);

        int rows = db.update(DatabaseHelper.TABLE_PRODUCT, cv, "id=?", new String[]{String.valueOf(product.getId())});
        db.close();
        return rows;
    }

    // 3️⃣ Delete a product by id
    public int delete(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete(DatabaseHelper.TABLE_PRODUCT, "id=?", new String[]{String.valueOf(id)});
        db.close();
        return rows;
    }

    // 4️⃣ Get a single product by id
    public Product getById(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_PRODUCT, null, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        Product product = null;

        if (cursor != null && cursor.moveToFirst()) {
            product = cursorToProduct(cursor);
            cursor.close();
        }
        db.close();
        return product;
    }

    // 5️⃣ Get all products
    public List<Product> getAll() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_PRODUCT, null, null, null, null, null, "id DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Product product = cursorToProduct(cursor);
                productList.add(product);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return productList;
    }

    // Helper method to convert Cursor to Product
    private Product cursorToProduct(Cursor cursor) {
        Product product = new Product();
        product.setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
        product.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
        product.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
        product.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("price")));
        product.setDiscountPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("discount_price")));
        product.setSku(cursor.getString(cursor.getColumnIndexOrThrow("sku")));
        product.setReviewsCount(cursor.getString(cursor.getColumnIndexOrThrow("reviews_count")));
        product.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow("quantity")));
        product.setCategoryId(cursor.getLong(cursor.getColumnIndexOrThrow("categoryId")));
        product.setBrandId(cursor.getLong(cursor.getColumnIndexOrThrow("brandId")));

        String imagesJson = cursor.getString(cursor.getColumnIndexOrThrow("image_urls"));
        Type listType = new TypeToken<List<String>>() {}.getType();
        List<String> images = gson.fromJson(imagesJson, listType);
        product.setImageUrls(images);

        product.setActive(cursor.getInt(cursor.getColumnIndexOrThrow("is_active")) == 1);
        return product;
    }
}

