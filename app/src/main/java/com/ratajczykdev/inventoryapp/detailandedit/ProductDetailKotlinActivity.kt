package com.ratajczykdev.inventoryapp.detailandedit

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import com.ratajczykdev.inventoryapp.R
import com.ratajczykdev.inventoryapp.database.ProductViewModel
import kotlinx.android.synthetic.main.activity_product_detail.*

/**
 * Shows details about product and help with making order
 *
 * Gets data from own [ProductViewModel]
 *
 * @author Mikolaj Ratajczyk <mikolaj.ratajczyk(AT)gmail.com>
 */
class ProductDetailKotlinActivity : AppCompatActivity(), OrderDialogFragment.OrderDialogListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
    }

    /**
     * Triggered when user clicks positive button on OrderDialogFragment
     *
     * The dialog fragment receives a reference to this Activity through the
     * Fragment.onAttach() callback, which it uses to call this  method
     *
     * @param dialog OrderDialogFragment object
     */
    override fun onDialogPositiveClick(dialog: DialogFragment) {
        val productQuantity = (dialog as OrderDialogFragment).quantity
        exportOrder(productQuantity)
    }

    /**
     * Sends predefined order to external app
     */
    private fun exportOrder(productQuantity: Int) {
        val productName = product_detail_name.text.toString()

        val subject = getString(R.string.email_order) + " " + productName
        val body = getString(R.string.email_dear) + "\n\n" +
                getString(R.string.email_would_like) + " " + productName + "." + "\n" +
                getString(R.string.email_number_of) + productQuantity + "\n\n" +
                getString(R.string.email_yours)
        val chooserTitle = getString(R.string.select_app_title)

        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        emailIntent.putExtra(Intent.EXTRA_TEXT, body)

        //  use Intent.createChooser ... if user uses two or more email apps
        startActivity(Intent.createChooser(emailIntent, chooserTitle))
    }

    /**
     * Triggered when user clicks negative button on OrderDialogFragment
     *
     * The dialog fragment receives a reference to this Activity through the
     * Fragment.onAttach() callback, which it uses to call this  method
     *
     * @param dialog OrderDialogFragment object
     */
    override fun onDialogNegativeClick(dialog: DialogFragment) {
        dialog.dismiss()
    }


}
