package com.example.android.labequipmentinventory;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.labequipmentinventory.data.Contract;
import com.example.android.labequipmentinventory.data.Contract.EquipmentEntry;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER = 0;

    private EquipmentCursorAdapter mCursorAdapter;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        ListView equipmentListView = (ListView) findViewById(R.id.list);

        View emptyView = findViewById(R.id.empty_view);
        equipmentListView.setEmptyView(emptyView);

        mCursorAdapter = new EquipmentCursorAdapter(this, null);
        equipmentListView.setAdapter(mCursorAdapter);


        equipmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long Id) {
                final Uri currentUri = ContentUris.withAppendedId(EquipmentEntry.CONTENT_URI, Id);

                final TextView saleTextView = (TextView) view.findViewById(R.id.sale);
                saleTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String[] projection = {EquipmentEntry._ID, EquipmentEntry.COLUMN_NAME, EquipmentEntry.COLUMN_QUANTITY};
                        Cursor cursor = getContentResolver().query(currentUri, projection, null, null, null);
                        int quantityColumnIndex = cursor.getColumnIndex(Contract.EquipmentEntry.COLUMN_QUANTITY);

                        if (cursor.moveToFirst()) {
                            int Quantity = cursor.getInt(quantityColumnIndex);
                            if (Quantity > 0) {
                                ContentValues values = new ContentValues();
                                values.put(EquipmentEntry.COLUMN_QUANTITY, Quantity - 1);
                                int rowsAffected = getContentResolver().update(currentUri, values, null, null);
                            }
                        }
                    }
                });

                View wrapperView = view.findViewById(R.id.item_wrapper);
                wrapperView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                        intent.setData(currentUri);
                        startActivity(intent);
                    }
                });
            }

        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        getLoaderManager().initLoader(LOADER, null, this);

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {EquipmentEntry._ID, EquipmentEntry.COLUMN_NAME, EquipmentEntry.COLUMN_QUANTITY, EquipmentEntry.COLUMN_MAIL};

        return new CursorLoader(this, EquipmentEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Main Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://com.example.android.labequipmentinventory/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Main Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://com.example.android.labequipmentinventory/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}