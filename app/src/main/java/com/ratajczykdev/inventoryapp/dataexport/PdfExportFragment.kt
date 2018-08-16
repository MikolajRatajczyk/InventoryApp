package com.ratajczykdev.inventoryapp.dataexport

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
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
import java.util.*

/**
 * Exports database data to PDF file
 *
 * Gets data from own [ProductViewModel]
 * but with the same repository as e.g. [CatalogFragment] and [ProductDetailActivity]
 */
class PdfExportFragment : Fragment() {
    //  change to export PDF not TXT

    private companion object {
        const val STRING_FORMAT_STYLE = "%-10s %-25s %-15s %-10s"
    }

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
        val stringProductList = createStringProductList()
        StorageOperations.writeStringToFile(externalExportDir, "exported_database.txt", stringProductList)
    }

    private fun createStringProductList(): String {
        var stringProductList = createDatabaseHeader() + "\n"
        for (product in productList) {
            val id = product.id.toString()
            val name = product.name.toString()
            val price = product.price.toString()
            val quantity = product.quantity.toString()

            stringProductList += String.format(STRING_FORMAT_STYLE, id, name, price, quantity) + "\n"
        }
        return stringProductList
    }

    private fun createDatabaseHeader(): String {
        val dateLine = Calendar.getInstance().time.toString() + "\n"
        val columnNames = String.format(STRING_FORMAT_STYLE,
                "[ID]", "[NAME]", "[PRICE]", "[QUANTITY]")
        return dateLine + columnNames
    }

}
