package com.ratajczykdev.inventoryapp.tools

import android.content.Context
import android.os.Build
import java.util.*

/**
 * Handles receiving [Locale] from device
 */
object LocaleHelper {

    @Suppress("DEPRECATION")
    fun getCurrentLocale(context: Context): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0]
        } else {
            context.resources.configuration.locale
        }
    }

}