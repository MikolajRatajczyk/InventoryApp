package com.ratajczykdev.inventoryapp.statistics;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.ratajczykdev.inventoryapp.R;

import java.util.HashMap;

/**
 * Shows graphs with statistics data about products
 *
 * @author Mikołaj Ratajczyk <mikolaj.ratajczyk@gmail.com>
 */
public class GraphsActivity extends AppCompatActivity
{
    //  TODO: make more usable graphs

    private GraphView graphProductsNumber;
    private GraphView graphMaxMinPrice;
    private HashMap<String, Float> statistics;
    private float productsNumber;
    private float productsMaxPrice;
    private float productsMinPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs);

        setUiReferences();

        if (getIntent().getSerializableExtra(StatisticsContract.STATISTICS_MAP_NAME) != null)
        {
            Intent intent = getIntent();
            statistics = (HashMap<String, Float>) intent.getSerializableExtra(StatisticsContract.STATISTICS_MAP_NAME);
            productsNumber = statistics.get(StatisticsContract.PRODUCTS_NUMBER_KEY);
            productsMaxPrice = statistics.get(StatisticsContract.PRODUCTS_MAX_PRICE_KEY);
            productsMinPrice = statistics.get(StatisticsContract.PRODUCTS_MIN_PRICE_KEY);
            configureGraphProductsNumber();
            configureGraphMaxMinPrice();
        }
    }

    private void setUiReferences()
    {
        graphProductsNumber = findViewById(R.id.activity_graphs_products_number_graphview);
        graphMaxMinPrice = findViewById(R.id.activity_graphs_maxmin_price_graphview);
    }

    private void configureGraphProductsNumber()
    {
        final DataPoint FIX_UI_DATAPOINT = new DataPoint(1, 0);
        DataPoint[] dataPointsArray = {new DataPoint(0, productsNumber), FIX_UI_DATAPOINT};
        BarGraphSeries<DataPoint> barGraphSeries = new BarGraphSeries<>(dataPointsArray);
        barGraphSeries.setAnimated(true);
        graphProductsNumber.addSeries(barGraphSeries);
    }

    private void configureGraphMaxMinPrice()
    {
        DataPoint[] dataPointsArray = {
                new DataPoint(0, productsMaxPrice),
                new DataPoint(1, productsMinPrice)};
        BarGraphSeries<DataPoint> barGraphSeries = new BarGraphSeries<>(dataPointsArray);
        barGraphSeries.setAnimated(true);
        graphMaxMinPrice.addSeries(barGraphSeries);
    }

}
