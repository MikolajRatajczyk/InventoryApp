package com.ratajczykdev.inventoryapp.detailandedit;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ratajczykdev.inventoryapp.R;
import com.ratajczykdev.inventoryapp.catalog.CatalogFragment;
import com.ratajczykdev.inventoryapp.catalog.ProductListRecyclerAdapter;
import com.ratajczykdev.inventoryapp.database.Product;
import com.ratajczykdev.inventoryapp.database.ProductViewModel;
import com.ratajczykdev.inventoryapp.tools.DateHelper;
import com.ratajczykdev.inventoryapp.tools.ImageHelper;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * This class allows to edit existing or add a new product
 * <p>
 * Gets data from own {@link ProductViewModel}
 *
 * @author Mikolaj Ratajczyk <mikolaj.ratajczyk@gmail.com>
 */
public class ProductEditActivity extends AppCompatActivity {
    //  TODO: check for edit mode

    /**
     * Request code that identifies photo request from user
     */
    private static final int PHOTO_REQUEST_ID = 2;

    /**
     * Activity gets its own {@link ProductViewModel},
     * but with the same repository as e.g. {@link CatalogFragment} and {@link ProductDetailActivity}
     */
    private ProductViewModel productViewModel;

    private Button buttonChangePhoto;
    private EditText editTextName;
    private EditText editTextQuantity;
    private EditText editTextPrice;
    private EditText editTextDayYearMonth;
    private EditText editTextTime;
    private Button buttonDelete;
    private Button buttonCancel;
    private Button buttonSave;
    private ImageView imagePhoto;
    //  TODO: do not store rest of the data here use ViewModel
    private Product currentProduct;

