package com.ratajczykdev.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ratajczykdev.inventoryapp.data.ProductContract.ProductEntry;

public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>
{

    /**
     * Floating action button for adding a new product
     */
    private FloatingActionButton fabNewProduct;

    /**
     * Adapter for list view
     */
    private ProductCursorAdapter productCursorAdapter;

    /**
     * Identifier for product data loader
     */
    private static final int PRODUCT_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        fabNewProduct = (FloatingActionButton) findViewById(R.id.fab_new_product);

        fabNewProduct.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(CatalogActivity.this, DetailActivity.class);
                startActivity(intent);
            }
        });

        //  loader will provide cursor, so now pass null for cursor
        productCursorAdapter = new ProductCursorAdapter(this, null);

        ListView viewProductList = (ListView) findViewById(R.id.product_list);
        View viewEmptyHint = (View) findViewById(R.id.empty_view_hint);
        viewProductList.setEmptyView(viewEmptyHint);
        viewProductList.setAdapter(productCursorAdapter);
        viewProductList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                //  TODO: make it open with data from selected product
                startActivity(new Intent(CatalogActivity.this, DetailActivity.class));
            }
        });

        //  start loader
        getLoaderManager().initLoader(PRODUCT_LOADER_ID, null, this);

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
        //  TODO: correct photo storing
        contentValues.put(ProductEntry.COLUMN_PRODUCT_PHOTO, "Nice photo");
        contentValues.put(ProductEntry.COLUMN_PRODUCT_PRICE, 1580);
        contentValues.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, 35);

        Uri newUri = getContentResolver().insert(ProductEntry.CONTENT_URI, contentValues);
        Toast.makeText(this, newUri.toString(), Toast.LENGTH_SHORT).show();
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
}
