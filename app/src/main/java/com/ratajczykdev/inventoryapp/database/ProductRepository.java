package com.ratajczykdev.inventoryapp.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Abstracts access to multiple data sources (here: only one)
 * The Repository is not part of the Architecture Components libraries,
 * but is a suggested best practice for code separation and architecture.
 */
public class ProductRepository {
    //  TODO: change class names to Big letter (?)

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

    /**
     * Wrapper for findSingleById
     * Call this on a non-UI thread or app will crash.
     */
    Product findSingleById(int searchId) {
        Product product = null;
        try {
            product = new findSingleByIdAsyncTask(productDao).execute(searchId).get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e("ProductRepository", "findSingleById failed, id: " + searchId);
            e.printStackTrace();
        }
        return product;
    }

    /**
     * Static class for findSingleById
     */
    private static class findSingleByIdAsyncTask extends AsyncTask<Integer, Void, Product> {
        private ProductDao productDao;

        public findSingleByIdAsyncTask(ProductDao productDao) {
            this.productDao = productDao;
        }

        @Override
        protected Product doInBackground(Integer... ids) {
            Product product = productDao.findSingleById(ids[0]);
            return product;
        }

        @Override
        protected void onPostExecute(Product product) {
            super.onPostExecute(product);
        }
    }

    /**
     * Wrapper for deleteSingle
     * Call this on a non-UI thread or app will crash.
     */
    public void deleteSingle(Product product) {
        new deleteSingleAsyncTask(productDao).execute(product);
    }

    /**
     * Static class for deleteSingle
     */
    private static class deleteSingleAsyncTask extends AsyncTask<Product, Void, Void> {
        private ProductDao productDao;

        public deleteSingleAsyncTask(ProductDao productDao) {
            this.productDao = productDao;
        }

        @Override
        protected Void doInBackground(Product... products) {
            productDao.deleteSingle(products[0]);
            return null;
        }
    }


}
