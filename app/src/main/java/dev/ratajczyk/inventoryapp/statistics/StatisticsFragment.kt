package dev.ratajczyk.inventoryapp.statistics


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import dev.ratajczyk.inventoryapp.R
import dev.ratajczyk.inventoryapp.catalog.CatalogFragment
import dev.ratajczyk.inventoryapp.database.Product
import dev.ratajczyk.inventoryapp.database.ProductViewModel
import dev.ratajczyk.inventoryapp.detailandedit.ProductDetailActivity
import kotlinx.android.synthetic.main.fragment_statistics.*

/**
 * Shows statistics data about products in numeric form
 *
 * Gets data from own [ProductViewModel]
 * but with the same repository as e.g. [CatalogFragment] and [ProductDetailActivity]
 *
 * @author Mikołaj Ratajczyk <mikolaj.ratajczyk(AT)gmail.com>
 */
class StatisticsFragment : Fragment() {

    private lateinit var productViewModel: ProductViewModel
    private var productList: MutableList<Product> = mutableListOf()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_statistics, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)

        productViewModel.all.observe(this, Observer { products ->
            productList = products ?: mutableListOf()
            if (productList.isNotEmpty()) {
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
        items_number_textview.text = itemsNumberString
    }

    private fun getItemsNumber(): Int {
        var itemsNumber = 0
        for (product in productList) {
            itemsNumber += product.quantity
        }
        return itemsNumber
    }

    private fun updateProductsNumberInUi() {
        val productsNumberString = getProductsNumber().toString()
        products_number_textview.text = productsNumberString
    }

    private fun getProductsNumber(): Int {
        return productList.size
    }

    private fun updateMaxPriceInUi() {
        val maximumPriceString = getProductsMaxPrice().toString()
        max_price_textview.text = maximumPriceString
    }

    private fun getProductsMaxPrice(): Float {
        val pricesSet = getPricesSet()
        return pricesSet.max() ?: 0F
    }

    private fun getPricesSet(): Set<Float> {
        val pricesSet = HashSet<Float>()
        for (product in productList) {
            pricesSet.add(product.price)
        }
        return pricesSet
    }

    private fun updateMinPriceInUi() {
        val minimalPriceString = getProductsMinPrice().toString()
        min_price_textview.text = minimalPriceString
    }

    private fun getProductsMinPrice(): Float {
        val pricesSet = getPricesSet()
        return pricesSet.min() ?: 0F
    }

    private fun setFabListener() {
        graphs_fab.setOnClickListener {
            if (context is LoadingFragmentWithArgs) {
                val bundle = Bundle()
                bundle.putSerializable(StatisticsContract.STATISTICS_MAP_NAME, getStatisticsMap())
                (context as LoadingFragmentWithArgs).loadFragmentWithArgs(GraphsFragment(), bundle)
            } else {
                Log.e(StatisticsFragment::class.java.simpleName, "Given context does not implement LoadingFragmentWithArgs")
            }
        }
    }

    private fun getStatisticsMap(): HashMap<String, Float> {
        val statisticsMap = HashMap<String, Float>()
        statisticsMap.put(StatisticsContract.ITEMS_NUMBER_KEY, getItemsNumber().toFloat())
        statisticsMap.put(StatisticsContract.PRODUCTS_NUMBER_KEY, getProductsNumber().toFloat())
        statisticsMap.put(StatisticsContract.PRODUCTS_MAX_PRICE_KEY, getProductsMaxPrice())
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
