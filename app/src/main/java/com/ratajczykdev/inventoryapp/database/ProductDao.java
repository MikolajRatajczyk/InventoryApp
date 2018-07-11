package com.ratajczykdev.inventoryapp.database;

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

    @Query("SELECT * FROM products_table ORDER BY name ASC")
    List<Product> getAll();

    @Query("SELECT * FROM products_table WHERE id IN (:ids)")
    List<Product> findAllByIds(int[] ids);

    @Query("SELECT * FROM products_table WHERE name LIKE :searchName LIMIT 1")
    Product findByName(String searchName);

    @Query("SELECT * FROM products_table WHERE id = :searchId")
    Product findById(int searchId);
}
