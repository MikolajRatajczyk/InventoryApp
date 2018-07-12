package com.ratajczykdev.inventoryapp;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ratajczykdev.inventoryapp.data.ImageHelper;
import com.ratajczykdev.inventoryapp.database.Product;
import com.ratajczykdev.inventoryapp.database.ProductListRecyclerAdapter;
import com.ratajczykdev.inventoryapp.database.ProductViewModel;

import java.util.Locale;

/**
 * This class allows to edit or add a new product to the database
 *
 * @author Mikołaj Ratajczyk
 */
public class ProductEditActivity extends AppCompatActivity {
    //  TODO: do not store rest of the data here, create ViewModel

    /**
     * Activity gets its own {@link ProductViewModel},
     * but with the same repository as {@link CatalogActivity}
     */
    private ProductViewModel productViewModel;

    /**
     * Request code that identifies photo request from user
     */
    private static final int PHOTO_REQUEST_ID = 2;

    private Button buttonChangePhoto;
    private EditText editTextName;
    private EditText editTextQuantity;
    private EditText editTextPrice;
    private Button buttonDelete;
    private Button buttonCancel;
    private Button buttonSave;

    private Product currentProduct;
    private int currentProductId;
    private String currentName;
    private int currentQuantity;
    private float currentFloatPrice;

    /**
     * URI to product photo received directly from user
     */
    private Uri receivedImageUri;

    /**
     * URI to product photo saved by app
     */
    private Uri savedImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);

        //  Activity gets its own ViewModel, but with the same repository
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);

        setUiElementsReferences();
        setButtonCancelListener();
        setButtonChangePhotoListener();

        if (getIntent().hasExtra(ProductListRecyclerAdapter.DATA_SELECTED_PRODUCT_ID)) {
            currentProductId = getProductIdFromIntent(getIntent());
            setExistingProductDataInUi();
            setupButtonsForEditing();
        } else {
            setupButtonsForAdding();
        }
    }

    private void setUiElementsReferences() {
        buttonChangePhoto = findViewById(R.id.product_edit_change_photo_button);
        editTextName = findViewById(R.id.product_edit_name);
        editTextQuantity = findViewById(R.id.product_edit_quantity);
        editTextPrice = findViewById(R.id.product_edit_price);
        buttonDelete = findViewById(R.id.product_edit_delete_button);
        buttonCancel = findViewById(R.id.product_edit_cancel_button);
        buttonSave = findViewById(R.id.product_edit_save_button);
    }

    private void setButtonCancelListener() {
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setButtonChangePhotoListener() {
        buttonChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestBitmapUriFromUser();
            }
        });
    }

    /**
     * Starts activity to receive image URI from user
     * <p>
     * Data will be received by {@link ProductEditActivity#onActivityResult(int, int, Intent)}
     */
    private void requestBitmapUriFromUser() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PHOTO_REQUEST_ID);
    }

    private int getProductIdFromIntent(Intent intent) {
        String stringCurrentProductId = intent.getStringExtra(ProductListRecyclerAdapter.DATA_SELECTED_PRODUCT_ID);
        return Integer.parseInt(stringCurrentProductId);
    }

    /**
     * Set current product data with provided ones from ViewModel
     */
    private void setExistingProductDataInUi() {
        currentProduct = productViewModel.findSingleById(currentProductId);
        setQuantityInUi();
        setPriceInUi();
        setNameInUi();
    }

    private void setQuantityInUi() {
        int quantity = currentProduct.getQuantity();
        editTextQuantity.setText(String.valueOf(quantity));
    }

    private void setPriceInUi() {
        float price = currentProduct.getPrice();
        editTextPrice.setText(String.format(Locale.US, "%.2f", price));
    }

    private void setNameInUi() {
        String name = currentProduct.getName();
        editTextName.setText(name);
    }

    /**
     * Setups all buttons in order to edit existing product
     */
    private void setupButtonsForEditing() {

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (saveExistingProduct()) {
                    finish();
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deleteProduct()) {
                    finish();
                }
            }
        });
    }

    /**
     * Saves existing product with changes
     *
     * @return true if change was successful, false if failed
     */
    private boolean saveExistingProduct() {
        if (setCurrentProductDataFromUi()) {
            Product editedProduct = getProductWithCurrentData();
            editedProduct.setId(currentProductId);
            productViewModel.updateSingle(editedProduct);
            return true;
        } else {
            return false;
        }
    }

    private boolean setCurrentProductDataFromUi() {
        currentQuantity = getIntQuantityFromUi();
        currentFloatPrice = getFloatPriceFromUi();
        currentName = editTextName.getText().toString().trim();

        if (TextUtils.isEmpty(currentName)) {
            Toast.makeText(this, R.string.info_correct_name_enter, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private int getIntQuantityFromUi() {
        String stringQuantity = editTextQuantity.getText().toString().trim();
        if (TextUtils.isEmpty(stringQuantity) || stringQuantity.equals(".")) {
            stringQuantity = "0";
        }
        return Integer.valueOf(stringQuantity);
    }

    private float getFloatPriceFromUi() {
        String stringPrice = editTextPrice.getText().toString().trim();
        if (TextUtils.isEmpty(stringPrice) || stringPrice.equals(".")) {
            stringPrice = "0";
        }
        return Float.valueOf(stringPrice);
    }

    /**
     * Create new product with current data
     */
    @NonNull
    private Product getProductWithCurrentData() {
        Product product = new Product();
        product.setName(currentName);
        product.setQuantity(currentQuantity);
        product.setPrice(currentFloatPrice);
        setPhotoUriInProduct(product);

        return product;
    }

    /**
     * Set photo URI in product if available
     */
    private void setPhotoUriInProduct(Product product) {
        if (savedImageUri != null) {
            String savedImageUriString = savedImageUri.toString();
            product.setPhotoUri(savedImageUriString);
        }
    }

    private boolean deleteProduct() {
        productViewModel.deleteSingle(currentProduct);
        return true;
    }

    /**
     * Setups all buttons in order to add a new product to database
     */
    private void setupButtonsForAdding() {
        buttonDelete.setVisibility(View.INVISIBLE);

        buttonChangePhoto.setText(getString(R.string.add_photo));
        buttonSave.setText(getString(R.string.add));
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addNewProduct()) {
                    finish();
                }
            }
        });
    }

    /**
     * Adds a new product to database
     *
     * @return true if adding was successful, false if failed
     */
    private boolean addNewProduct() {
        if (setCurrentProductDataFromUi()) {
            Product newProduct = getProductWithCurrentData();
            productViewModel.insertSingle(newProduct);
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method is called when request finishes in other app
     *
     * @param requestCode request code passed when starting activity
     * @param resultCode  result code
     * @param data        carries the result data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_REQUEST_ID) {
            if (resultCode == RESULT_OK && data != null) {
                receivedImageUri = data.getData();
                savedImageUri = ImageHelper.saveImageAndGetUri(receivedImageUri, getApplicationContext());

            } else {
                Toast.makeText(this, R.string.info_not_picked_image, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
