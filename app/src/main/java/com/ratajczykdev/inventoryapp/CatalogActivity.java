package com.ratajczykdev.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ratajczykdev.inventoryapp.data.PhotoConverter;
import com.ratajczykdev.inventoryapp.data.ProductContract.ProductEntry;

/**
 * Loads view with product list
 *
 * @author Mikołaj Ratajczyk
 */
public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>
{
    /**
     * Identifier for product data loader
     */
    private static final int PRODUCT_LOADER_ID = 0;
    /**
     * Floating action button for adding a new product
     */
    private FloatingActionButton fabNewProduct;
    /**
     * Adapter for list view
     */
    private ProductCursorAdapter productCursorAdapter;

    private ListView viewProductList;

    private View viewEmptyHint;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        fabNewProduct = findViewById(R.id.fab_new_product);
        setFabListener();

        //  loader will provide cursor, so now pass null for cursor
        productCursorAdapter = new ProductCursorAdapter(this, null);

        viewProductList = findViewById(R.id.product_list);
        viewEmptyHint = findViewById(R.id.empty_view_hint);

        configureViewProductList();

        startProductLoader();
    }

    private void setFabListener()
    {
        fabNewProduct.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(CatalogActivity.this, ProductEditActivity.class);
                startActivity(intent);
            }
        });
    }

    private void configureViewProductList()
    {
        viewProductList.setEmptyView(viewEmptyHint);
        viewProductList.setAdapter(productCursorAdapter);
        viewProductList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
            {
                Intent intent = new Intent(CatalogActivity.this, ProductDetailActivity.class);
                //  create the content URI that represents the specific product that was clicked on
                Uri currentProductUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id);
                intent.setData(currentProductUri);
                startActivity(intent);
            }
        });
    }

    private void startProductLoader()
    {
        getLoaderManager().initLoader(PRODUCT_LOADER_ID, null, this);
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

        return new CursorLoader(
                this,
                ProductEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor)
    {
        //  update adapter with new data
        productCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        //  when data needs to be deleted
        productCursorAdapter.swapCursor(null);
    }

    /**
     * Helper method to insert hardcoded product data into the database.
     * <p>
     * Only for debugging.
     */
    private void insertFakeProduct()
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ProductEntry.COLUMN_PRODUCT_NAME, "Fast smartphone");

        //  Drawable (PNG) to Bitmap
        Bitmap bitmapPhoto = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.product_photo_placeholder_phone);
        //  Bitmap to byte[]
        byte[] byteArrayPhoto = PhotoConverter.bitmapToByteArray(bitmapPhoto);
        //  put byte[] in contentValues
        contentValues.put(ProductEntry.COLUMN_PRODUCT_PHOTO, byteArrayPhoto);

        contentValues.put(ProductEntry.COLUMN_PRODUCT_PRICE, 15.80);
        contentValues.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, 35);

        Uri newUri = getContentResolver().insert(ProductEntry.CONTENT_URI, contentValues);
        Toast.makeText(this, newUri.toString(), Toast.LENGTH_SHORT).show();
    }
}
