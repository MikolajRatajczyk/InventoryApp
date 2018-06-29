package com.ratajczykdev.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ratajczykdev.inventoryapp.data.ImageHelper;
import com.ratajczykdev.inventoryapp.data.ProductContract.ProductEntry;

import java.util.Locale;

/**
 * {@link ProductCursorAdapter} is an adapter for list or grid view
 * that uses a {@link Cursor} of product data as its data source.
 *
 * @author Mikołaj Ratajczyk
 */
public class ProductCursorAdapter extends CursorAdapter
{
    private TextView textName;
    private ImageView imagePhoto;
    private TextView textPrice;
    private TextView textQuantity;


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
        setLayoutElementsReferences(view);

        String name = getNameFromCursor(cursor);
        textName.setText(name);

        float price = getPriceFromCursor(cursor);
        textPrice.setText(String.format(Locale.US, "%.2f", price));

        int quantity = getQuantityFromCursor(cursor);
        textQuantity.setText(String.valueOf(quantity));

        Uri photoUri = getPhotoUriFromCursor(cursor);
        imagePhoto.setImageBitmap(ImageHelper.getBitmapFromUri(photoUri, context));
    }

    private void setLayoutElementsReferences(View view)
    {
        textName = view.findViewById(R.id.text_name);
        imagePhoto = view.findViewById(R.id.image_photo);
        textPrice = view.findViewById(R.id.text_price);
        textQuantity = view.findViewById(R.id.text_quantity);
    }

    private String getNameFromCursor(Cursor cursor)
    {
        int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        return cursor.getString(nameColumnIndex);
    }

    private float getPriceFromCursor(Cursor cursor)
    {
        int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        return cursor.getFloat(priceColumnIndex);
    }

    private int getQuantityFromCursor(Cursor cursor)
    {
        int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
        return cursor.getInt(quantityColumnIndex);
    }

    private Uri getPhotoUriFromCursor(Cursor cursor)
    {
        int photoUriColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PHOTO_URI);
        String photoUriString = ImageHelper.getStringFromCursor(cursor, photoUriColumnIndex);
        return Uri.parse(photoUriString);
    }
}
