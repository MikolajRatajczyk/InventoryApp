package com.ratajczykdev.inventoryapp.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

/**
 * Abstracts access to multiple data sources (here: only one)
 * The Repository is not part of the Architecture Components libraries,
 * but is a suggested best practice for code separation and architecture.
 */
public class ProductRepository {
    //  TODO: add more methods wrappers

    private ProductDao productDao;
    private LiveData<List<Product>> allProducts;

    ProductRepository(Application application) {
        ProductDatabase productDatabase = ProductDatabase.getProductsDatabaseInstance(application);
        productDao = productDatabase.productDao();
        allProducts = productDao.getAll();
    }

    /**
     * Wrapper for getAll()
     */
    LiveData<List<Product>> getAll() {
        return allProducts;
    }

    /**
     * Wrapper for insertSingle()
     * Call this on a non-UI thread or app will crash.
     */
    public void insertSingle(Product product) {

        new insertSingleAsyncTask(productDao).execute(product);
    }

    /**
     * Wrapper for updateSingle()
     * Call this on a non-UI thread or app will crash.
     */
    public void updateSingle(Product product) {
        new updateSingleAsyncTask(productDao).execute(product);
    }

    /**
     * Static class for insertSingle
     */
    private static class insertSingleAsyncTask extends AsyncTask<Product, Void, Void> {
        private ProductDao productDao;

        insertSingleAsyncTask(ProductDao productDao) {
            this.productDao = productDao;
        }

        @Override
        protected Void doInBackground(Product... products) {
            productDao.insertSingle(products[0]);
            return null;
        }
    }

    /**
     * Static class for updateSingle
     */
    private static class updateSingleAsyncTask extends AsyncTask<Product, Void, Void> {
        private ProductDao productDao;

        updateSingleAsyncTask(ProductDao productDao) {
            this.productDao = productDao;
        }

        @Override
        protected Void doInBackground(Product... products) {
            productDao.updateSingle(products[0]);
            return null;
        }
    }
}
