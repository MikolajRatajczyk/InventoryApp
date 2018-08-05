package com.ratajczykdev.inventoryapp.detailandedit;

import android.app.ActivityOptions;
import android.app.DialogFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ratajczykdev.inventoryapp.R;
import com.ratajczykdev.inventoryapp.catalog.CatalogFragment;
import com.ratajczykdev.inventoryapp.catalog.ProductListRecyclerAdapter;
import com.ratajczykdev.inventoryapp.database.Product;
import com.ratajczykdev.inventoryapp.database.ProductViewModel;
import com.ratajczykdev.inventoryapp.tools.ImageHelper;

import java.util.Locale;

/**
 * Shows details about product and help with making order
 * <p>
 * Gets data from own {@link ProductViewModel}
 *
 * @author Mikolaj Ratajczyk <mikolaj.ratajczyk@gmail.com>
 */
public class ProductDetailActivity extends AppCompatActivity implements OrderDialogFragment.OrderDialogListener {

    /**
     * Activity gets its own {@link ProductViewModel},
     * but with the same repository as e.g. {@link CatalogFragment} and {@link ProductEditActivity}
     */
    private ProductViewModel productViewModel;

    private FloatingActionButton fabEdit;
    private Button buttonDismiss;
    private Button buttonOrder;
    private ImageView imagePhoto;
    private ImageView imageNameIcon;
    private ImageView imageQuantityIcon;
    private ImageView imagePriceIcon;
    private TextView textName;
    private TextView textQuantity;
    private TextView textPrice;

    private Product product;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);

        setUiElementsReferences();

        if (getIntent().hasExtra(ProductListRecyclerAdapter.DATA_SELECTED_PRODUCT_ID)) {
            int productId = getProductIdFromIntent(getIntent());
            product = productViewModel.findSingleById(productId);
            setReceivedProductDataInUi();
            setFabListener();
        } else {
            makeFabAndButtonOrderInvisible();
        }

        setButtonDismissListener();
        setButtonOrderListener();
    }


    private void setUiElementsReferences() {
        imagePhoto = findViewById(R.id.product_detail_photo);
        imageNameIcon = findViewById(R.id.product_detail_name_icon);
        fabEdit = findViewById(R.id.product_detail_edit_fab);
        textName = findViewById(R.id.product_detail_name);
        textQuantity = findViewById(R.id.product_detail_quantity);
        imageQuantityIcon = findViewById(R.id.product_detail_quantity_icon);
        textPrice = findViewById(R.id.product_detail_price);
        imagePriceIcon = findViewById(R.id.product_detail_price_icon);
        buttonOrder = findViewById(R.id.product_detail_order_button);
        buttonDismiss = findViewById(R.id.product_detail_dismiss_button);
    }

    private int getProductIdFromIntent(Intent intent) {
        String stringCurrentProductId = intent.getStringExtra(ProductListRecyclerAdapter.DATA_SELECTED_PRODUCT_ID);
        return Integer.parseInt(stringCurrentProductId);
    }

    /**
     * Set current product data with provided ones
     */
    private void setReceivedProductDataInUi() {
        setQuantityInUi();
        setPriceInUi();
        setNameInUi();
        setPhotoInUi();
    }

    private void setQuantityInUi() {
        int quantity = product.getQuantity();
        textQuantity.setText(String.valueOf(quantity));
    }

    private void setPriceInUi() {
        float price = product.getPrice();
        textPrice.setText(String.format(Locale.US, "%.2f", price));
    }

    private void setNameInUi() {
        String name = product.getName();
        textName.setText(name);
    }

    private void setPhotoInUi() {
        String stringPhotoUri = product.getPhotoUri();
        if (stringPhotoUri != null) {
            Uri photoUri = Uri.parse(stringPhotoUri);
            Bitmap photoBitmap = ImageHelper.getBitmapFromUri(photoUri, getApplicationContext());
            imagePhoto.setImageBitmap(photoBitmap);

        }
    }

    private void setFabListener() {
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pair pairNameIcon = Pair.create(imageNameIcon, imageNameIcon.getTransitionName());
                Pair pairQuantityIcon = Pair.create(imageQuantityIcon, imageQuantityIcon.getTransitionName());
                Pair pairPriceIcon = Pair.create(imagePriceIcon, imagePriceIcon.getTransitionName());
                Pair[] sharedElementsPairs = {pairNameIcon, pairQuantityIcon, pairPriceIcon};

                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(ProductDetailActivity.this, sharedElementsPairs).toBundle();

                Intent intent = new Intent(ProductDetailActivity.this, ProductEditActivity.class);
                intent.putExtra(ProductListRecyclerAdapter.DATA_SELECTED_PRODUCT_ID, String.valueOf(product.getId()));
                startActivity(intent);
            }
        });
    }

    private void makeFabAndButtonOrderInvisible() {
        //  if there is no correct data, so there is no point on editing - hide fab
        fabEdit.setVisibility(View.INVISIBLE);
        // also hide order button
        buttonOrder.setVisibility(View.INVISIBLE);
    }

    private void setButtonDismissListener() {
        buttonDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setButtonOrderListener() {
        buttonOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOrderDialog();
            }
        });
    }

    /**
     * Shows OrderDialogFragment that gets product quantity from user
     */
    private void showOrderDialog() {
        OrderDialogFragment orderDialog = new OrderDialogFragment();
        orderDialog.show(getFragmentManager(), "OrderDialogFragment");
    }

    /**
     * Triggered when user clicks positive button on OrderDialogFragment
     * <p>
     * The dialog fragment receives a reference to this Activity through the
     * Fragment.onAttach() callback, which it uses to call this  method
     * defined by the NoticeDialogFragment.NoticeDialogListener interface
     *
     * @param dialog OrderDialogFragment object
     */
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        int productQuantity = ((OrderDialogFragment) dialog).getQuantity();
        exportOrder(productQuantity);
    }

    /**
     * Sends predefined order to external app
     */
    private void exportOrder(int productQuantity) {
        String productName = textName.getText().toString();

        String subject = getString(R.string.email_order) + " " + productName;
        String body = getString(R.string.email_dear) + "\n\n" +
                getString(R.string.email_would_like) + " " + productName + "." + "\n" +
                getString(R.string.email_number_of) + String.valueOf(productQuantity) + "\n\n"
                + getString(R.string.email_yours);
        String chooserTitle = getString(R.string.select_app_title);

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        //  use Intent.createChooser ... if user uses two or more email apps
        startActivity(Intent.createChooser(emailIntent, chooserTitle));
    }

    /**
     * Triggered when user clicks negative button on OrderDialogFragment
     * <p>
     * The dialog fragment receives a reference to this Activity through the
     * Fragment.onAttach() callback, which it uses to call this  method
     * defined by the NoticeDialogFragment.NoticeDialogListener interface
     *
     * @param dialog OrderDialogFragment object
     */
    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }
}
