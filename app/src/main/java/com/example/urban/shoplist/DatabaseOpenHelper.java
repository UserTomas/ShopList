package com.example.urban.shoplist;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by urban on 28. 9. 2017.
 */

public class DatabaseOpenHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "shop_Database";
    private final String DATABASE_TABLE = "items";
    private final String NAME = "name";
    private final String PRICE = "price";
    private final String COUNT = "count";

    public DatabaseOpenHelper(Context context) {
        // Context, database name, optional cursor factory, database version
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create a new table
        db.execSQL("CREATE TABLE "+DATABASE_TABLE+" (_id INTEGER PRIMARY KEY AUTOINCREMENT, "+NAME+" TEXT, "+PRICE+" REAL,"+COUNT+" REAL);");
        // create sample data
        ContentValues values = new ContentValues();
        values.put(NAME, "Apples");
        values.put(COUNT, 50);
        values.put(PRICE, 3);
        // insert data to database, name of table, "Nullcolumnhack", values
        db.insert(DATABASE_TABLE, null, values);
        // a more data...
        values.put(NAME, "Ham");
        values.put(COUNT, 20);
        values.put(PRICE, 4);
        db.insert(DATABASE_TABLE, null, values);
        values.put(NAME, "Butter");
        values.put(COUNT, 100);
        values.put(PRICE, 1.5);
        db.insert(DATABASE_TABLE, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
        onCreate(db);
    }
}