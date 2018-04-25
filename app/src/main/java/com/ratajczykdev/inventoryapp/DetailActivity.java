package com.ratajczykdev.inventoryapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DetailActivity extends AppCompatActivity
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
     * Button for decreasing product quantity by one
     */
    private Button buttonQuantityDecrease;

    /**
     * Button for making the order from supplier
     */
    private Button buttonOrder;

    /**
     * Button for increasing product quantity by one
     */
    private Button buttonQuantityIncrease;

    /**
     * Button for completely deleting product
     */
    private Button buttonDelete;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        fabEditMode = (FloatingActionButton) findViewById(R.id.fab_edit_mode);

        fabEditMode.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                switchToEditMode();
            }
        });

        textName = (EditText) findViewById(R.id.detail_product_name);
        textPrice = (EditText) findViewById(R.id.detail_product_price);
        textQuantity = (EditText) findViewById(R.id.detail_product_quantity);

        buttonQuantityDecrease = (Button) findViewById(R.id.detail_quantity_decrease_button);
        buttonOrder = (Button) findViewById(R.id.detail_order_button);
        buttonQuantityIncrease = (Button) findViewById(R.id.detail_quantity_increase_button);
        buttonDelete = (Button) findViewById(R.id.detail_delete_button);
    }

    /**
     * Method to switch DetailActivity to edit mode
     */
    private void switchToEditMode()
    {
        textName.setFocusableInTouchMode(true);
        textPrice.setFocusableInTouchMode(true);
        textQuantity.setFocusableInTouchMode(true);
    }
}
