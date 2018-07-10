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

    @Query("SELECT * FROM products")
    List<Product> getAll();

    @Query("SELECT * FROM products WHERE id IN (:ids)")
    List<Product> findAllByIds(int[] ids);

    @Query("SELECT * FROM products WHERE name LIKE :searchName LIMIT 1")
    Product findByName(String searchName);

    @Query("SELECT * FROM products WHERE id = :searchId")
    Product findById(int searchId);

    @Insert
    void insertMultipleProducts(List<Product> productsList);

    @Update
    void updateProduct(Product product);

    @Delete
    void deleteProduct(Product product);
}