    /**
     * URI to product photo saved by app
     */
    private String savedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);

        //  Activity gets its own ViewModel
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);

        setUiElementsReferences();
        setButtonCancelListener();
        setButtonChangePhotoListener();

        if (getIntent().hasExtra(ProductListRecyclerAdapter.DATA_SELECTED_PRODUCT_ID)) {
            int currentProductId = getProductIdFromIntent(getIntent());
            currentProduct = productViewModel.findSingleById(currentProductId);
            loadProductDataToUi();
            setupButtonsForEditing();
        } else {
            currentProduct = new Product();
            setupButtonsForAdding();
        }
    }

    private void setUiElementsReferences() {
        buttonChangePhoto = findViewById(R.id.product_edit_change_photo_button);
        editTextName = findViewById(R.id.product_edit_name);
        editTextQuantity = findViewById(R.id.product_edit_quantity);
        editTextPrice = findViewById(R.id.product_edit_price);
        editTextDayYearMonth = findViewById(R.id.product_edit_day_month_year);
        editTextTime = findViewById(R.id.product_edit_time);
        buttonDelete = findViewById(R.id.product_edit_delete_button);
        buttonCancel = findViewById(R.id.product_edit_cancel_button);
        buttonSave = findViewById(R.id.product_edit_save_button);
        imagePhoto = findViewById(R.id.product_edit_photo_imageview);
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
     * Set current product data with provided ones
     */
    private void loadProductDataToUi() {
        setQuantityInUi();
        setPriceInUi();
        setNameInUi();
        setDateInUi();
        setPhotoInUi();
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

    private void setDateInUi() {
        Date date = currentProduct.getCreationDate();
        DateFormat dateFormat = DateHelper.INSTANCE.getDayMonthYearDateFormat(this);
        String dateString = dateFormat.format(date);
        editTextDayYearMonth.setText(dateString);
    }

    private void setPhotoInUi() {
        Picasso.get()
                .load(currentProduct.getPhotoUri())
                .placeholder(R.drawable.product_list_item_placeholder)
                .error(R.drawable.ic_error)
                .fit()
                .into(imagePhoto);
    }

    /**
     * Setups all buttons in order to edit product
     */
    private void setupButtonsForEditing() {

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (saveProduct()) {
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

    private boolean saveProduct() {
        if (isUiDataCorrect()) {
            replaceProductDataFromUi();
            productViewModel.updateSingle(currentProduct);
            return true;
        } else {
            return false;
        }
    }

    private boolean isUiDataCorrect() {
        String currentName = getStringNameFromUi();
        if (TextUtils.isEmpty(currentName)) {
            Toast.makeText(this, R.string.info_correct_name_enter, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private String getStringNameFromUi() {
        return editTextName.getText().toString().trim();
    }

    /**
     * Replace product data with ones from UI
     */
    private void replaceProductDataFromUi() {
        currentProduct.setName(getStringNameFromUi());
        currentProduct.setQuantity(getIntQuantityFromUi());
        currentProduct.setPrice(getFloatPriceFromUi());
        currentProduct.setCreationDate(getDateFromUi());
        if (savedImageUri != null) {
            currentProduct.setPhotoUri(savedImageUri);
        }
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

    private Date getDateFromUi() {
        Date dayMonthYear = getDayMonthYearFromUi();
        Date time = getTimeFromUi();
        long unixDate = dayMonthYear.getTime() + time.getTime();
        return new Date(unixDate);
    }

    private Date getDayMonthYearFromUi() {
        String stringDate = editTextDayYearMonth.getText().toString().trim();
        Date dayMonthYearDate = new Date(0L);
        if (!TextUtils.isEmpty(stringDate)) {
            DateFormat dateFormat = DateHelper.INSTANCE.getDayMonthYearDateFormat(this);
            try {
                dayMonthYearDate = dateFormat.parse(stringDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return dayMonthYearDate;
    }

    private Date getTimeFromUi() {
        String timeString = editTextTime.getText().toString();
        Date timeDate = new Date(0L);
        if (!TextUtils.isEmpty(timeString)) {
            DateFormat dateFormat = DateHelper.INSTANCE.getTimeDateFormat(this);
            try {
                timeDate = dateFormat.parse(timeString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return timeDate;
    }

    private boolean deleteProduct() {
        productViewModel.deleteSingle(currentProduct);
        return true;
    }

    /**
     * Setups all buttons in order to add a new product
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

        configureDayMonthYearPicker();
        configureTimePicker();
    }

    /**
     * Adds a new product to database
     *
     * @return true if adding was successful, false if failed
     */
    private boolean addNewProduct() {
        if (isUiDataCorrect()) {
            replaceProductDataFromUi();
            productViewModel.insertSingle(currentProduct);
            return true;
        } else {
            return false;
        }
    }

    private void configureDayMonthYearPicker() {
        final Calendar calendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                DateFormat dateFormat = DateHelper.INSTANCE.getDayMonthYearDateFormat(ProductEditActivity.this);
                String dayMonthYearString = dateFormat.format(calendar.getTime());
                editTextDayYearMonth.setText(dayMonthYearString);
            }
        };

        editTextDayYearMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                int currentMonth = calendar.get(Calendar.MONTH);
                int currentYear = calendar.get(Calendar.YEAR);

                new DatePickerDialog(ProductEditActivity.this, onDateSetListener, currentYear, currentMonth, currentDay).show();
            }
        });
    }

    private void configureTimePicker() {
        final Calendar calendar = Calendar.getInstance();

        final TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                calendar.set(Calendar.HOUR, hour);
                calendar.set(Calendar.MINUTE, minute);
                DateFormat timeFormat = DateHelper.INSTANCE.getTimeDateFormat(ProductEditActivity.this);
                String timeString = timeFormat.format(calendar.getTime());
                editTextTime.setText(timeString);
            }
        };

        editTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentHour = calendar.get(Calendar.HOUR);
                int currentMinute = calendar.get(Calendar.MINUTE);

                final boolean IS_24_HOUR_VIEW = true;
                new TimePickerDialog(ProductEditActivity.this, onTimeSetListener, currentHour, currentMinute, IS_24_HOUR_VIEW).show();
            }
        });
    }

    /**
     * This method is called when photo request finishes in other app
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
                Uri receivedImageUri = data.getData();
                savedImageUri = String.valueOf(ImageHelper.saveImageAndGetUri(receivedImageUri, getApplicationContext()));
                currentProduct.setPhotoUri(savedImageUri);
                setPhotoInUi();
            } else {
                Toast.makeText(this, R.string.info_not_picked_image, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
