package dev.ratajczyk.inventoryapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Interface class for database access
 *
 * Getting LiveData happens on background thread automatically
 *
 * @author Mikolaj Ratajczyk <mikolaj.ratajczyk(AT)gmail.com>
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
    LiveData<Product> findSingleById(int searchId);

    @Query("SELECT * FROM products_table ORDER BY name ASC")
    LiveData<List<Product>> getAllOrderNameAsc();

    @Query("SELECT * FROM products_table ORDER BY name DESC")
    LiveData<List<Product>> getAllOrderNameDesc();

    @Query("SELECT * FROM products_table ORDER BY price DESC")
    LiveData<List<Product>> getAllOrderPriceDesc();

    @Query("SELECT * FROM products_table ORDER BY price ASC")
    LiveData<List<Product>> getAllOrderPriceAsc();

    @Query("SELECT * FROM products_table ORDER BY id DESC")
    LiveData<List<Product>> getAllOrderIdDesc();

    @Query("SELECT * FROM products_table ORDER BY id ASC")
    LiveData<List<Product>> getAllOrderIdAsc();

    @Query("SELECT * FROM products_table ORDER BY quantity DESC")
    LiveData<List<Product>> getAllOrderQuantityDesc();

    @Query("SELECT * FROM products_table ORDER BY quantity ASC")
    LiveData<List<Product>> getAllOrderQuantityAsc();
}
