package com.ratajczykdev.inventoryapp.tools

import android.content.Context
import android.os.Build
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Helps dealing with date
 */
object DateHelper {

    @Suppress("DEPRECATION")
    fun getCurrentLocale(context: Context): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0]
        } else {
            context.resources.configuration.locale
        }
    }

    fun getDateFormat(context: Context): DateFormat {
        val dateSchema = "dd.MM.yyyy"
        val currentLocale = getCurrentLocale(context)
        return SimpleDateFormat(dateSchema, currentLocale)
    }

}