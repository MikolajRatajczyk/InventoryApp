package com.ratajczykdev.inventoryapp;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ratajczykdev.inventoryapp.data.ProductContract.ProductEntry;

//  TODO: finish
public class ProductEditActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>
{
    /**
     * Add photo button
     */
    private Button buttonAddPhoto;

    /**
     * Name EditText
     */
    private EditText editTextName;

    /**
     * Quantity EditText
     */
    private EditText editTextQuantity;

    /**
     * Price EditText
     */
    private EditText editTextPrice;

    /**
     * Delete button
     */
    private Button buttonDelete;

    /**
     * Dismiss button
     */
    private Button buttonDismiss;

    /**
     * Add button
     */
    private Button buttonAdd;

    /**
     * Content URI for the existing product
     */
    private Uri currentProductUri;

    private static final int EDITED_PRODUCT_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);

        if (getIntent() != null)
        {
            currentProductUri = getIntent().getData();
            getLoaderManager().initLoader(EDITED_PRODUCT_LOADER_ID, null, this);
        }
        //  TODO: finish whole editing behaviour

        buttonAddPhoto=findViewById(R.id.product_edit_add_photo_button);
        editTextName=findViewById(R.id.product_edit_name);
        editTextQuantity=findViewById(R.id.product_edit_quantity);
        editTextPrice=findViewById(R.id.product_edit_price);
        buttonDelete=findViewById(R.id.product_edit_delete_button);
        buttonDismiss=findViewById(R.id.product_edit_dismiss_button);
        buttonAdd=findViewById(R.id.product_edit_add_button);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle)
    {
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PHOTO,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QUANTITY};

        return new CursorLoader(this, currentProductUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor)
    {
        //  TODO: fill
        //  TODO: delete debugging Toast
        Toast.makeText(this, currentProductUri.toString() + "->" + cursor.toString(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        //  TODO: fill
    }
}
