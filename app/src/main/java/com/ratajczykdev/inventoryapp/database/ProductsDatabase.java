package com.ratajczykdev.inventoryapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

/**
 * Products database abstract class
 */
@Database(entities = {Product.class}, version = 1, exportSchema = false)
public abstract class ProductsDatabase extends RoomDatabase {

    public abstract ProductDao productDao();
}
