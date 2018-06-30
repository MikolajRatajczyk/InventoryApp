package com.ratajczykdev.inventoryapp.statistics;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.ratajczykdev.inventoryapp.R;
import com.ratajczykdev.inventoryapp.data.ProductContract;
import com.ratajczykdev.inventoryapp.data.ProductContract.ProductEntry;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class StatisticsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>
{
    private TextView textViewProductsNumber;
    private TextView textViewMaxPrice;
    private TextView textViewMinPrice;

    private Cursor productsCursor;

    private static final int PRODUCTS_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        setUiReferences();

        startProductsLoader();
    }

    private void startProductsLoader()
    {
        getLoaderManager().initLoader(PRODUCTS_LOADER_ID, null, this);
    }

    private void updateStatisticsNumbersInUi()
    {
        textViewProductsNumber.setText(String.valueOf(getProductsNumber()));

        //  TODO: implement max price
        textViewMaxPrice.setText("to be implemented");

        String minimalPriceString = String.valueOf(getProductsMinPrice());
        textViewMinPrice.setText(minimalPriceString);
    }

    private void setUiReferences()
    {
        textViewProductsNumber = findViewById(R.id.activity_statistics_products_number_textview);
        textViewMaxPrice = findViewById(R.id.activity_statistics_max_price_textview);
        textViewMinPrice = findViewById(R.id.activity_statistics_min_price_textview);
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
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor)
    {
        productsCursor = cursor;
        setStatisticsData();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        releaseStatisticsData();
    }

    private void releaseStatisticsData()
    {
        textViewProductsNumber.setText("no data");
        textViewMaxPrice.setText("no data");
        textViewMinPrice.setText("no data");
    }

    private void setStatisticsData()
    {
        if (productsCursor == null || productsCursor.getCount() < 1)
        {
            Log.e(StatisticsActivity.class.getSimpleName(), "Error with loading products data from database");
            return;
        }

        if (productsCursor.moveToFirst())
        {
            getProductsNumber();
            updateStatisticsNumbersInUi();
        }

    }

    private int getProductsNumber()
    {
        if (productsCursor != null)
        {
            return productsCursor.getCount();
        } else
        {
            return 0;
        }
    }

    private float getProductsMinPrice()
    {
        //  TODO: make return statement
        //  TODO: change logic in order to not use fix
        //  fix for position (moveToFirst() called already in setStatisticsData())
        Set<Float> pricesSet = getPricesSet();
        //  TODO: delete Log.e
        Log.e("pricesSet: ", pricesSet.toString());
        Log.e("min: ", String.valueOf(Collections.min(pricesSet)));

        return Collections.min(pricesSet);
    }

    private Set<Float> getPricesSet()
    {
        productsCursor.moveToPrevious();
        Set<Float> pricesSet = new HashSet<Float>();
        while (productsCursor.moveToNext())
        {
            int priceColumnIndex = productsCursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
            float price = productsCursor.getFloat(priceColumnIndex);
            //  TODO: delete Log.e
            Log.e("getPriceSet: ", "price: " + String.valueOf(price));
            pricesSet.add(price);
        }
        productsCursor.close();

        return pricesSet;
    }
}
