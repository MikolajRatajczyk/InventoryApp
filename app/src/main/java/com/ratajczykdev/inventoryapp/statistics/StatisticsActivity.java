package com.ratajczykdev.inventoryapp.statistics;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.ratajczykdev.inventoryapp.R;
import com.ratajczykdev.inventoryapp.data.ProductContract;
import com.ratajczykdev.inventoryapp.data.ProductContract.ProductEntry;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Shows statistics data about products in numbers
 *
 * @author Mikołaj Ratajczyk <mikolaj.ratajczyk@gmail.com>
 */
public class StatisticsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>
{
    private TextView textViewProductsNumber;
    private TextView textViewMaxPrice;
    private TextView textViewMinPrice;
    private FloatingActionButton fabGraphs;

    private Cursor productsCursor;

    private static final int PRODUCTS_LOADER_ID = 0;
    private static final int PRODUCTS_NUMBER_IF_ERROR = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        setUiReferences();
        setUiListeners();
        animateFabGraphs();
        startProductsLoader();
    }

    private void setUiReferences()
    {
        textViewProductsNumber = findViewById(R.id.activity_statistics_products_number_textview);
        textViewMaxPrice = findViewById(R.id.activity_statistics_max_price_textview);
        textViewMinPrice = findViewById(R.id.activity_statistics_min_price_textview);
        fabGraphs = findViewById(R.id.activity_statistics_graphs_fab);
    }

    private void setUiListeners()
    {
        fabGraphs.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(StatisticsActivity.this, GraphsActivity.class);
                //  add statistics Map to Intent for GraphActivity
                intent.putExtra(StatisticsContract.STATISTICS_MAP_NAME, getStatisticsMap());
                startActivity(intent);
            }
        });
    }

    private HashMap<String, Float> getStatisticsMap()
    {
        //  TODO: add more data
        HashMap<String, Float> statisticsMap = new HashMap<>();
        statisticsMap.put(StatisticsContract.PRODUCTS_NUMBER_KEY, (float) getProductsNumber());
        statisticsMap.put(StatisticsContract.PRODUCTS_MAX_PRICE_KEY, getProductsMaxPrice());
        statisticsMap.put(StatisticsContract.PRODUCTS_MIN_PRICE_KEY, getProductsMinPrice());
        return statisticsMap;
    }

    private void startProductsLoader()
    {
        getLoaderManager().initLoader(PRODUCTS_LOADER_ID, null, this);
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
        updateStatisticsNumbersInUi();
    }

    private void updateStatisticsNumbersInUi()
    {
        if (productsCursor == null || productsCursor.getCount() < 1)
        {
            Log.e(StatisticsActivity.class.getSimpleName(), "Error with loading products data from database");
        } else if (productsCursor.moveToFirst())
        {
            updateProductsNumberInUi();
            updateMaxPriceInUi();
            updateMinPriceInUi();
        }
    }

    private void updateProductsNumberInUi()
    {
        String productsNumberString = String.valueOf(getProductsNumber());
        textViewProductsNumber.setText(productsNumberString);
    }

    private int getProductsNumber()
    {
        if (productsCursor != null)
        {
            return productsCursor.getCount();
        } else
        {
            return PRODUCTS_NUMBER_IF_ERROR;
        }
    }

    private void updateMaxPriceInUi()
    {
        String maximumPriceString = String.valueOf(getProductsMaxPrice());
        textViewMaxPrice.setText(maximumPriceString);
    }

    private float getProductsMaxPrice()
    {
        Set<Float> pricesSet = getPricesSet();
        return Collections.max(pricesSet);
    }

    private Set<Float> getPricesSet()
    {
        Set<Float> pricesSet = new HashSet<Float>();
        do
        {
            int priceColumnIndex = productsCursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
            float price = productsCursor.getFloat(priceColumnIndex);
            pricesSet.add(price);
        } while (productsCursor.moveToNext());

        productsCursor.moveToFirst();

        return pricesSet;
    }

    private void updateMinPriceInUi()
    {
        String minimalPriceString = String.valueOf(getProductsMinPrice());
        textViewMinPrice.setText(minimalPriceString);
    }

    private float getProductsMinPrice()
    {
        Set<Float> pricesSet = getPricesSet();
        return Collections.min(pricesSet);
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

    private void animateFabGraphs()
    {
        fabGraphs.animate()
                .rotation(360)
                .setInterpolator(AnimationUtils.loadInterpolator(StatisticsActivity.this, android.R.interpolator.accelerate_decelerate))
                .setDuration(700)
                .start();
    }
}
