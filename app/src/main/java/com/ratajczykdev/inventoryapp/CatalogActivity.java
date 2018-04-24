package com.ratajczykdev.inventoryapp;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.ratajczykdev.inventoryapp.data.ProductContract.ProductEntry;

public class CatalogActivity extends AppCompatActivity
{

    /**
     * Floating action button for adding a new product
     */
    private FloatingActionButton fabNewProduct;

    /**
     * Adapter for list view
     */
    private ProductCursorAdapter productCursorAdapter;

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

        // TODO: set OnClickListener for product list view


    }

    /**
     * Helper method to insert hardcoded product data into the database.
     *
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
    }
}
