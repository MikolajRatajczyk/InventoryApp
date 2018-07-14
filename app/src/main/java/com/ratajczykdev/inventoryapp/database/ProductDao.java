package com.ratajczykdev.inventoryapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Interface class for database access
 */
@Dao
public interface ProductDao {


    @Insert
    void insertSingle(Product product);

    @Insert
    void insertMultiple(List<Product> productsList);

    @Update
    void updateSingle(Product product);

    @Delete
    void deleteSingle(Product product);

    @Query("DELETE FROM products_table")
    void deleteAll();

    @Query("SELECT * FROM products_table ORDER BY id ASC")
    LiveData<List<Product>> getAll();

    @Query("SELECT * FROM products_table WHERE id IN (:ids)")
    LiveData<List<Product>> findMultipleByIds(int[] ids);

    @Query("SELECT * FROM products_table WHERE name LIKE :searchName LIMIT 1")
    LiveData<Product> findSingleByName(String searchName);

    @Query("SELECT * FROM products_table WHERE id = :searchId")
    Product findSingleById(int searchId);

    @Query("SELECT * FROM products_table ORDER BY name ASC")
    LiveData<List<Product>> getAllOrderNameAsc();

    @Query("SELECT * FROM products_table ORDER BY name DESC")
    LiveData<List<Product>> getAllOrderNameDesc();

    @Query("SELECT * FROM products_table ORDER BY price DESC")
    LiveData<List<Product>> getAllOrderPriceDesc();
}
