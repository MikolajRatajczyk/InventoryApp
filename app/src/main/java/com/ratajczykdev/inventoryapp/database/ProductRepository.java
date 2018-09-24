package com.ratajczykdev.inventoryapp.database;

import android.app.Application;
import androidx.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

/**
 * Abstracts access to multiple data sources (here: only one)
 * The Repository is not part of the Architecture Components libraries,
 * but is a suggested best practice for code separation and architecture.
 *
 * @author Mikolaj Ratajczyk <mikolaj.ratajczyk(AT)gmail.com>
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
     */
    public LiveData<Product> findSingleById(int searchId) {
        return productDao.findSingleById(searchId);
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

    /**
     * Wrapper for {@link ProductDao#getAllOrderNameAsc()}
     */
    public LiveData<List<Product>> getAllOrderNameAsc() {
        return productDao.getAllOrderNameAsc();
    }


    /**
     * Wrapper for {@link ProductDao#getAllOrderNameDesc()}
     */
    public LiveData<List<Product>> getAllOrderNameDesc() {
        return productDao.getAllOrderNameDesc();
    }


    /**
     * Wrapper for {@link ProductDao#getAllOrderPriceDesc()}
     */
    public LiveData<List<Product>> getAllOrderPriceDesc() {
        return productDao.getAllOrderPriceDesc();
    }

    /**
     * Wrapper for {@link ProductDao#getAllOrderPriceAsc()}
     */
    public LiveData<List<Product>> getAllOrderPriceAsc() {
        return productDao.getAllOrderPriceAsc();
    }


    /**
     * Wrapper for {@link ProductDao#getAllOrderIdDesc()}
     */
    public LiveData<List<Product>> getAllOrderIdDesc() {
        return productDao.getAllOrderIdDesc();
    }


    /**
     * Wrapper for {@link ProductDao#getAllOrderIdAsc()}
     */
    public LiveData<List<Product>> getAllOrderIdAsc() {
        return productDao.getAllOrderIdAsc();
    }


    /**
     * Wrapper for {@link ProductDao#getAllOrderQuantityDesc()}
     */
    public LiveData<List<Product>> getAllOrderQuantityDesc() {
        return productDao.getAllOrderQuantityDesc();
    }

    /**
     * Wrapper for {@link ProductDao#getAllOrderQuantityAsc()}
     */
    public LiveData<List<Product>> getAllOrderQuantityAsc() {
        return productDao.getAllOrderQuantityAsc();
    }

}
