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

    /**
     * Wrapper for {@link ProductDao#getAllOrderNameAsc()}
     * Call this on a non-UI thread or app will crash.
     */
    public LiveData<List<Product>> getAllOrderNameAsc() {
        LiveData<List<Product>> productLiveDataList = null;
        try {
            productLiveDataList = new GetAllOrderNameAscAsyncTask(productDao).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e("ProductRepository", "getAllOrderNameAsc failed");
            e.printStackTrace();
        }
        return productLiveDataList;
    }

    /**
     * Static class for {@link ProductRepository#getAllOrderNameAsc()}
     */
    private static class GetAllOrderNameAscAsyncTask extends AsyncTask<Void, Void, LiveData<List<Product>>> {
        private ProductDao productDao;

        public GetAllOrderNameAscAsyncTask(ProductDao productDao) {
            this.productDao = productDao;
        }

        @Override
        protected LiveData<List<Product>> doInBackground(Void... voids) {
            return productDao.getAllOrderNameAsc();
        }

        @Override
        protected void onPostExecute(LiveData<List<Product>> listLiveData) {
            super.onPostExecute(listLiveData);
        }
    }

    /**
     * Wrapper for {@link ProductDao#getAllOrderNameDesc()}
     * Call this on a non-UI thread or app will crash.
     */
    public LiveData<List<Product>> getAllOrderNameDesc() {
        LiveData<List<Product>> productLiveDataList = null;
        try {
            productLiveDataList = new GetAllOrderNameDescAsyncTask(productDao).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e("ProductRepository", "getAllOrderNameDesc failed");
            e.printStackTrace();
        }
        return productLiveDataList;
    }

    /**
     * Static class for {@link ProductRepository#getAllOrderNameDesc()}
     */
    private static class GetAllOrderNameDescAsyncTask extends AsyncTask<Void, Void, LiveData<List<Product>>> {
        private ProductDao productDao;

        public GetAllOrderNameDescAsyncTask(ProductDao productDao) {
            this.productDao = productDao;
        }

        @Override
        protected LiveData<List<Product>> doInBackground(Void... voids) {
            return productDao.getAllOrderNameDesc();
        }

        @Override
        protected void onPostExecute(LiveData<List<Product>> listLiveData) {
            super.onPostExecute(listLiveData);
        }
    }

    /**
     * Wrapper for {@link ProductDao#getAllOrderPriceDesc()}
     * Call this on a non-UI thread or app will crash.
     */
    public LiveData<List<Product>> getAllOrderPriceDesc() {
        LiveData<List<Product>> productLiveDataList = null;
        try {
            productLiveDataList = new GetAllOrderPriceDescAsyncTask(productDao).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e("ProductRepository", "getAllOrderPriceDesc failed");
            e.printStackTrace();
        }
        return productLiveDataList;
    }

    /**
     * Static class for {@link ProductRepository#getAllOrderPriceDesc()}
     */
    private static class GetAllOrderPriceDescAsyncTask extends AsyncTask<Void, Void, LiveData<List<Product>>> {
        private ProductDao productDao;

        public GetAllOrderPriceDescAsyncTask(ProductDao productDao) {
            this.productDao = productDao;
        }

        @Override
        protected LiveData<List<Product>> doInBackground(Void... voids) {
            return productDao.getAllOrderPriceDesc();
        }

        @Override
        protected void onPostExecute(LiveData<List<Product>> listLiveData) {
            super.onPostExecute(listLiveData);
        }
    }

    /**
     * Wrapper for {@link ProductDao#getAllOrderPriceAsc()}
     * Call this on a non-UI thread or app will crash.
     */
    public LiveData<List<Product>> getAllOrderPriceAsc() {
        LiveData<List<Product>> productLiveDataList = null;
        try {
            productLiveDataList = new GetAllOrderPriceAscAsyncTask(productDao).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e("ProductRepository", "getAllOrderPriceDesc failed");
            e.printStackTrace();
        }
        return productLiveDataList;
    }

    /**
     * Static class for {@link ProductRepository#getAllOrderPriceAsc()}
     */
    private static class GetAllOrderPriceAscAsyncTask extends AsyncTask<Void, Void, LiveData<List<Product>>> {
        private ProductDao productDao;

        public GetAllOrderPriceAscAsyncTask(ProductDao productDao) {
            this.productDao = productDao;
        }

        @Override
        protected LiveData<List<Product>> doInBackground(Void... voids) {
            return productDao.getAllOrderPriceAsc();
        }

        @Override
        protected void onPostExecute(LiveData<List<Product>> listLiveData) {
            super.onPostExecute(listLiveData);
        }
    }
}
