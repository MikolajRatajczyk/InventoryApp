package dev.ratajczyk.inventoryapp.dataexport

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import dev.ratajczyk.inventoryapp.R
import dev.ratajczyk.inventoryapp.catalog.CatalogFragment
import dev.ratajczyk.inventoryapp.database.Product
import dev.ratajczyk.inventoryapp.database.ProductViewModel
import dev.ratajczyk.inventoryapp.detailandedit.ProductDetailActivity
import dev.ratajczyk.inventoryapp.tools.StorageOperations
import kotlinx.android.synthetic.main.fragment_pdf_export.*

/**
 * Exports database data to PDF file
 *
 * Gets data from own [ProductViewModel]
 * but with the same repository as e.g. [CatalogFragment] and [ProductDetailActivity]
 */
class PdfExportFragment : Fragment() {

    private lateinit var productViewModel: ProductViewModel
    private var productList: List<Product> = listOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pdf_export, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)
        productViewModel.all.observe(this, Observer { products -> productList = products ?: listOf() })

        save_to_pdf_button.setOnClickListener { saveDatabaseToPdf() }
    }

    private fun saveDatabaseToPdf() {
        val successSnackbar = Snackbar.make(root_scrollview,
                getString(R.string.snackbar_database_export_success),
                Snackbar.LENGTH_SHORT)
        ExportDatabaseAsyncTask(successSnackbar).execute()
    }

    private inner class ExportDatabaseAsyncTask(val successSnackbar: Snackbar) : AsyncTask<Unit, Unit, Unit>() {
        override fun doInBackground(vararg paramters: Unit?) {
            val externalExportDir = StorageOperations.createExternalDir("exported", context)
            val productListString = ProductListConverter.createStringProductList(productList, isZeroQuantityChecked())

            StorageOperations.writeToPdfFile(externalExportDir, "exported_database", productListString)
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)
            successSnackbar.show()
        }
    }

    private fun isZeroQuantityChecked(): Boolean {
        return include_zero_quantity_switch.isChecked
    }

    //  TODO: complete include photos feature
    //  TODO: fix columns inconsistency in exported file
}
