package com.example.android.labequipmentinventory;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.labequipmentinventory.data.Contract;
import com.example.android.labequipmentinventory.data.Contract.EquipmentEntry;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_LOADER = 0;
    private Uri mCurrentUri;
    private EditText mNameEditText;
    private EditText mQuantityEditText;
    private EditText mEmailEditText;
    private TextView mOrderTextView;
    private TextView mIncreaseTextView;
    private TextView mDecreaseTextView;
    private boolean mHasChanged = false;
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mHasChanged = true;
            return false;
        }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        mCurrentUri = intent.getData();

        final String[] projection = {EquipmentEntry._ID, EquipmentEntry.COLUMN_NAME, EquipmentEntry.COLUMN_QUANTITY, EquipmentEntry.COLUMN_MAIL};

        mNameEditText = (EditText) findViewById(R.id.item_name);
        mQuantityEditText = (EditText) findViewById(R.id.edit_quantity);
        mEmailEditText = (EditText) findViewById(R.id.email);
        mIncreaseTextView = (TextView) findViewById(R.id.increase);
        mDecreaseTextView = (TextView) findViewById(R.id.decrease);
        mOrderTextView = (TextView) findViewById(R.id.order);
        mNameEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
        mEmailEditText.setOnTouchListener(mTouchListener);


        if (mCurrentUri == null) {

            setTitle(getString(R.string.editor_activity_title_new));
            invalidateOptionsMenu();
            mOrderTextView.setVisibility(View.GONE);
            mIncreaseTextView.setVisibility(View.GONE);
            mDecreaseTextView.setVisibility(View.GONE);

        } else {
            setTitle(getString(R.string.editor_activity_title_edit));
            getLoaderManager().initLoader(EXISTING_LOADER, null, this);

        }

        mIncreaseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cursor cursor = getContentResolver().query(mCurrentUri, projection, null, null, null);

                int quantityColumnIndex = cursor.getColumnIndex(Contract.EquipmentEntry.COLUMN_QUANTITY);

                if (cursor.moveToFirst()) {
                    int Quantity = cursor.getInt(quantityColumnIndex);
                    ContentValues values = new ContentValues();
                    values.put(EquipmentEntry.COLUMN_QUANTITY, Quantity + 1);
                    int rowsAffected = getContentResolver().update(mCurrentUri, values, null, null);
                }
            }
        });


        mDecreaseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cursor cursor = getContentResolver().query(mCurrentUri, projection, null, null, null);

                int quantityColumnIndex = cursor.getColumnIndex(Contract.EquipmentEntry.COLUMN_QUANTITY);

                if (cursor.moveToFirst()) {
                    int Quantity = cursor.getInt(quantityColumnIndex);
                    ContentValues values = new ContentValues();

                    if (Quantity > 0) {
                        values.put(EquipmentEntry.COLUMN_QUANTITY, Quantity - 1);
                        int rowsAffected = getContentResolver().update(mCurrentUri, values, null, null);
                    }

                }
            }
        });

        mOrderTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cursor cursor = getContentResolver().query(mCurrentUri, projection, null, null, null);


                int mailColumnIndex = cursor.getColumnIndex(EquipmentEntry.COLUMN_MAIL);

                if (cursor.moveToFirst()) {
                    String Mail = cursor.getString(mailColumnIndex);
                    //mOrderTextView.setText(Mail);
                    Intent intentsms = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + Mail));
                    intentsms.putExtra("sms_body", "Hi, we would like to order...");
                    startActivity(intentsms);
                }

            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onBackPressed() {
        if (!mHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };

        showUnsavedChangesDialog(discardButtonClickListener);
    }


    private void saveEquipment() {

        String nameString = mNameEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        String mailString = mEmailEditText.getText().toString().trim();


        if (mCurrentUri == null &&
                TextUtils.isEmpty(nameString) &&
                TextUtils.isEmpty(quantityString)) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(EquipmentEntry.COLUMN_NAME, nameString);
        values.put(EquipmentEntry.COLUMN_MAIL,mailString);
        int quantity = 1;
        if (!TextUtils.isEmpty(quantityString)) {
            quantity = Integer.parseInt(quantityString);
        }

        values.put(EquipmentEntry.COLUMN_QUANTITY, quantity);

        if (mCurrentUri == null) {

            Uri newUri = getContentResolver().insert(EquipmentEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, getString(R.string.editor_insert_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_insert_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {

            int rowsAffected = getContentResolver().update(mCurrentUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.editor_update_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_update_successful),
                        Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (mCurrentUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_save:
                saveEquipment();
                finish();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!mHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                EquipmentEntry._ID,
                EquipmentEntry.COLUMN_NAME,
                EquipmentEntry.COLUMN_QUANTITY,
                EquipmentEntry.COLUMN_MAIL

        };

        return new CursorLoader(this,
                mCurrentUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(EquipmentEntry.COLUMN_NAME);
            int mailColumnIndex = cursor.getColumnIndex(EquipmentEntry.COLUMN_MAIL);
            int quantityColumnIndex = cursor.getColumnIndex(EquipmentEntry.COLUMN_QUANTITY);

            String name = cursor.getString(nameColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            String email = cursor.getString(mailColumnIndex);

            mNameEditText.setText(name);
            mQuantityEditText.setText(Integer.toString(quantity));
            mEmailEditText.setText(email);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteEquipment();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void deleteEquipment() {

        if (mCurrentUri != null) {

            int rowsDeleted = getContentResolver().delete(mCurrentUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_delete_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Editor Page",
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
                "Editor Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://com.example.android.labequipmentinventory/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}