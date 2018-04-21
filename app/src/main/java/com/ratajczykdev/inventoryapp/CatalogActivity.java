package com.ratajczykdev.inventoryapp;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class CatalogActivity extends AppCompatActivity
{

    /**
     * Floating action button for adding a new product
     */
    private FloatingActionButton fabNewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        fabNewButton = (FloatingActionButton)findViewById(R.id.fab_new_product);
    }
}
