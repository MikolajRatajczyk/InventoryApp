package dev.ratajczyk.inventoryapp.database;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import java.util.List;

/**
 * Provides data to the UI and survive configuration changes
 *
 * @author Mikolaj Ratajczyk <mikolaj.ratajczyk(AT)gmail.com>
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
    public LiveData<Product> findSingleById(int searchId) {
        return productRepository.findSingleById(searchId);
    }

    /**
     * This way implementation is completely hidden from UI
     */
    public void deleteSingle(Product product) {
        productRepository.deleteSingle(product);
    }

    /**
     * This way implementation is completely hidden from UI
     */
    public LiveData<List<Product>> getAllOrderNameAsc() {
        return productRepository.getAllOrderNameAsc();
    }

    /**
     * This way implementation is completely hidden from UI
     */
    public LiveData<List<Product>> getAllOrderNameDesc() {
        return productRepository.getAllOrderNameDesc();
    }

    /**
     * This way implementation is completely hidden from UI
     */
    public LiveData<List<Product>> getAllOrderPriceDesc() {
        return productRepository.getAllOrderPriceDesc();
    }

    /**
     * This way implementation is completely hidden from UI
     */
    public LiveData<List<Product>> getAllOrderPriceAsc() {
        return productRepository.getAllOrderPriceAsc();
    }

    /**
     * This way implementation is completely hidden from UI
     */
    public LiveData<List<Product>> getAllOrderIdDesc() {
        return productRepository.getAllOrderIdDesc();
    }

    /**
     * This way implementation is completely hidden from UI
     */
    public LiveData<List<Product>> getAllOrderIdAsc() {
        return productRepository.getAllOrderIdAsc();
    }

    /**
     * This way implementation is completely hidden from UI
     */
    public LiveData<List<Product>> getAllOrderQuantityDesc() {
        return productRepository.getAllOrderQuantityDesc();
    }

    /**
     * This way implementation is completely hidden from UI
     */
    public LiveData<List<Product>> getAllOrderQuantityAsc() {
        return productRepository.getAllOrderQuantityAsc();
    }

}
