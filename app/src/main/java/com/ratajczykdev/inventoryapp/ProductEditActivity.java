package com.ratajczykdev.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ratajczykdev.inventoryapp.data.ProductContract;
import com.ratajczykdev.inventoryapp.data.ProductContract.ProductEntry;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Locale;

/**
 * This class allows to edit or add a new product to the database
 *
 * @author Mikołaj Ratajczyk
 */
public class ProductEditActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>
{
    /**
     * Identifier for product data loader
     */
    private static final int EDITED_PRODUCT_LOADER_ID = 1;
    /**
     * Request code that identifies photo request from user
     */
    private static final int PHOTO_REQUEST_ID = 2;

    private static final String EMPTY_STRING = "";
    /**
     * Change photo button
     */
    private Button buttonChangePhoto;
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
    /**
     * URI to product photo from user
     */
    private Uri imageUri;

    private String currentName;

    private int currentQuantity;

    private float currentFloatPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);

        setUiElementsReferences();

        setButtonCancelListener();
        setButtonChangePhotoListener();

        if (getIntent().getData() != null)
        {
            currentProductUri = getIntent().getData();
            startProductLoader();
            setupButtonsForEditing();
        } else if (currentProductUri == null)
        {
            setupButtonsForAdding();
        }
    }

    private void setUiElementsReferences()
    {
        buttonChangePhoto = findViewById(R.id.product_edit_change_photo_button);
        editTextName = findViewById(R.id.product_edit_name);
        editTextQuantity = findViewById(R.id.product_edit_quantity);
        editTextPrice = findViewById(R.id.product_edit_price);
        buttonDelete = findViewById(R.id.product_edit_delete_button);
        buttonCancel = findViewById(R.id.product_edit_cancel_button);
        buttonSave = findViewById(R.id.product_edit_save_button);
    }

    private void setButtonCancelListener()
    {
        buttonCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
    }

    private void setButtonChangePhotoListener()
    {
        buttonChangePhoto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                requestBitmapUriFromUser();
            }
        });
    }

    /**
     * Starts activity to receive image URI from user
     * <p>
     * Data will be received by onActivityResult method
     */
    private void requestBitmapUriFromUser()
    {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PHOTO_REQUEST_ID);
    }

    private void startProductLoader()
    {
        getLoaderManager().initLoader(EDITED_PRODUCT_LOADER_ID, null, this);
    }

    /**
     * Setups all buttons in order to edit existing product
     */
    private void setupButtonsForEditing()
    {

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
     * Deletes existing product from database
     *
     * @return true if deletion was successful, false if failed
     */
    private boolean deleteProduct()
    {
        int rowsDeleted = getContentResolver().delete(currentProductUri, null, null);
        if (rowsDeleted == 0)
        {
            Toast.makeText(this, R.string.failed_deleting_product, Toast.LENGTH_SHORT).show();
            Log.e(ProductEditActivity.class.getSimpleName(), "Deleting product failed for URI: " + currentProductUri.toString());
            return false;
        } else
        {
            Toast.makeText(this, R.string.info_deleted_product, Toast.LENGTH_SHORT).show();
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
        if (setCurrentProductDataFromUi())
        {
            ContentValues contentValues = getContentValuesWithProductData();
            return updateProductFromContentValues(contentValues);
        } else
        {
            return false;
        }
    }

    private boolean setCurrentProductDataFromUi()
    {
        currentQuantity = getIntQuantityFromUi();
        currentFloatPrice = getFloatPriceFromUi();
        currentName = editTextName.getText().toString().trim();

        if (TextUtils.isEmpty(currentName))
        {
            Toast.makeText(this, R.string.info_correct_name_enter, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private float getFloatPriceFromUi()
    {
        String stringPrice = editTextPrice.getText().toString().trim();
        if (TextUtils.isEmpty(stringPrice) || stringPrice.equals("."))
        {
            stringPrice = "0";
        }
        return Float.valueOf(stringPrice);
    }

    private int getIntQuantityFromUi()
    {
        String stringQuantity = editTextQuantity.getText().toString().trim();
        if (TextUtils.isEmpty(stringQuantity) || stringQuantity.equals("."))
        {
            stringQuantity = "0";
        }
        return Integer.valueOf(stringQuantity);
    }

    @NonNull
    private ContentValues getContentValuesWithProductData()
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ProductEntry.COLUMN_PRODUCT_NAME, currentName);
        contentValues.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, currentQuantity);
        contentValues.put(ProductEntry.COLUMN_PRODUCT_PRICE, currentFloatPrice);
        putPhotoUriInContentValuesIfExists(contentValues);
        return contentValues;
    }

    private void putPhotoUriInContentValuesIfExists(ContentValues contentValues)
    {
        if (imageUri != null)
        {
            String imageUriString = imageUri.toString();
            contentValues.put(ProductEntry.COLUMN_PRODUCT_PHOTO_URI, imageUriString);
            Toast.makeText(this, "path: " + imageUriString, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Makes InputStream from the photo's URI
     *
     * @param photoUri photo URI
     * @return InputStream with the photo
     */
    private InputStream photoUriToInputStream(Uri photoUri)
    {
        InputStream photoStream = null;
        try
        {
            photoStream = getContentResolver().openInputStream(photoUri);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            Toast.makeText(this, R.string.failed_getting_image, Toast.LENGTH_SHORT).show();
            Log.e(ProductEditActivity.class.getSimpleName(), "Getting image failed for URI: " + photoUri.toString());
        }

        return photoStream;
    }

    private boolean updateProductFromContentValues(ContentValues contentValues)
    {
        int rowsAffected = getContentResolver().update(currentProductUri, contentValues, null, null);
        if (rowsAffected == 0)
        {
            Toast.makeText(this, R.string.failed_saving_product, Toast.LENGTH_SHORT).show();
            Log.e(ProductEditActivity.class.getSimpleName(), "Saving product failed for URI: " + currentProductUri.toString());
            return false;
        } else
        {
            Toast.makeText(this, R.string.info_saved_product, Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    /**
     * Setups all buttons in order to add a new product to database
     */
    private void setupButtonsForAdding()
    {
        buttonDelete.setVisibility(View.INVISIBLE);

        buttonChangePhoto.setText(getString(R.string.add_photo));
        buttonSave.setText(getString(R.string.add));
        buttonSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (addNewProduct())
                {
                    Intent intent = new Intent(ProductEditActivity.this, CatalogActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    /**
     * Adds a new product to database
     *
     * @return true if adding was successful, false if failed
     */
    private boolean addNewProduct()
    {
        if (setCurrentProductDataFromUi())
        {
            ContentValues contentValues = getContentValuesWithProductData();
            return insertProductFromContentValues(contentValues);
        } else
        {
            return false;
        }
    }

    private boolean insertProductFromContentValues(ContentValues contentValues)
    {
        Uri newUri = getContentResolver().insert(ProductEntry.CONTENT_URI, contentValues);
        if (newUri == null)
        {
            Toast.makeText(this, R.string.failed_adding_product, Toast.LENGTH_SHORT).show();
            Log.e(ProductEditActivity.class.getSimpleName(), "Adding a new product failed");
            return false;
        } else
        {
            Toast.makeText(this, R.string.info_added_product, Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    /**
     * This method is called when request finishes in other app
     *
     * @param requestCode request code passed when starting activity
     * @param resultCode  result code
     * @param data        carries the result data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_REQUEST_ID)
        {
            if (resultCode == RESULT_OK && data != null)
            {
                imageUri = data.getData();

            } else
            {
                Toast.makeText(this, R.string.info_not_picked_image, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle)
    {
        String[] projection = ProductContract.FULL_PROJECTION_ARRAY;

        return new CursorLoader(this, currentProductUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor)
    {
        setProductData(cursor);
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
            setNameInUi(cursor);
            setPriceInUi(cursor);
            setQuantityInUi(cursor);
        }
    }

    private void setQuantityInUi(Cursor cursor)
    {
        int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
        int quantity = cursor.getInt(quantityColumnIndex);
        editTextQuantity.setText(String.valueOf(quantity));
    }

    private void setPriceInUi(Cursor cursor)
    {
        int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        float price = cursor.getFloat(priceColumnIndex);
        editTextPrice.setText(String.format(Locale.US, "%.2f", price));
    }

    private void setNameInUi(Cursor cursor)
    {
        int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        String name = cursor.getString(nameColumnIndex);
        editTextName.setText(name);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        releaseProductData();
    }

    /**
     * Replace current product data with blank space
     */
    private void releaseProductData()
    {
        editTextName.setText(EMPTY_STRING);
        editTextQuantity.setText(EMPTY_STRING);
        editTextPrice.setText(EMPTY_STRING);
    }
}
