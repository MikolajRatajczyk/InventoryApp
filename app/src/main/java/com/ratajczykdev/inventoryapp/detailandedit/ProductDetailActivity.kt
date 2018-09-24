package com.ratajczykdev.inventoryapp.detailandedit

import android.app.ActivityOptions
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Pair
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ratajczykdev.inventoryapp.R
import com.ratajczykdev.inventoryapp.catalog.CatalogFragment
import com.ratajczykdev.inventoryapp.catalog.ProductListRecyclerAdapter
import com.ratajczykdev.inventoryapp.database.Product
import com.ratajczykdev.inventoryapp.database.ProductViewModel
import com.ratajczykdev.inventoryapp.tools.DateHelper
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_product_detail.*
import java.util.*

/**
 * Shows details about product and help with making order
 *
 * Gets data from own [ProductViewModel]
 *
 * @author Mikolaj Ratajczyk <mikolaj.ratajczyk(AT)gmail.com>
 */
class ProductDetailActivity : AppCompatActivity(), OrderDialogFragment.OrderDialogListener {

    /**
     * Activity gets its own [ProductViewModel],
     * but with the same repository as e.g. [CatalogFragment] and [ProductEditActivity]
     */
    private lateinit var productViewModel: ProductViewModel
    private lateinit var product: Product


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)

        if (intent.hasExtra(ProductListRecyclerAdapter.DATA_SELECTED_PRODUCT_ID)) {
            val productId = getProductIdFromIntent(intent)
            val liveDataProduct = productViewModel.findSingleById(productId)
            //  here must be anonymous inner class, because of use this in removeObserver
            liveDataProduct.observe(this, object : Observer<Product> {
                override fun onChanged(loadedProduct: Product?) {
                    //  stop getting updates about LiveData
                    liveDataProduct.removeObserver(this)
                    product = loadedProduct!!
                    setReceivedProductDataInUi()
                }
            })
            setFabListener()
            setDismissButtonListener()
            setOrderButtonListener()
        }
    }

    private fun getProductIdFromIntent(intent: Intent): Int {
        val productIdString = intent.getStringExtra(ProductListRecyclerAdapter.DATA_SELECTED_PRODUCT_ID)
        return productIdString.toInt()
    }

    /**
     * Set current product data with provided ones
     */
    private fun setReceivedProductDataInUi() {
        setQuantityInUi()
        setPriceInUi()
        setNameInUi()
        setPhotoInUi()
        setDateInUi()
    }

    private fun setQuantityInUi() {
        val quantity = product.quantity.toString()
        product_detail_quantity.text = quantity
    }

    private fun setPriceInUi() {
        val price = product.price
        product_detail_price.text = String.format(Locale.US, "%.2f", price)
    }

    private fun setNameInUi() {
        val name = product.name
        product_detail_name.text = name
    }

    private fun setPhotoInUi() {
        Picasso.get()
                .load(product.photoUri)
                .placeholder(R.drawable.product_list_item_placeholder)
                .error(R.drawable.ic_error)
                .into(product_detail_photo)
    }

    private fun setDateInUi() {
        val creationDate = product.creationDate
        setDayMonthYearInUi(creationDate)
        setTimeInUi(creationDate)
    }

    private fun setDayMonthYearInUi(creationDate: Date) {
        val dateFormat = DateHelper.getDayMonthYearDateFormat(this)
        val dayMonthYearString = dateFormat.format(creationDate)
        product_detail_day_month_year.text = dayMonthYearString
    }

    private fun setTimeInUi(creationDate: Date) {
        val dateFormat = DateHelper.getTimeDateFormat(this)
        val timeString = dateFormat.format(creationDate)
        product_detail_time.text = timeString
    }

    private fun setFabListener() {
        product_detail_edit_fab.setOnClickListener {
            val pairNameIcon: Pair<View, String> = Pair.create(product_detail_name_icon, product_detail_name_icon.transitionName)
            val pairQuantityIcon: Pair<View, String> = Pair.create(product_detail_quantity_icon, product_detail_quantity_icon.transitionName)
            val pairPriceIcon: Pair<View, String> = Pair.create(product_detail_price_icon, product_detail_price_icon.transitionName)
            val pairDateIcon: Pair<View, String> = Pair.create(product_detail_date_icon, product_detail_date_icon.transitionName)
            val sharedElementPairs = arrayOf(pairNameIcon, pairQuantityIcon, pairPriceIcon, pairDateIcon)

            //  already have an array and want to pass its contents to the function,
            //  use the spread operator (prefix the array with *)
            val bundle = ActivityOptions.makeSceneTransitionAnimation(this@ProductDetailActivity, *sharedElementPairs).toBundle()

            val intent = Intent(this@ProductDetailActivity, ProductEditActivity::class.java)
            intent.putExtra(ProductListRecyclerAdapter.DATA_SELECTED_PRODUCT_ID, product.id.toString())
            startActivity(intent, bundle)
        }
    }

    private fun setDismissButtonListener() {
        product_detail_dismiss_button.setOnClickListener { finish() }
    }

    private fun setOrderButtonListener() {
        product_detail_order_button.setOnClickListener { showOrderDialog() }
    }

    /**
     * Shows [OrderDialogFragment] that gets product quantity from user
     */
    private fun showOrderDialog() {
        val orderDialogFragment = OrderDialogFragment()
        orderDialogFragment.show(supportFragmentManager, "OrderDialogFragment")
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
