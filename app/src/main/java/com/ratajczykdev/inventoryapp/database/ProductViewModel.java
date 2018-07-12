package com.ratajczykdev.inventoryapp.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Provides data to the UI and survive configuration changes
 */
public class ProductViewModel extends AndroidViewModel {

    private ProductRepository productRepository;
    private LiveData<List<Product>> allProducts;

    public ProductViewModel(@NonNull Application application) {
        super(application);
        productRepository = new ProductRepository(application);
        allProducts = productRepository.getAll();
    }

    /**
     * Completely hides implementation from the UI
     */
    public LiveData<List<Product>> getAll() {
        return allProducts;
    }

    /**
     * This way implementation is completely hidden from UI
     */
    public void insertSingle(Product product) {
        productRepository.insertSingle(product);
    }

    /**
     * This way implementation is completely hidden from UI
     */
    public void updateSingle(Product product) {
        productRepository.updateSingle(product);
    }

    /**
     * This way implementation is completely hidden from UI
     */
    public Product findSingleById(int searchId) {
        return productRepository.findSingleById(searchId);
    }

    /**
     * This way implementation is completely hidden from UI
     */
    public void deleteSingle(Product product) {
        productRepository.deleteSingle(product);
    }

}
