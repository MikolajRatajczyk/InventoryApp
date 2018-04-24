package com.ratajczykdev.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

/**
 * {@link ProductCursorAdapter} is an adapter for list or grid view
 * that uses a {@link Cursor} of product data as its data source.
 */
public class ProductCursorAdapter extends CursorAdapter
{
    /**
     * Constructs a new {@link ProductCursorAdapter}
     *
     * @param context app context
     * @param cursor  data source cursor
     */
    public ProductCursorAdapter(Context context, Cursor cursor)
    {
        super(context, cursor, 0);
    }

    /**
     * Makes a new blank list item view.
     *
     * @param context app context
     * @param cursor  data source cursor, moved to the correct position
     * @param parent  view to which the new view is attached to
     * @return newly created item view
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        return null;
    }

    /**
     * This method binds the product data from cursor to the given item layout
     *
     * @param view    existing view, returned by newView method
     * @param context app context
     * @param cursor  data source cursor, moved to the correct position
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {

    }
}
