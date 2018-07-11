package com.ratajczykdev.inventoryapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Products database abstract class
 * <p>
 * Uses Singleton design pattern
 */
@Database(entities = {Product.class}, version = 1, exportSchema = false)
public abstract class ProductDatabase extends RoomDatabase {

    private static ProductDatabase productDatabaseInstance;

    public abstract ProductDao productDao();

    public static ProductDatabase getProductsDatabaseInstance(Context context) {
        if (productDatabaseInstance == null) {
            synchronized (ProductDatabase.class) {
                if (productDatabaseInstance == null) {
                    productDatabaseInstance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            ProductDatabase.class,
                            "products_database")
                            .build();
                }
            }
        }
        return productDatabaseInstance;
    }

    /**
     * AsyncTask that deletes the contents of the database,
     * then populates it with the two products.
     * <p>
     * Only for debugging purposes
     */
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        //  TODO: only temporary method
        private final ProductDao productDao;

        PopulateDbAsync(ProductDatabase productDatabase) {
            productDao = productDatabase.productDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            productDao.deleteAll();

            Product productPhone = new Product();
            productPhone.setName("OnePlus 3T");
            productDao.insertSingle(productPhone);

            Product productLaptop = new Product();
            productLaptop.setName("MacBook Pro");
            productDao.insertSingle(productLaptop);

            return null;
        }
    }

}
