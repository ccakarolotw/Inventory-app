package com.example.android.labequipmentinventory;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.labequipmentinventory.data.Contract;

public class EquipmentCursorAdapter extends CursorAdapter {


    public EquipmentCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView summaryTextView = (TextView) view.findViewById(R.id.summary);

        int nameColumnIndex = cursor.getColumnIndex(Contract.EquipmentEntry.COLUMN_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(Contract.EquipmentEntry.COLUMN_QUANTITY);

        int Quantity = cursor.getInt(quantityColumnIndex);
        String Name = cursor.getString(nameColumnIndex);

        nameTextView.setText(Name);
        summaryTextView.setText("Stock: " + String.valueOf(Quantity));

    }
}