package com.ratajczykdev.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ratajczykdev.inventoryapp.data.ProductContract;
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
    TextView textName;
    ImageView imagePhoto;
    TextView textPrice;
    TextView textQuantity;


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

        //  TODO: extract to new methods
        String name = getNameFromCursor(cursor);
        textName.setText(name);

        int photoColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PHOTO);
        if (!(cursor.isNull(photoColumnIndex)))
        {
            // get photo as byte array
            byte[] byteArrayPhoto = cursor.getBlob(photoColumnIndex);
            //  convert byte array to Bitmap
            Bitmap photo = BitmapFactory.decodeByteArray(byteArrayPhoto, 0, byteArrayPhoto.length);
            imagePhoto.setImageBitmap(photo);
        }

        float price = getPriceFromCursor(cursor);
        textPrice.setText(String.format(Locale.US, "%.2f", price));

        int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
        int quantity = cursor.getInt(quantityColumnIndex);
        textQuantity.setText(String.valueOf(quantity));
    }

    private float getPriceFromCursor(Cursor cursor)
    {
        int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        return cursor.getFloat(priceColumnIndex);
    }

    private String getNameFromCursor(Cursor cursor)
    {
        int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        return cursor.getString(nameColumnIndex);
    }

    private void setLayoutElementsReferences(View view)
    {
        textName = view.findViewById(R.id.text_name);
        imagePhoto = view.findViewById(R.id.image_photo);
        textPrice = view.findViewById(R.id.text_price);
        textQuantity = view.findViewById(R.id.text_quantity);
    }
}
