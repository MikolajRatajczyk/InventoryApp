package com.ratajczykdev.inventoryapp;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ratajczykdev.inventoryapp.data.ProductContract.ProductEntry;

import java.util.Locale;

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
     * Save button
     */
    private Button buttonSave;

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

        buttonAddPhoto = findViewById(R.id.product_edit_add_photo_button);
        editTextName = findViewById(R.id.product_edit_name);
        editTextQuantity = findViewById(R.id.product_edit_quantity);
        editTextPrice = findViewById(R.id.product_edit_price);
        buttonDelete = findViewById(R.id.product_edit_delete_button);
        buttonDismiss = findViewById(R.id.product_edit_dismiss_button);
        buttonSave = findViewById(R.id.product_edit_save_button);

        if (getIntent() != null)
        {
            currentProductUri = getIntent().getData();
            getLoaderManager().initLoader(EDITED_PRODUCT_LOADER_ID, null, this);
        }

        buttonDismiss.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
        //  TODO: finish whole editing behaviour
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
        setProductData(cursor);
        //  TODO: delete debugging Toast
        Toast.makeText(this, currentProductUri.toString() + "->" + cursor.toString(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        releaseProductData();
    }

    /**
     * Set current product data with provided ones from database
     *
     * @param cursor cursor that contains the existing product data
     */
    private void setProductData(Cursor cursor)
    {
        if (cursor == null || cursor.getCount() < 1)
        {
            Log.e(ProductEditActivity.class.getSimpleName(), "Error with loading existing product data from database");
            Toast.makeText(this, "Error with loading product", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cursor.moveToFirst())
        {
            int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);

            String name = cursor.getString(nameColumnIndex);
            float price = (float) cursor.getInt(priceColumnIndex) / 100;
            int quantity = cursor.getInt(quantityColumnIndex);

            editTextName.setText(name);
            // get current Locale in order to set proper decimal sign
            Locale currentLocale = getResources().getConfiguration().locale;
            editTextPrice.setText(String.format(currentLocale, "%.2f", price));
            editTextQuantity.setText(String.valueOf(quantity));
        }
    }

    /**
     * Replace current product data with blank space
     */
    private void releaseProductData()
    {
        editTextName.setText("");
        editTextQuantity.setText("");
        editTextPrice.setText("");
    }
}
