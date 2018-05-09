package com.ratajczykdev.inventoryapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines constants that help work with database content
 */
public final class ProductContract
{

    /**
     * Name for the entire content provider
     */
    public static final String CONTENT_AUTHORITY = "com.ratajczykdev.inventoryapp";

    /**
     * Base of all URI's which app use to contact the content provider
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Added to base content uri creates path to product data
     */
    public static final String PATH_PRODUCTS = "products";

    /**
     * Empty private constructor to prevent from instantiating the class
     */
    private ProductContract()
    {
    }

    /**
     * Inner class that defines constant values for the products' database table
     */
    public static final class ProductEntry implements BaseColumns
    {
        /**
         * Content URI to access the product data in the provider
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCTS);

        /**
         * MIME type of the content uri for a list of products
         */
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        /**
         * MIME type of the content uri for a single pet
         */
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        /**
         * Name of database table for products
         */
        public static final String TABLE_NAME = "products";

        /**
         * Unique ID number for the product
         */
        public static final String _ID = BaseColumns._ID;

        /**
         * Name of the product
         */
        public static final String COLUMN_PRODUCT_NAME = "name";

        /**
         * Photo of the product
         */
        public static final String COLUMN_PRODUCT_PHOTO = "photo";

        /**
         * Price of the product
         */
        public static final String COLUMN_PRODUCT_PRICE = "price";

        /**
         * Quantity of the product
         */
        public static final String COLUMN_PRODUCT_QUANTITY = "quantity";

    }

}
