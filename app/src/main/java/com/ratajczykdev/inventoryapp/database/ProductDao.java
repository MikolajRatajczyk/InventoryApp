package com.ratajczykdev.inventoryapp.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ProductDao {

    @Query("SELECT * FROM products")
    List<Product> getAll();

    @Query("SELECT * FROM products WHERE id (:ids)")
    List<Product> findAllByIds(int[] ids);

    @Query("SELECT * FROM products WHERE name LIKE :searchName LIMIT 1")
    Product findByName(String searchName);

    @Insert
    void insertAll(Product[] products);

    @Delete
    void delete(Product product);
}
