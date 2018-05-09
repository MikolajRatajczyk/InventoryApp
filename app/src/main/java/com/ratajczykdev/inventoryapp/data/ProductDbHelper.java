package com.ratajczykdev.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ratajczykdev.inventoryapp.data.ProductContract.ProductEntry;

/**
 * Helps with accessing or creating database file
 *
 * @author Mikołaj Ratajczyk
 */
public class ProductDbHelper extends SQLiteOpenHelper
{
    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "products.db";

    /**
     * Database version
     * <p>
     * If database schema changed, this must be incremented
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link ProductDbHelper}
     *
     * @param context context of the app
     */
    public ProductDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    /**
     * This method is called when database is created for the first time
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        //  SQL statement to create the products table
        String SQL_CREATE_PRODUCTS_TABLE = "CREATE TABLE " + ProductEntry.TABLE_NAME + " ("
                + ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ProductEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                + ProductEntry.COLUMN_PRODUCT_PHOTO + " BLOB, "
                + ProductEntry.COLUMN_PRODUCT_PRICE + " REAL NOT NULL DEFAULT 0, "
                + ProductEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL DEFAULT 0);";

        sqLiteDatabase.execSQL(SQL_CREATE_PRODUCTS_TABLE);
    }

    /**
     * This method is called when database needs to be updated
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
    }
}
