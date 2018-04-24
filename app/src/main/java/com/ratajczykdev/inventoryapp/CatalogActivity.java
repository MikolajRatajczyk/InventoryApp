package com.ratajczykdev.inventoryapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

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
        //  TODO: make empty view for viewProductList

        viewProductList.setAdapter(productCursorAdapter);

        // TODO: set OnClickListener for product list view


    }
}
