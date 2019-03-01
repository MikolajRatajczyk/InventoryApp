package dev.ratajczyk.inventoryapp.database;


import androidx.room.TypeConverter;

import java.util.Date;

/**
 * Converts a custom class to/from a known type that Room can persist
 */
public class DateConverter {

    @TypeConverter
    public static Date unixToDate(Long unixTime) {
        if (unixTime != null) {
            return new Date(unixTime);
        } else {
            return new Date(0);
        }
    }

    @TypeConverter
    public static Long dateToUnix(Date date) {
        if (date != null) {
            return date.getTime();
        } else {
            return 0L;
        }
    }

}
