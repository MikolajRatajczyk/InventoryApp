package com.ratajczykdev.inventoryapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

//  TODO: fill
public class ProductDetailActivity extends AppCompatActivity
{
    /**
     * Floating action button for switching to edit mode
     */
    private FloatingActionButton fabEditMode;

    /**
     * Product name
     */
    private EditText textName;

    /**
     * Product price
     */
    private EditText textPrice;

    /**
     * Product quantity
     */
    private EditText textQuantity;

    /**
     * Button for making the order from supplier
     */
    private Button buttonOrder;


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

        if (getIntent().getData() != null)
        {
            setProductDataFromIntent();
        }
    }

    private void setProductDataFromIntent()
    {
        //  TODO: complete this method
    }

    /**
     * Method to send predefined order
     */
    private void sendOrder()
    {
        //  TODO: delete hardcoded Strings
        //  TODO: add ability to specify the quantity before making order
        //  TODO: add ability to specify the name of owner before making order
        //  TODO: add product name to subject
        String subject = "Order - " + "[SOMETHING]";
        String body = "Dear Sir/Madam," +
                "\n\nI would like to order " + "[SOMETHING]." +
                "\nNumber of items: " + " [QUANTITY]."
                + "\n\nYours faithfully,"
                + "\n" + "[OWNER_NAME]";
        String chooserTitle = "Select an app to send message";

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        //  use Intent.createChooser ... if user uses two or more email apps
        startActivity(Intent.createChooser(emailIntent, chooserTitle));
    }
}
