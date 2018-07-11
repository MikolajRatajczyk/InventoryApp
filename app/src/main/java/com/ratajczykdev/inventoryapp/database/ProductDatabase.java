package com.ratajczykdev.inventoryapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

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
}
