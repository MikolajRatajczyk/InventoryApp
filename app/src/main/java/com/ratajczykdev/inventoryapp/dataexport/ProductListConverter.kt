package com.ratajczykdev.inventoryapp.dataexport

import com.ratajczykdev.inventoryapp.database.Product
import java.util.*

/**
 * Handles converting [Product] list to regular pattern [String]
 */
object ProductListConverter {

    private const val STRING_FORMAT_STYLE = "%-10s %-25s %-15s %-20s %-20s"


    fun createStringProductList(productList: List<Product>): String {
        var stringProductList = createDatabaseHeader() + "\n"
        for (product in productList) {
            val id = product.id.toString()
            val name = product.name.toString()
            val price = product.price.toString()
            val quantity = product.quantity.toString()
            val creationDate = product.creationDate.toString()

            stringProductList += String.format(STRING_FORMAT_STYLE, id, name, price, quantity, creationDate) + "\n"
        }
        return stringProductList
    }

    private fun createDatabaseHeader(): String {
        val dateLine = Calendar.getInstance().time.toString() + "\n"
        val columnNames = String.format(STRING_FORMAT_STYLE,
                "[ID]", "[NAME]", "[PRICE]", "[QUANTITY]", "[CREATION DATE]")
        return dateLine + columnNames
    }
}