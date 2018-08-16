package com.ratajczykdev.inventoryapp.dataexport

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ratajczykdev.inventoryapp.R
import com.ratajczykdev.inventoryapp.catalog.CatalogFragment
import com.ratajczykdev.inventoryapp.database.Product
import com.ratajczykdev.inventoryapp.database.ProductViewModel
import com.ratajczykdev.inventoryapp.detailandedit.ProductDetailActivity
import com.ratajczykdev.inventoryapp.tools.StorageOperations
import kotlinx.android.synthetic.main.fragment_pdf_export.*
import java.io.File

/**
 * Exports database data to PDF file
 *
 * Gets data from own [ProductViewModel]
 * but with the same repository as e.g. [CatalogFragment] and [ProductDetailActivity]
 */
class PdfExportFragment : Fragment() {
    //  change to export PDF not TXT

    private lateinit var productViewModel: ProductViewModel
    private var productList: MutableList<Product> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pdf_export, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)
        productViewModel.all.observe(this, Observer { products -> productList = products ?: mutableListOf() })

        save_to_pdf_button.setOnClickListener { action() }
    }

    private fun action() {
        //  TODO: change method name
        val externalExportDir = StorageOperations.createDirInExternal("exported", context)
        val stringProductList = ProductListExporter.createStringProductList(productList)
        StorageOperations.writeStringToFile(externalExportDir, "exported_database.txt", stringProductList)
        showExportedDatabaseSnackbar()
    }

    private fun showExportedDatabaseSnackbar() {
        Snackbar.make(root_constraintlayout, getString(R.string.snackbar_database_export_success), Snackbar.LENGTH_SHORT)
                .show()
    }
}
