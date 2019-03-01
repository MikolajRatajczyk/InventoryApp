package dev.ratajczyk.inventoryapp.tools

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

    fun getDayMonthYearDateFormat(context: Context): DateFormat {
        val dateSchema = "dd.MM.yyyy"
        return getDateFormat(dateSchema, context)
    }

    fun getTimeDateFormat(context: Context): DateFormat {
        val timeSchema = "HH:mm"
        return getDateFormat(timeSchema, context)
    }

    private fun getDateFormat(schema: String, context: Context): DateFormat {
        val currentLocale = getCurrentLocale(context)
        return SimpleDateFormat(schema, currentLocale)
    }

    /**
     * Sums bare date and time
     *
     * For example: 27.01.2018 + 17:28 = 27.01.2018 17:28
     */
     fun dayMonthYearPlusTime(dayMonthYear: Date, time: Date): Date {
        val calendarDayMonthYear = Calendar.getInstance()
        calendarDayMonthYear.time = dayMonthYear

        val calendarTime = Calendar.getInstance()
        calendarTime.time = time

        val calendarSum = Calendar.getInstance()
        calendarSum.set(Calendar.YEAR, calendarDayMonthYear.get(Calendar.YEAR))
        calendarSum.set(Calendar.MONTH, calendarDayMonthYear.get(Calendar.MONTH))
        calendarSum.set(Calendar.DAY_OF_MONTH, calendarDayMonthYear.get(Calendar.DAY_OF_MONTH))
        calendarSum.set(Calendar.HOUR_OF_DAY, calendarTime.get(Calendar.HOUR_OF_DAY))
        calendarSum.set(Calendar.MINUTE, calendarTime.get(Calendar.MINUTE))

        return calendarSum.time
    }

}