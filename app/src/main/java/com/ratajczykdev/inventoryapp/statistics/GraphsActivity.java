package com.ratajczykdev.inventoryapp.statistics;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.ratajczykdev.inventoryapp.R;

import java.util.HashMap;

/**
 * Shows graphs with statistics data about products
 *
 * @author Mikołaj Ratajczyk <mikolaj.ratajczyk@gmail.com>
 */
public class GraphsActivity extends AppCompatActivity {
    private GraphView graphItemsAndProductsNumber;
    private GraphView graphMaxAndMinPrice;
    private HashMap<String, Float> statistics;
    private float itemsNumber;
    private float productsNumber;
    private float productsMaxPrice;
    private float productsMinPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs);

        setUiReferences();

        if (getIntent().getSerializableExtra(StatisticsContract.STATISTICS_MAP_NAME) != null) {
            Intent intent = getIntent();
            statistics = (HashMap<String, Float>) intent.getSerializableExtra(StatisticsContract.STATISTICS_MAP_NAME);
            itemsNumber = statistics.get(StatisticsContract.ITEMS_NUMBER_KEY);
            productsNumber = statistics.get(StatisticsContract.PRODUCTS_NUMBER_KEY);
            productsMaxPrice = statistics.get(StatisticsContract.PRODUCTS_MAX_PRICE_KEY);
            productsMinPrice = statistics.get(StatisticsContract.PRODUCTS_MIN_PRICE_KEY);
            configureGraphItemsAndProductsNumber();
            configureGraphMaxAndMinPrice();
        }
    }

    private void setUiReferences() {
        graphItemsAndProductsNumber = findViewById(R.id.activity_graphs_products_number_graphview);
        graphMaxAndMinPrice = findViewById(R.id.activity_graphs_maxmin_price_graphview);
    }

    private void configureGraphItemsAndProductsNumber() {
        DataPoint[] dataPointsArray = {
                new DataPoint(0, productsNumber),
                new DataPoint(1, itemsNumber)};

        BarGraphSeries<DataPoint> barGraphSeries = new BarGraphSeries<>(dataPointsArray);
        barGraphSeries.setAnimated(true);
        barGraphSeries.setColor(getColor(R.color.colorAccent));

        graphItemsAndProductsNumber.addSeries(barGraphSeries);
        graphItemsAndProductsNumber.setTitle(getString(R.string.items_products_graph_title));
        hideGraphXLabels(graphItemsAndProductsNumber);
    }

    private void configureGraphMaxAndMinPrice() {
        DataPoint[] dataPointsArray = {
                new DataPoint(0, productsMaxPrice),
                new DataPoint(1, productsMinPrice)};

        BarGraphSeries<DataPoint> barGraphSeries = new BarGraphSeries<>(dataPointsArray);
        barGraphSeries.setAnimated(true);
        barGraphSeries.setColor(getColor(R.color.colorPrimary));

        graphMaxAndMinPrice.addSeries(barGraphSeries);
        graphMaxAndMinPrice.setTitle(getString(R.string.max_min_price_graph_title));
        hideGraphXLabels(graphMaxAndMinPrice);
    }

    private static void hideGraphXLabels(GraphView graph) {
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        //  must have more than one element, if not it will cause crash
        final String[] EMPTY_LABEL_ARRAY = new String[]{"", ""};
        staticLabelsFormatter.setHorizontalLabels(EMPTY_LABEL_ARRAY);

        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
    }

}
