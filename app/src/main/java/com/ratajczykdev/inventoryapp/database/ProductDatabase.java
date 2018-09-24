package com.ratajczykdev.inventoryapp.database;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;

/**
 * Products database abstract class
 * <p>
 * Uses Singleton design pattern
 *
 * @author Mikolaj Ratajczyk <mikolaj.ratajczyk(AT)gmail.com>
 */
@Database(entities = {Product.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
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
     * To delete all content and repopulate the database whenever the app is started,
     * create a RoomDatabase.Callback and override onOpen().
     * Because you cannot do Room database operations on the UI thread,
     * onOpen() creates and executes an AsyncTask to add content to the database.
     * <p>
     * Only for debugging purposes
     * <p>
     * If you want to use this you must invoke .addCallback(roomDatabaseCallback)"
     * on Room.databaseBuilder.... before .build()
     */
    private static RoomDatabase.Callback roomDatabaseCallback =
            new RoomDatabase.Callback() {
                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDbAsync(productDatabaseInstance).execute();
                }
            };

    /**
     * AsyncTask that deletes the contents of the database,
     * then populates it with the two products.
     * <p>
     * Only for debugging purposes
     */
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final ProductDao productDao;

        PopulateDbAsync(ProductDatabase productDatabase) {
            productDao = productDatabase.productDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            productDao.deleteAll();

            Product productPhone = new Product();
            productPhone.setName("OnePlus 3T");
            productPhone.setPrice(519.99f);
            productPhone.setQuantity(12);
            productDao.insertSingle(productPhone);

            Product productLaptop = new Product();
            productLaptop.setName("MacBook Pro");
            productLaptop.setPrice(999);
            productLaptop.setQuantity(34);
            productDao.insertSingle(productLaptop);

            return null;
        }
    }

}
