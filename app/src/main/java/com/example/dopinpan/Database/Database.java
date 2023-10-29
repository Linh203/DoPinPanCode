package com.example.dopinpan.Database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dopinpan.Model.Order;
import com.example.dopinpan.Model.Request;
import com.example.dopinpan.Model.Statictical;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    private static final String DB_NAME = "DoPinPanDB.db";
    private static final int DB_VER = 3;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }


    @SuppressLint("Range")
    public List<Order> getCart() {
        SQLiteDatabase db = getReadableDatabase();
        String sqlSelect = "SELECT * FROM OderDetail";
        Cursor cursor = db.rawQuery(sqlSelect, null);
        final List<Order> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                result.add(new Order(cursor.getString(cursor.getColumnIndex("ProductId")),
                        cursor.getString(cursor.getColumnIndex("ProductName")),
                        cursor.getString(cursor.getColumnIndex("Quantity")),
                        cursor.getString(cursor.getColumnIndex("Price")),
                        cursor.getString(cursor.getColumnIndex("Discount"))));
            } while (cursor.moveToNext());
        }
        return result;
    }

    public void addToCart(Order order) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO OderDetail(ProductId,ProductName,Quantity,Price,Discount) VALUES ('%s','%s','%s','%s','%s');",
                order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                order.getDiscount());
        db.execSQL(query);
    }

    public void cleanCart() {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OderDetail");
        db.execSQL(query);
    }

    public void addToFavorites(String foodId) {
        SQLiteDatabase database = getReadableDatabase();
        String query = String.format("INSERT INTO Favorites(FoodId) VALUES ('%s')", foodId);
        database.execSQL(query);
    }

    public void removeToFavorites(String foodId) {
        SQLiteDatabase database = getReadableDatabase();
        String query = String.format("DELETE FROM Favorites WHERE FoodId='%s'", foodId);
        database.execSQL(query);
    }

    public boolean isFavorites(String foodId) {
        SQLiteDatabase database = getReadableDatabase();
        String query = String.format("SELECT * FROM Favorites WHERE FoodId='%s'", foodId);
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public void clearFavorites() {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM Favorites");
        db.execSQL(query);
    }

    @SuppressLint("Range")
    public List<Statictical> getStatis() {
        SQLiteDatabase db = getReadableDatabase();
        String sqlSelect = "SELECT * FROM Statistical";
        Cursor cursor = db.rawQuery(sqlSelect, null);
        final List<Statictical> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                result.add(new Statictical(cursor.getString(cursor.getColumnIndex("Day")),
                        cursor.getString(cursor.getColumnIndex("Month")),
                        cursor.getString(cursor.getColumnIndex("Year")),
                        cursor.getString(cursor.getColumnIndex("Total"))));
            } while (cursor.moveToNext());
        }
        return result;
    }
    public void addToStatis(Statictical statictical) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO Statistical(Day,Month,Year,Total) VALUES ('%s','%s','%s','%s');",
                statictical.getDay(),
                statictical.getMonth(),
                statictical.getYear(),
                statictical.getTotal());
        db.execSQL(query);
    }
    public void cleanStatis() {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM Statistical");
        db.execSQL(query);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create_table_carts = String.format("CREATE TABLE OderDetail(ID INTEGER PRIMARY KEY,ProductId TEXT,ProductName TEXT,Quantity TEXT,Price TEXT,Discount TEXT)");
        String create_table_fav = String.format("CREATE TABLE Favorites(FoodId TEXT PRIMARY KEY)");
        String create_table_statis = String.format("CREATE TABLE Statistical(ID INTEGER PRIMARY KEY,Day TEXT,Month TEXT,Year TEXT,Total TEXT)");


        sqLiteDatabase.execSQL(create_table_carts);
        sqLiteDatabase.execSQL(create_table_fav);
        sqLiteDatabase.execSQL(create_table_statis);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String drop_carts_table = String.format("DROP TABLE IF EXISTS OderDetail");
        String drop_carts_fav = String.format("DROP TABLE IF EXISTS Favorites");
        String drop_carts_statis = String.format("DROP TABLE IF EXISTS Statistical");


        sqLiteDatabase.execSQL(drop_carts_table);
        sqLiteDatabase.execSQL(drop_carts_fav);
        sqLiteDatabase.execSQL(drop_carts_statis);


        onCreate(sqLiteDatabase);
    }
}
