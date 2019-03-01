package dev.ratajczyk.inventoryapp.dataexport

import android.util.Log
import dev.ratajczyk.inventoryapp.database.Product
import java.util.*

/**
 * Handles converting [Product] list to regular pattern [String]
 */
object ProductListConverter {

    private const val STRING_FORMAT_STYLE = "%-5s %-25s %-15s %-15s %-30s"


    fun createStringProductList(productList: List<Product>, includeZeroQuantity: Boolean = true): String {
        var stringProductList = createDatabaseHeader() + "\n"
        for (product in productList) {
            val id = product.id.toString()
            val name = product.name.toString()
            val price = product.price.toString()
            val quantity = product.quantity.toString()
            val creationDate = dateToFormattedString(product.creationDate)
            if (!includeZeroQuantity and (product.quantity <= 0)) {
                continue
            } else {
                stringProductList += String.format(STRING_FORMAT_STYLE, id, name, price, quantity, creationDate) + "\n"
            }
            //  TODO: delete Log
            Log.e("line: ", stringProductList)
        }
        return stringProductList
    }

    private fun createDatabaseHeader(): String {
        val dateLine = Calendar.getInstance().time.toString() + "\n"
        val columnNames = String.format(STRING_FORMAT_STYLE,
                "[ID]", "[NAME]", "[PRICE]", "[QUANTITY]", "[CREATION DATE]")
        return dateLine + columnNames
    }

    private fun dateToFormattedString(date: Date): String {
        //  TODO: consider moving to [DateHelper]
        val calendar = Calendar.getInstance()
        calendar.time = date
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        return "$day.$month.$year $hour:$minute"
    }
}