package com.ratajczykdev.inventoryapp.statistics;

/**
 * Helper class containing constants for statistics data transfer between Activities
 * <p>
 * For example: Intents
 *
 * @author Mikołaj Ratajczyk <mikolaj.ratajczyk@gmail.com>
 */
public final class StatisticsContract {
    //  TODO: convert to Kotlin nad move to another package
    public static final String STATISTICS_MAP_NAME = "STATISTICS_MAP";
    public static final String ITEMS_NUMBER_KEY = "ITEMS_NUMBER";
    public static final String PRODUCTS_NUMBER_KEY = "PRODUCTS_NUMBER";
    public static final String PRODUCTS_MAX_PRICE_KEY = "MAX_PRODUCTS_PRICE";
    public static final String PRODUCTS_MIN_PRICE_KEY = "MIN_PRODUCTS_PRICE";

    private StatisticsContract() {
    }
}
