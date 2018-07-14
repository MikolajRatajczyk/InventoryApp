package com.ratajczykdev.inventoryapp.statistics;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.ratajczykdev.inventoryapp.CatalogActivity;
import com.ratajczykdev.inventoryapp.ProductDetailActivity;
import com.ratajczykdev.inventoryapp.R;
import com.ratajczykdev.inventoryapp.database.Product;
import com.ratajczykdev.inventoryapp.database.ProductViewModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Shows statistics data about products in numeric form
 * <p>
 * Gets data from own {@link ProductViewModel}
 *
 * @author Mikołaj Ratajczyk <mikolaj.ratajczyk@gmail.com>
 */
public class StatisticsActivity extends AppCompatActivity {

    /**
     * Activity gets its own {@link ProductViewModel},
     * but with the same repository as e.g. {@link CatalogActivity} and {@link ProductDetailActivity}
     */
    private ProductViewModel productViewModel;
    private List<Product> productList;

    private TextView textViewItemsNumber;
    private TextView textViewProductsNumber;
    private TextView textViewMaxPrice;
    private TextView textViewMinPrice;
    private FloatingActionButton fabGraphs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);

        productList = productViewModel.getAll().getValue();
        productViewModel.getAll().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(@Nullable List<Product> products) {
                productList = products;
                if (productList != null && !productList.isEmpty()) {
                    updateStatisticsInUi();
                }
            }
        });

        setUiReferences();
        setFabListener();
        animateFabGraphs();
    }

    private void updateStatisticsInUi() {
        updateItemsNumberInUi();
        updateProductsNumberInUi();
        updateMaxPriceInUi();
        updateMinPriceInUi();
    }

    private void updateItemsNumberInUi() {
        String itemsNumberString = String.valueOf(getItemsNumber());
        textViewItemsNumber.setText(itemsNumberString);
    }

    private int getItemsNumber() {
        int itemsNumber = 0;
        if (!productList.isEmpty()) {
            for (Product product : productList) {
                itemsNumber += product.getQuantity();
            }
        }
        return itemsNumber;
    }

    private void updateProductsNumberInUi() {
        String productsNumberString = String.valueOf(getProductsNumber());
        textViewProductsNumber.setText(productsNumberString);
    }

    private int getProductsNumber() {
        return productList.size();
    }

    private void updateMaxPriceInUi() {
        String maximumPriceString = String.valueOf(getProductsMaxPrice());
        textViewMaxPrice.setText(maximumPriceString);
    }

    private float getProductsMaxPrice() {
        Set<Float> pricesSet = getPricesSet();
        return Collections.max(pricesSet);
    }

    private Set<Float> getPricesSet() {
        Set<Float> pricesSet = new HashSet<>();
        for (Product product : productList) {
            pricesSet.add(product.getPrice());
        }
        return pricesSet;
    }

    private void updateMinPriceInUi() {
        String minimalPriceString = String.valueOf(getProductsMinPrice());
        textViewMinPrice.setText(minimalPriceString);
    }

    private float getProductsMinPrice() {
        Set<Float> pricesSet = getPricesSet();
        return Collections.min(pricesSet);
    }

    private void setUiReferences() {
        textViewItemsNumber = findViewById(R.id.activity_statistics_items_number_textview);
        textViewProductsNumber = findViewById(R.id.activity_statistics_products_number_textview);
        textViewMaxPrice = findViewById(R.id.activity_statistics_max_price_textview);
        textViewMinPrice = findViewById(R.id.activity_statistics_min_price_textview);
        fabGraphs = findViewById(R.id.activity_statistics_graphs_fab);
    }

    private void setFabListener() {
        fabGraphs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StatisticsActivity.this, GraphsActivity.class);
                if (productList != null && !productList.isEmpty()) {
                    //  add statistics Map to Intent for GraphActivity
                    intent.putExtra(StatisticsContract.STATISTICS_MAP_NAME, getStatisticsMap());
                }
                startActivity(intent);
            }
        });
    }

    private HashMap<String, Float> getStatisticsMap() {
        HashMap<String, Float> statisticsMap = new HashMap<>();
        statisticsMap.put(StatisticsContract.ITEMS_NUMBER_KEY, (float) getItemsNumber());
        statisticsMap.put(StatisticsContract.PRODUCTS_NUMBER_KEY, (float) getProductsNumber());
        statisticsMap.put(StatisticsContract.PRODUCTS_MAX_PRICE_KEY, getProductsMaxPrice());
        statisticsMap.put(StatisticsContract.PRODUCTS_MIN_PRICE_KEY, getProductsMinPrice());
        return statisticsMap;
    }

    private void animateFabGraphs() {
        final int FULL_ANGLE_IN_DEG = 360;
        final int ANIMATION_DURATION_IN_MS = 700;
        fabGraphs.animate()
                .rotation(FULL_ANGLE_IN_DEG)
                .setInterpolator(AnimationUtils.loadInterpolator(StatisticsActivity.this, android.R.interpolator.accelerate_decelerate))
                .setDuration(ANIMATION_DURATION_IN_MS)
                .start();
    }
}
