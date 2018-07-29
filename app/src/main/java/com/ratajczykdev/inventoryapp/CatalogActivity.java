package com.ratajczykdev.inventoryapp;

import android.Manifest;
import android.app.ActivityOptions;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.ratajczykdev.inventoryapp.database.Product;
import com.ratajczykdev.inventoryapp.database.ProductViewModel;
import com.ratajczykdev.inventoryapp.settings.SettingsActivity;
import com.ratajczykdev.inventoryapp.statistics.StatisticsActivity;

import java.util.List;

/**
 * Loads view with product list
 * <p>
 * Gets data from own {@link ProductViewModel}
 *
 * @author Mikołaj Ratajczyk
 */
public class CatalogActivity extends AppCompatActivity {

    //  TODO: add listener for BottomNavigationView
    //  TODO: delete old navigation icons on appbar

    /**
     * Identifier for WRITE permissions request
     * The callback method gets the result of the request
     */
    private static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_ID = 1000;

    private FloatingActionButton fabNewProduct;

    /**
     * Activity gets its own {@link ProductViewModel},
     * but with the same repository as e.g. {@link StatisticsActivity} and {@link ProductEditActivity}
     */
    private ProductViewModel productViewModel;
    private RecyclerView recyclerProductList;
    ProductListRecyclerAdapter productListRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        askWriteStoragePermission();

        //  Use ViewModelProviders to associate your ViewModel with your UI controller.
        //  When your app first starts, the ViewModelProviders will create the ViewModel.
        //  When the activity is destroyed, for example through a configuration change, the ViewModel persists.
        //  When the activity is re-created, the ViewModelProviders return the existing ViewModel
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        //  Add an observer for the LiveData returned by getAll().
        //  The onChanged() method fires when the observed data changes
        //  and the activity is in the foreground.
        productViewModel.getAll().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(@Nullable List<Product> productsList) {
                // Update the cached copy of the products in the adapter.
                productListRecyclerAdapter.setProducts(productsList);
            }
        });

        recyclerProductList = findViewById(R.id.product_list_recyclerview);
        productListRecyclerAdapter = new ProductListRecyclerAdapter(this);
        recyclerProductList.setAdapter(productListRecyclerAdapter);
        recyclerProductList.setLayoutManager(new LinearLayoutManager(this));

        fabNewProduct = findViewById(R.id.fab_new_product);
        setFabListener();
        animateFabNewProduct();

        setBottomNavigationListener();
    }

    private void askWriteStoragePermission() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //  TODO: add explanation for a user (asynchronously)
            }

            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_ID);
        }
    }

    private void setFabListener() {
        fabNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, ProductEditActivity.class);
                startActivity(intent);
            }
        });
    }

    private void animateFabNewProduct() {
        final int FULL_ANGLE_IN_DEG = 360;
        final int ANIMATION_DURATION_IN_MS = 700;
        fabNewProduct.animate()
                .rotation(FULL_ANGLE_IN_DEG)
                .setInterpolator(AnimationUtils.loadInterpolator(CatalogActivity.this, android.R.interpolator.accelerate_decelerate))
                .setDuration(ANIMATION_DURATION_IN_MS)
                .start();
    }

    /**
     * Modifies App Bar to have actions
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_catalog_appbar_actions, menu);
        return true;
    }

    /**
     * Triggers methods for the specified action on the app bar
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int currentItemId = item.getItemId();
        if (currentItemId == R.id.activity_catalog_appbar_actions_sort_by_name_asc) {
            productViewModel.getAllOrderNameAsc().observe(this, new Observer<List<Product>>() {
                @Override
                public void onChanged(@Nullable List<Product> productsList) {
                    productListRecyclerAdapter.setProducts(productsList);
                }
            });
        } else if (currentItemId == R.id.activity_catalog_appbar_actions_sort_by_name_desc) {
            productViewModel.getAllOrderNameDesc().observe(this, new Observer<List<Product>>() {
                @Override
                public void onChanged(@Nullable List<Product> productsList) {
                    productListRecyclerAdapter.setProducts(productsList);
                }
            });
        } else if (currentItemId == R.id.activity_catalog_appbar_actions_sort_by_price_highest) {
            productViewModel.getAllOrderPriceDesc().observe(this, new Observer<List<Product>>() {
                @Override
                public void onChanged(@Nullable List<Product> productsList) {
                    productListRecyclerAdapter.setProducts(productsList);
                }
            });
        } else if (currentItemId == R.id.activity_catalog_appbar_actions_sort_by_price_lowest) {
            productViewModel.getAllOrderPriceAsc().observe(this, new Observer<List<Product>>() {
                @Override
                public void onChanged(@Nullable List<Product> productsList) {
                    productListRecyclerAdapter.setProducts(productsList);
                }
            });
        } else if (currentItemId == R.id.activity_catalog_appbar_actions_sort_by_date_newest) {
            productViewModel.getAllOrderIdDesc().observe(this, new Observer<List<Product>>() {
                @Override
                public void onChanged(@Nullable List<Product> productsList) {
                    productListRecyclerAdapter.setProducts(productsList);
                }
            });
        } else if (currentItemId == R.id.activity_catalog_appbar_actions_sort_by_date_oldest) {
            productViewModel.getAllOrderIdAsc().observe(this, new Observer<List<Product>>() {
                @Override
                public void onChanged(@Nullable List<Product> productsList) {
                    productListRecyclerAdapter.setProducts(productsList);
                }
            });
        } else if (currentItemId == R.id.activity_catalog_appbar_actions_sort_by_quantity_more) {
            productViewModel.getAllOrderQuantityDesc().observe(this, new Observer<List<Product>>() {
                @Override
                public void onChanged(@Nullable List<Product> productsList) {
                    productListRecyclerAdapter.setProducts(productsList);
                }
            });
        } else if (currentItemId == R.id.activity_catalog_appbar_actions_sort_by_quantity_less) {
            productViewModel.getAllOrderQuantityAsc().observe(this, new Observer<List<Product>>() {
                @Override
                public void onChanged(@Nullable List<Product> productsList) {
                    productListRecyclerAdapter.setProducts(productsList);
                }
            });
        }
        return true;
    }

    private void setBottomNavigationListener() {
        BottomNavigationView bottomNavigation = findViewById(R.id.activity_catalog_bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem currentItem) {
                int currentItemId = currentItem.getItemId();
                if (currentItemId == R.id.settings_button) {
                    Intent intent = new Intent(CatalogActivity.this, SettingsActivity.class);
                    startActivity(intent);
                } else if (currentItemId == R.id.statistics_button) {
                    Intent intent = new Intent(CatalogActivity.this, StatisticsActivity.class);
                    startActivity(intent);
                } else if (currentItemId == R.id.about_button) {
                    Intent intent = new Intent(CatalogActivity.this, AboutActivity.class);
                    //  to start content transition in AboutActivity
                    Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(CatalogActivity.this).toBundle();
                    startActivity(intent, bundle);
                }
                return true;
            }
        });
    }
}


