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

    private ProductDao productDao;
    private LiveData<List<Product>> allProducts;


    public ProductRepository(Application application) {
        ProductDatabase productDatabase = ProductDatabase.getProductsDatabaseInstance(application);
        productDao = productDatabase.productDao();
        allProducts = productDao.getAll();
    }

    /**
     * Wrapper for {@link ProductDao#getAll()}
     */
    public LiveData<List<Product>> getAll() {
        return allProducts;
    }

    /**
     * Wrapper for {@link ProductDao#insertSingle(Product)}
     * Call this on a non-UI thread or app will crash.
     */
    public void insertSingle(Product product) {

        new InsertSingleAsyncTask(productDao).execute(product);
    }

    /**
     * Static class for {@link ProductRepository#insertSingle(Product)}
     */
    private static class InsertSingleAsyncTask extends AsyncTask<Product, Void, Void> {
        private ProductDao productDao;

        InsertSingleAsyncTask(ProductDao productDao) {
            this.productDao = productDao;
        }

        @Override
        protected Void doInBackground(Product... products) {
            productDao.insertSingle(products[0]);
            return null;
        }
    }

    /**
     * Wrapper for {@link ProductDao#updateSingle(Product)}
     * Call this on a non-UI thread or app will crash.
     */
    public void updateSingle(Product product) {
        new UpdateSingleAsyncTask(productDao).execute(product);
    }

    /**
     * Static class for {@link ProductRepository#updateSingle(Product)}
     */
    private static class UpdateSingleAsyncTask extends AsyncTask<Product, Void, Void> {
        private ProductDao productDao;

        UpdateSingleAsyncTask(ProductDao productDao) {
            this.productDao = productDao;
        }

        @Override
        protected Void doInBackground(Product... products) {
            productDao.updateSingle(products[0]);
            return null;
        }
    }

    /**
     * Wrapper for {@link ProductDao#findSingleById(int)}
     * Call this on a non-UI thread or app will crash.
     */
    public Product findSingleById(int searchId) {
        Product product = null;
        try {
            product = new FindSingleByIdAsyncTask(productDao).execute(searchId).get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e("ProductRepository", "findSingleById failed, id: " + searchId);
            e.printStackTrace();
        }
        return product;
    }

    /**
     * Static class for {@link ProductRepository#findSingleById(int)}
     */
    private static class FindSingleByIdAsyncTask extends AsyncTask<Integer, Void, Product> {
        private ProductDao productDao;

        public FindSingleByIdAsyncTask(ProductDao productDao) {
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
     * Wrapper for {@link ProductDao#deleteSingle(Product)}
     * Call this on a non-UI thread or app will crash.
     */
    public void deleteSingle(Product product) {
        new DeleteSingleAsyncTask(productDao).execute(product);
    }

    /**
     * Static class for {@link ProductRepository#deleteSingle(Product)}
     */
    private static class DeleteSingleAsyncTask extends AsyncTask<Product, Void, Void> {
        private ProductDao productDao;

        public DeleteSingleAsyncTask(ProductDao productDao) {
            this.productDao = productDao;
        }

        @Override
        protected Void doInBackground(Product... products) {
            productDao.deleteSingle(products[0]);
            return null;
        }
    }
}
