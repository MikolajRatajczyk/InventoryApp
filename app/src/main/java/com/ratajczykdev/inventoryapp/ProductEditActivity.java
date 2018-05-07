package com.ratajczykdev.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ratajczykdev.inventoryapp.data.ProductContract.ProductEntry;

import java.util.Locale;

/**
 * This class allows to edit or add a new product to the database.
 */
public class ProductEditActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>
{
    //  TODO: add adding new product feature
    /**
     * Identifier for product data loader
     */
    private static final int EDITED_PRODUCT_LOADER_ID = 1;
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
     * Cancel button
     */
    private Button buttonCancel;
    /**
     * Save button
     */
    private Button buttonSave;
    /**
     * Content URI for the existing product
     */
    private Uri currentProductUri;

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
        buttonCancel = findViewById(R.id.product_edit_cancel_button);
        buttonSave = findViewById(R.id.product_edit_save_button);

        currentProductUri = getIntent().getData();
        //  edit mode
        if (currentProductUri != null)
        {
            getLoaderManager().initLoader(EDITED_PRODUCT_LOADER_ID, null, this);
            setupButtonsForEditing();
        }

        //  adding a new product mode
        if (currentProductUri == null)
        {
            setupButtonsForAdding();
        }
    }

    /**
     * Setups all buttons in order to edit existing product
     */
    private void setupButtonsForEditing()
    {
        buttonCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (saveExistingProduct())
                {
                    finish();
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (deleteProduct())
                {
                    Intent intent = new Intent(ProductEditActivity.this, CatalogActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    /**
     * Setups all buttons in order to add a new product to database
     */
    private void setupButtonsForAdding()
    {
        buttonDelete.setVisibility(View.INVISIBLE);
    }

    /**
     * Deletes existing product from database
     *
     * @return true if deletion was successful, false if failed
     */
    private boolean deleteProduct()
    {
        int rowsDeleted = getContentResolver().delete(currentProductUri, null, null);
        if (rowsDeleted == 0)
        {
            Toast.makeText(this, "Deleting product failed", Toast.LENGTH_SHORT).show();
            Log.e(ProductEditActivity.class.getSimpleName(), "Deleting product failed for URI: " + currentProductUri.toString());
            return false;
        } else
        {
            Toast.makeText(this, "Product deleted", Toast.LENGTH_SHORT).show();
            return true;
        }
    }


    /**
     * Saves existing product with changes
     *
     * @return true if change was successful, false if failed
     */
    private boolean saveExistingProduct()
    {
        String name = editTextName.getText().toString().trim();
        if (TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Enter correct name", Toast.LENGTH_SHORT).show();
            return false;
        }
        int quantity = Integer.valueOf(editTextQuantity.getText().toString().trim());

        String stringPrice = editTextPrice.getText().toString().trim();
        float floatPrice = Float.valueOf(stringPrice);

        ContentValues contentValues = new ContentValues();
        contentValues.put(ProductEntry.COLUMN_PRODUCT_NAME, name);
        contentValues.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity);
        contentValues.put(ProductEntry.COLUMN_PRODUCT_PRICE, floatPrice);
        //  TODO: add photo

        int rowsAffected = getContentResolver().update(currentProductUri, contentValues, null, null);
        if (rowsAffected == 0)
        {
            Toast.makeText(this, "Saving product failed", Toast.LENGTH_SHORT).show();
            Log.e(ProductEditActivity.class.getSimpleName(), "Saving product failed for URI: " + currentProductUri.toString());
            return false;
        } else
        {
            Toast.makeText(this, "Product saved", Toast.LENGTH_SHORT).show();
            return true;
        }
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
            return;
        }

        if (cursor.moveToFirst())
        {
            int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);

            String name = cursor.getString(nameColumnIndex);
            float price = cursor.getFloat(priceColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);

            editTextName.setText(name);
            editTextPrice.setText(String.format(Locale.US, "%.2f", price));
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
