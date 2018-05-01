package com.ratajczykdev.inventoryapp;

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

public class ProductDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>
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

        imagePhoto = (ImageView) findViewById(R.id.product_detail_photo);
        fabEditMode = (FloatingActionButton) findViewById(R.id.product_detail_edit_fab);
        textName = (TextView) findViewById(R.id.product_detail_name);
        textQuantity = (TextView) findViewById(R.id.product_detail_quantity);
        textPrice = (TextView) findViewById(R.id.product_detail_price);
        buttonOrder = (Button) findViewById(R.id.product_detail_order_button);

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


        buttonOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                sendOrder();
            }
        });
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
            Toast.makeText(this, "Error with loading product", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cursor.moveToFirst())
        {
            int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int photoColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PHOTO);
            int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);

            String name = cursor.getString(nameColumnIndex);
            // get photo as byte array
            byte[] byteArrayPhoto = cursor.getBlob(photoColumnIndex);
            //  convert byte array to Bitmap
            Bitmap photo = BitmapFactory.decodeByteArray(byteArrayPhoto, 0, byteArrayPhoto.length);
            float price = (float) cursor.getInt(priceColumnIndex) / 100;
            int quantity = cursor.getInt(quantityColumnIndex);

            textName.setText(name);
            imagePhoto.setImageBitmap(photo);
            // get current Locale in order to set proper decimal sign
            Locale currentLocale = getResources().getConfiguration().locale;
            textPrice.setText(String.format(currentLocale, "%.2f", price));
            textQuantity.setText(String.valueOf(quantity));
        }
    }

    /**
     * Method to send predefined order
     */
    private void sendOrder()
    {
        //  TODO: delete hardcoded Strings
        //  TODO: add ability to specify the quantity before making order
        //  TODO: add feature to get user name from Google Account and save it to email body
        String productName = textName.getText().toString();

        String subject = "Order - " + productName;
        String body = "Dear Sir/Madam," +
                "\n\nI would like to order " + productName + "." +
                "\nNumber of items: " + " [QUANTITY]."
                + "\n\nYours faithfully,";
        String chooserTitle = "Select an app to send message";

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        //  use Intent.createChooser ... if user uses two or more email apps
        startActivity(Intent.createChooser(emailIntent, chooserTitle));
    }
}
