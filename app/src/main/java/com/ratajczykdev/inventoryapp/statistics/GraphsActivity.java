package com.ratajczykdev.inventoryapp.statistics;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.ratajczykdev.inventoryapp.R;

import java.util.HashMap;

public class GraphsActivity extends AppCompatActivity
{
    //  TODO: make more usable graphs

    private GraphView graphViewStatistics;
    private HashMap<String, Float> statistics;
    private float productsNumber;

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
            configureGraphViewStatistics();
        }
    }

    private void setUiReferences()
    {
        graphViewStatistics = findViewById(R.id.activity_graphs_statistics_graphview);
    }

    private void configureGraphViewStatistics()
    {
        //  TODO: when there will be more statistics data - delete placeholder DataPoint s
        DataPoint[] dataPointsArray = {
                new DataPoint(0, -1),
                new DataPoint(1, productsNumber),
                new DataPoint(2, 3)};
        BarGraphSeries<DataPoint> barGraphSeries = new BarGraphSeries<>(dataPointsArray);
        graphViewStatistics.addSeries(barGraphSeries);
    }

}
