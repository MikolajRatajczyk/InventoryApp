package dev.ratajczyk.inventoryapp.catalog


import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import dev.ratajczyk.inventoryapp.R
import dev.ratajczyk.inventoryapp.database.Product
import dev.ratajczyk.inventoryapp.database.ProductViewModel
import dev.ratajczyk.inventoryapp.detailandedit.ProductDetailActivity
import dev.ratajczyk.inventoryapp.detailandedit.ProductEditActivity
import kotlinx.android.synthetic.main.fragment_catalog.*

/**
 * Loads view with products list
 *
 * Gets data from own [ProductViewModel]
 * @author Mikolaj Ratajczyk <mikolaj.ratajczyk(AT)gmail.com>
 */
class CatalogFragment : Fragment() {

    /**
     * Fragment gets its own [ProductViewModel]
     * but with the same repository as e.g. [ProductDetailActivity]
     */
    private lateinit var productViewModel: ProductViewModel
    private lateinit var productListRecyclerAdapter: ProductListRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //  call OnCreateOptionsMenu method
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_catalog, container, false)

        // Inflate the layout for this fragment
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productListRecyclerAdapter = ProductListRecyclerAdapter(context!!)

        //  Use ViewModelProviders to associate your ViewModel with your UI controller.
        //  When your app first starts, the ViewModelProviders will create the ViewModel.
        //  When the fragment is destroyed, for example through a configuration change, the ViewModel persists.
        //  When the fragment is re-created, the ViewModelProviders return the existing ViewModel
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)
        //  Add an observer for the LiveData returned by getAll().
        //  The onChanged() method fires when the observed data changes
        //  and the fragment is in the foreground.
        productViewModel.all.observe(this,
                Observer<List<Product>>
                { // Update the cached copy of the products in the adapter.
                    productList ->
                    productListRecyclerAdapter.productList = productList ?: emptyList()
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
                .setInterpolator(AnimationUtils.loadInterpolator(context, android.R.interpolator.accelerate_decelerate))
                .setDuration(ANIMATION_DURATION_IN_MS)
                .start()
    }

    /**
     * Modifies appbar to have actions
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_catalog_appbar_actions, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * Handles clicks on appbar's actions
     *
     * Fragment's method is called only, when the Activity didn't consume the event
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.fragment_catalog_appbar_actions_sort_by_name_asc -> {
                productViewModel.allOrderNameAsc.observe(this, Observer<List<Product>> {
                    productListRecyclerAdapter.productList = it!!
                })
                true
            }
            R.id.fragment_catalog_appbar_actions_sort_by_name_desc -> {
                productViewModel.allOrderNameDesc.observe(this, Observer<List<Product>> {
                    productListRecyclerAdapter.productList = it!!
                })
                true
            }
            R.id.fragment_catalog_appbar_actions_sort_by_price_highest -> {
                productViewModel.allOrderPriceDesc.observe(this, Observer<List<Product>> {
                    productListRecyclerAdapter.productList = it!!
                })
                true
            }
            R.id.fragment_catalog_appbar_actions_sort_by_price_lowest -> {
                productViewModel.allOrderPriceAsc.observe(this, Observer<List<Product>> {
                    productListRecyclerAdapter.productList = it!!
                })
                true
            }
            R.id.fragment_catalog_appbar_actions_sort_by_date_newest -> {
                productViewModel.allOrderIdDesc.observe(this, Observer<List<Product>> {
                    productListRecyclerAdapter.productList = it!!
                })
                true
            }
            R.id.fragment_catalog_appbar_actions_sort_by_date_oldest -> {
                productViewModel.allOrderIdAsc.observe(this, Observer<List<Product>> {
                    productListRecyclerAdapter.productList = it!!
                })
                true
            }
            R.id.fragment_catalog_appbar_actions_sort_by_quantity_more -> {
                productViewModel.allOrderQuantityDesc.observe(this, Observer<List<Product>> {
                    productListRecyclerAdapter.productList = it!!
                })
                true
            }
            R.id.fragment_catalog_appbar_actions_sort_by_quantity_less -> {
                productViewModel.allOrderQuantityAsc.observe(this, Observer<List<Product>> {
                    productListRecyclerAdapter.productList = it!!
                })
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
