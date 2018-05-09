package com.ratajczykdev.inventoryapp;

import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ratajczykdev.inventoryapp.data.ProductContract.ProductEntry;

import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, OrderDialogFragment.OrderDialogListener
{
    /**
     * Identifier for the existing product data loader
     */
    private static final int EXISTING_PRODUCT_LOADER_ID = 1;
    /**
     * Floating action button for switching to edit mode
     */
    private FloatingActionButton fabEditMode;
    /**
     * Product photo
     */
    private ImageView imagePhoto;
    /**
     * Product name
     */
    private TextView textName;
    /**
     * Product quantity
     */
    private TextView textQuantity;
    /**
     * Product price
     */
    private TextView textPrice;
    /**
     * Button for finishing activity
     */
    private Button buttonDismiss;
    /**
     * Button for making the order from supplier
     */
    private Button buttonOrder;
    /**
     * Content URI for the existing product
     */
    private Uri currentProductUri;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_product_detail);

        imagePhoto = findViewById(R.id.product_detail_photo);
        fabEditMode = findViewById(R.id.product_detail_edit_fab);
        textName = findViewById(R.id.product_detail_name);
        textQuantity = findViewById(R.id.product_detail_quantity);
        textPrice = findViewById(R.id.product_detail_price);
        buttonOrder = findViewById(R.id.product_detail_order_button);
        buttonDismiss = findViewById(R.id.product_detail_dismiss_button);

        if (getIntent().getData() != null)
        {
            currentProductUri = getIntent().getData();
            getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER_ID, null, this);

            fabEditMode.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent intent = new Intent(ProductDetailActivity.this, ProductEditActivity.class);
                    intent.setData(currentProductUri);
                    startActivity(intent);
                }
            });
        } else
        {
            //  if there is no correct data, so there is no point on editing - hide fab
            fabEditMode.setVisibility(View.INVISIBLE);
            // also hide order button
            buttonOrder.setVisibility(View.INVISIBLE);
        }

        buttonDismiss.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        buttonOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showOrderDialog();
            }
        });
    }

    /**
     * Shows OrderDialogFragment that gets product quantity from user
     */
    private void showOrderDialog()
    {
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
    public void onDialogPositiveClick(DialogFragment dialog)
    {
        int productQuantity = ((OrderDialogFragment) dialog).getQuantity();
        sendOrder(productQuantity);
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
    public void onDialogNegativeClick(DialogFragment dialog)
    {
        dialog.dismiss();
    }

    /**
     * Method to send predefined order
     */
    private void sendOrder(int productQuantity)
    {
        //  TODO: delete hardcoded Strings
        String productName = textName.getText().toString();

        String subject = "Order - " + productName;
        String body = "Dear Sir/Madam," +
                "\n\nI would like to order " + productName + "." +
                "\nNumber of items: " + String.valueOf(productQuantity)
                + "\n\nYours faithfully,";
        String chooserTitle = "Select an app to send message";

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        //  use Intent.createChooser ... if user uses two or more email apps
        startActivity(Intent.createChooser(emailIntent, chooserTitle));
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle)
    {
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PHOTO,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QUANTITY};

        return new CursorLoader(
                this,
                currentProductUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor)
    {
        //  update with new data
        setProductData(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        releaseProductData();
    }

    /**
     * Replace current product data with blank space
     */
    private void releaseProductData()
    {
        imagePhoto.setImageBitmap(null);
        textName.setText("");
        textQuantity.setText("");
        textPrice.setText("");
    }

    /**
     * Set current product data with provided ones from database
     *
     * @param cursor cursor that contains the existing product data
     */
    private void setProductData(Cursor cursor)
    {
        if (cursor == null || cursor.getCount() < 1)
        {
            Log.e(ProductDetailActivity.class.getSimpleName(), "Error with loading existing product data from database");
            Toast.makeText(this, R.string.error_loading_product, Toast.LENGTH_SHORT).show();
            return;
        }

        if (cursor.moveToFirst())
        {
            int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int photoColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PHOTO);
            int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);

            String name = cursor.getString(nameColumnIndex);
            float price = cursor.getFloat(priceColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);

            textName.setText(name);
            textPrice.setText(String.format(Locale.US, "%.2f", price));
            textQuantity.setText(String.valueOf(quantity));

            if (!(cursor.isNull(photoColumnIndex)))
            {
                // get photo as byte array
                byte[] byteArrayPhoto = cursor.getBlob(photoColumnIndex);
                //  convert byte array to Bitmap
                Bitmap photo = BitmapFactory.decodeByteArray(byteArrayPhoto, 0, byteArrayPhoto.length);
                imagePhoto.setImageBitmap(photo);
            }
        }
    }


}
