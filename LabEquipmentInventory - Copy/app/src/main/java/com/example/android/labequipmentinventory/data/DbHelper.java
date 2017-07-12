package com.example.android.labequipmentinventory.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.labequipmentinventory.data.Contract.EquipmentEntry;


/**
 * Created by ftf-icn on 4/21/2017.
 */
public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "lab.db";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + EquipmentEntry.TABLE_NAME;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ENTRIES = "CREATE TABLE " + EquipmentEntry.TABLE_NAME + "("
                + EquipmentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + EquipmentEntry.COLUMN_NAME + " TEXT," + EquipmentEntry.COLUMN_QUANTITY
                + " TEXT," + EquipmentEntry.COLUMN_MAIL + " TEXT)";


        db.execSQL(SQL_CREATE_ENTRIES);

        ContentValues values = new ContentValues();

        values.put(EquipmentEntry.COLUMN_NAME, "Chips");
        values.put(EquipmentEntry.COLUMN_QUANTITY, 5);
        values.put(EquipmentEntry.COLUMN_MAIL, "Lays supplier");
        db.insert(EquipmentEntry.TABLE_NAME, null, values);

        values.put(EquipmentEntry.COLUMN_NAME, "Pepsi");
        values.put(EquipmentEntry.COLUMN_QUANTITY, 10);
        values.put(EquipmentEntry.COLUMN_MAIL, "Pepsi supplier");
        db.insert(EquipmentEntry.TABLE_NAME, null, values);

        values.put(EquipmentEntry.COLUMN_NAME, "Toilet paper");
        values.put(EquipmentEntry.COLUMN_QUANTITY, 10);
        values.put(EquipmentEntry.COLUMN_MAIL, "Toilet paper supplier");
        db.insert(EquipmentEntry.TABLE_NAME, null, values);

        values.put(EquipmentEntry.COLUMN_NAME, "Juice");
        values.put(EquipmentEntry.COLUMN_QUANTITY, 10);
        values.put(EquipmentEntry.COLUMN_MAIL, "Juice supplier");
        db.insert(EquipmentEntry.TABLE_NAME, null, values);

        values.put(EquipmentEntry.COLUMN_NAME, "Bread");
        values.put(EquipmentEntry.COLUMN_QUANTITY, 20);
        values.put(EquipmentEntry.COLUMN_MAIL, "Bread supplier");
        db.insert(EquipmentEntry.TABLE_NAME, null, values);

        values.put(EquipmentEntry.COLUMN_NAME, "Pasta");
        values.put(EquipmentEntry.COLUMN_QUANTITY, 20);
        values.put(EquipmentEntry.COLUMN_MAIL, "Pasta supplier");
        db.insert(EquipmentEntry.TABLE_NAME, null, values);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}

