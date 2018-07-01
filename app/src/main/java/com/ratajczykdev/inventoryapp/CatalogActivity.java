package com.ratajczykdev.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ratajczykdev.inventoryapp.data.ImageHelper;
import com.ratajczykdev.inventoryapp.data.ProductContract;
import com.ratajczykdev.inventoryapp.data.ProductContract.ProductEntry;
import com.ratajczykdev.inventoryapp.statistics.StatisticsActivity;

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

    //  TODO: change entire behaviour to RecyclerView
    private ListView viewProductList;

    private View viewEmptyHint;

    private String loaderSqlSortOrder = null;

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

        animateFabNewProduct();
    }

    /**
     * Modify App Bar to have actions
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_catalog_appbar_actions, menu);
        return true;
    }

    /**
     * Triggers methods for the specified action on the app bar
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int currentItemId = item.getItemId();
        if (currentItemId == R.id.activity_catalog_appbar_actions_sort_by_name)
        {
            loaderSqlSortOrder = ProductEntry.COLUMN_PRODUCT_NAME + " ASC";
            getLoaderManager().restartLoader(PRODUCT_LOADER_ID, null, this);
        } else if (currentItemId == R.id.activity_catalog_appbar_actions_sort_by_price)
        {
            loaderSqlSortOrder = ProductEntry.COLUMN_PRODUCT_PRICE + " DESC";
            getLoaderManager().restartLoader(PRODUCT_LOADER_ID, null, this);
        } else if (currentItemId == R.id.activity_catalog_appbar_actions_sort_by_id)
        {
            loaderSqlSortOrder = ProductEntry._ID + " ASC";
            getLoaderManager().restartLoader(PRODUCT_LOADER_ID, null, this);
        } else if (currentItemId == R.id.activity_catalog_appbar_actions_sort_by_quantity)
        {
            loaderSqlSortOrder = ProductEntry.COLUMN_PRODUCT_QUANTITY + " DESC";
            getLoaderManager().restartLoader(PRODUCT_LOADER_ID, null, this);
        } else if (currentItemId == R.id.activity_catalog_appbar_actions_statistics)
        {
            Intent intent = new Intent(CatalogActivity.this, StatisticsActivity.class);
            startActivity(intent);
        } else if (currentItemId == R.id.activity_catalog_appbar_actions_about)
        {
            Intent intent = new Intent(CatalogActivity.this, AboutActivity.class);
            startActivity(intent);
        }
        return true;
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
        String[] projection = ProductContract.FULL_PROJECTION_ARRAY;

        return new CursorLoader(
                this,
                ProductEntry.CONTENT_URI,
                projection,
                null,
                null,
                loaderSqlSortOrder);
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

    private void animateFabNewProduct()
    {
        fabNewProduct.animate()
                .rotation(360)
                .setInterpolator(AnimationUtils.loadInterpolator(CatalogActivity.this, android.R.interpolator.accelerate_decelerate))
                .setDuration(700)
                .start();
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

        Uri productPlaceholderImageUri = ImageHelper.getUriForResourceId(R.drawable.product_photo_placeholder_phone, getApplicationContext());
        String productPlaceHolderImageUriString = productPlaceholderImageUri.toString();
        contentValues.put(ProductEntry.COLUMN_PRODUCT_PHOTO_URI, productPlaceHolderImageUriString);

        contentValues.put(ProductEntry.COLUMN_PRODUCT_PRICE, 15.80);
        contentValues.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, 35);

        Uri newUri = getContentResolver().insert(ProductEntry.CONTENT_URI, contentValues);
        Toast.makeText(this, newUri.toString(), Toast.LENGTH_SHORT).show();
    }
}
