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
public abstract class ProductsDatabase extends RoomDatabase {

    private static ProductsDatabase productsDatabaseInstance;

    public abstract ProductDao productDao();

    public static ProductsDatabase getProductsDatabaseInstance(Context context) {
        if (productsDatabaseInstance == null) {
            productsDatabaseInstance = Room.databaseBuilder(context, ProductsDatabase.class, "products_database")
                    .fallbackToDestructiveMigration()
                    .build();
            return productsDatabaseInstance;
        } else {
            return productsDatabaseInstance;
        }
    }
}
