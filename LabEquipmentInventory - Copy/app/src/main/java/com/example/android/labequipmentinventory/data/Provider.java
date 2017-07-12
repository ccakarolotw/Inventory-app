package com.example.android.labequipmentinventory.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.android.labequipmentinventory.data.Contract.EquipmentEntry;

public class Provider extends ContentProvider {

    public static final String LOG_TAG = Provider.class.getSimpleName();
    private static final int EQUIPMENTS = 100;
    private static final int EQUIPMENTS_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH, EQUIPMENTS);
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH + "/#", EQUIPMENTS_ID);
    }

    private DbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new DbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case EQUIPMENTS:
                cursor = database.query(EquipmentEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case EQUIPMENTS_ID:
                selection = EquipmentEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(EquipmentEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EQUIPMENTS:
                return insertEquipment(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertEquipment(Uri uri, ContentValues values) {
        String name = values.getAsString(EquipmentEntry.COLUMN_NAME);
        if (name == null) {
            throw new IllegalArgumentException("The input needs a name");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(EquipmentEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EQUIPMENTS:
                return updateEquipment(uri, contentValues, selection, selectionArgs);
            case EQUIPMENTS_ID:
                selection = EquipmentEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateEquipment(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }


    private int updateEquipment(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(EquipmentEntry.COLUMN_NAME)) {
            String name = values.getAsString(EquipmentEntry.COLUMN_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Pet requires a name");
            }
        }

        if (values.containsKey(EquipmentEntry.COLUMN_MAIL)) {
            String mail = values.getAsString(EquipmentEntry.COLUMN_MAIL);
        }

        if (values.containsKey(EquipmentEntry.COLUMN_QUANTITY)) {
            Integer quantity = values.getAsInteger(EquipmentEntry.COLUMN_QUANTITY);
            if (quantity != null && quantity < 0) {
                throw new IllegalArgumentException("Insert a quantity > 0");
            }
        }

        if (values.size() == 0) {
            return 0;
        }
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(EquipmentEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsDeleted;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EQUIPMENTS:
                rowsDeleted = database.delete(EquipmentEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case EQUIPMENTS_ID:
                selection = EquipmentEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(EquipmentEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EQUIPMENTS:
                return EquipmentEntry.CONTENT_LIST_TYPE;
            case EQUIPMENTS_ID:
                return EquipmentEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
