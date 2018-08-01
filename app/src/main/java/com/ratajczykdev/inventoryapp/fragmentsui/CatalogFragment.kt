package com.ratajczykdev.inventoryapp.fragmentsui


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.ratajczykdev.inventoryapp.ProductDetailActivity
import com.ratajczykdev.inventoryapp.ProductEditActivity
import com.ratajczykdev.inventoryapp.ProductListRecyclerAdapter
import com.ratajczykdev.inventoryapp.R
import com.ratajczykdev.inventoryapp.database.Product
import com.ratajczykdev.inventoryapp.database.ProductViewModel
import kotlinx.android.synthetic.main.fragment_catalog.*

/**
 * Loads view with products list
 *
 * Gets data from own [ProductViewModel]
 * @author Mikolaj Ratajczyk <mikolaj.ratajczyk@gmail.com>
 */
class CatalogFragment : Fragment() {


    /**
     * Fragment gets its own [ProductViewModel]
     * but with the same repository as e.g. [ProductDetailActivity]
     */
    private lateinit var productViewModel: ProductViewModel
    private lateinit var productListRecyclerAdapter: ProductListRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_catalog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productListRecyclerAdapter = ProductListRecyclerAdapter(context)

        //  Use ViewModelProviders to associate your ViewModel with your UI controller.
        //  TODO: check comment
        //  When your app first starts, the ViewModelProviders will create the ViewModel.
        //  When the activity is destroyed, for example through a configuration change, the ViewModel persists.
        //  When the activity is re-created, the ViewModelProviders return the existing ViewModel
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)
        //  Add an observer for the LiveData returned by getAll().
        //  TODO: check comment
        //  The onChanged() method fires when the observed data changes
        //  and the activity is in the foreground.
        productViewModel.all.observe(this,
                Observer<List<Product>>
                { // Update the cached copy of the products in the adapter.
                    productList ->
                    productListRecyclerAdapter.setProducts(productList)
                })

        product_list_recyclerview.adapter = productListRecyclerAdapter
        product_list_recyclerview.layoutManager = LinearLayoutManager(context)

        setFabListener()
        animateFab()
    }

    private fun setFabListener() {
        fab_new_product.setOnClickListener {
            val intent = Intent(context, ProductEditActivity::class.java)
            startActivity(intent)
        }
    }

    private fun animateFab() {
        val FULL_ANGLE_IN_DEG = 360F
        val ANIMATION_DURATION_IN_MS = 700L
        fab_new_product.animate()
                .rotation(FULL_ANGLE_IN_DEG)
                .setInterpolator(AnimationUtils.loadInterpolator(activity, android.R.interpolator.accelerate_decelerate))
                .setDuration(ANIMATION_DURATION_IN_MS)
                .start()
    }
}
