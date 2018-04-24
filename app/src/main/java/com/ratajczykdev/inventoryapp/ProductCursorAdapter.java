package com.ratajczykdev.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ratajczykdev.inventoryapp.data.ProductContract.ProductEntry;

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
        //  Inflate a list item view
        return LayoutInflater.from(context).inflate(R.layout.product_list_item, parent, false);
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
        TextView textName = (TextView) view.findViewById(R.id.text_name);
        ImageView imagePhoto = (ImageView) view.findViewById(R.id.image_photo);
        TextView textPrice = (TextView) view.findViewById(R.id.text_price);
        TextView textQuantity = (TextView) view.findViewById(R.id.text_quantity);

        int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        int photoColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PHOTO);
        int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);

        String name = cursor.getString(nameColumnIndex);
        //  TODO: correct photo storing in database
        String photo = cursor.getString(photoColumnIndex);
        float price = (float) cursor.getInt(priceColumnIndex) / 100;
        int quantity = cursor.getInt(quantityColumnIndex);

        textName.setText(name);
        // TODO: correct photo storing in database
        imagePhoto.setContentDescription(photo);
        //  TODO: consider using Locale...
        textPrice.setText(String.format("%.2f", price));
        textQuantity.setText(String.valueOf(quantity));
    }
}
