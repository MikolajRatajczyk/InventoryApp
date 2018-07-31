package com.ratajczykdev.inventoryapp.fragmentsui


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.ratajczykdev.inventoryapp.ProductDetailActivity
import com.ratajczykdev.inventoryapp.R
import com.ratajczykdev.inventoryapp.database.Product
import com.ratajczykdev.inventoryapp.database.ProductViewModel
import com.ratajczykdev.inventoryapp.statistics.GraphsActivity
import com.ratajczykdev.inventoryapp.statistics.StatisticsContract
import kotlinx.android.synthetic.main.fragment_statistics.*

/**
 * Shows statistics data about products in numeric form
 *
 * Gets data from own [ProductViewModel]
 *
 * @author Mikołaj Ratajczyk <mikolaj.ratajczyk@gmail.com>
 */
class StatisticsFragment : Fragment() {

    //  TODO: reduce using double bang (!!)

    /**
     * Fragment gets its own [ProductViewModel],
     * but with the same repository as e.g. [CatalogFragment] and [ProductDetailActivity]
     */
    private lateinit var productViewModel: ProductViewModel
    private var productList: MutableList<Product?>? = mutableListOf()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_statistics, container, false)
        // Inflate the layout for this fragment

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)

        productList = productViewModel.all.value
        productViewModel.all.observe(this, Observer { products ->
            productList = products
            if (productList != null && productList!!.isNotEmpty()) {
                updateStatisticsInUi()
            }
        })

        setFabListener()
        animateFabGraphs()
    }

    private fun updateStatisticsInUi() {
        updateItemsNumberInUi()
        updateProductsNumberInUi()
        updateMaxPriceInUi()
        updateMinPriceInUi()
    }

    private fun updateItemsNumberInUi() {
        val itemsNumberString = getItemsNumber().toString()
        items_number_textview.setText(itemsNumberString)
    }

    private fun getItemsNumber(): Int {
        var itemsNumber = 0
        if (productList!!.isNotEmpty()) {
            for (product in productList!!) {
                itemsNumber += product!!.quantity
            }
        }
        return itemsNumber
    }

    private fun updateProductsNumberInUi() {
        val productsNumberString = getProductsNumber().toString()
        products_number_textview.setText(productsNumberString)
    }

    private fun getProductsNumber(): Int {
        return productList!!.size
    }

    private fun updateMaxPriceInUi() {
        val maximumPriceString = getProductsMaxPrice().toString()
        max_price_textview.setText(maximumPriceString)
    }

    private fun getProductsMaxPrice(): Float {
        val pricesSet = getPricesSet()
        return pricesSet.max() ?: 0F
    }

    private fun getPricesSet(): Set<Float> {
        val pricesSet = HashSet<Float>()
        for (product in productList!!) {
            pricesSet.add(product!!.price)
        }
        return pricesSet
    }

    private fun updateMinPriceInUi() {
        val minimalPriceString = getProductsMinPrice().toString()
        min_price_textview.setText(minimalPriceString)
    }

    private fun getProductsMinPrice(): Float {
        val pricesSet = getPricesSet()
        return pricesSet.min() ?: 0F
    }

    private fun setFabListener() {
        //  TODO: change calling GraphsActivity
        graphs_fab.setOnClickListener { view ->
            val intent = Intent(context, GraphsActivity::class.java)
            if (productList != null && productList!!.isNotEmpty()) {
                //  add statistics Map to Intent for GraphActivity
                intent.putExtra(StatisticsContract.STATISTICS_MAP_NAME, getStatisticsMap())
            }
            startActivity(intent)
        }
    }

    private fun getStatisticsMap(): HashMap<String, Float> {
        val statisticsMap = HashMap<String, Float>()
        statisticsMap.put(StatisticsContract.ITEMS_NUMBER_KEY, getItemsNumber().toFloat())
        statisticsMap.put(StatisticsContract.PRODUCTS_NUMBER_KEY, getProductsNumber().toFloat())
        statisticsMap.put(StatisticsContract.PRODUCTS_MAX_PRICE_KEY, getProductsMaxPrice()!!)
        statisticsMap.put(StatisticsContract.PRODUCTS_MIN_PRICE_KEY, getProductsMinPrice())
        return statisticsMap
    }

    private fun animateFabGraphs() {
        val FULL_ANGLE_IN_DEG = 360F
        val ANIMATION_DURATION_IN_MS = 700L
        graphs_fab.animate()
                .rotation(FULL_ANGLE_IN_DEG)
                .setInterpolator(AnimationUtils.loadInterpolator(context, android.R.interpolator.accelerate_decelerate))
                .setDuration(ANIMATION_DURATION_IN_MS)
                .start()
    }


}
