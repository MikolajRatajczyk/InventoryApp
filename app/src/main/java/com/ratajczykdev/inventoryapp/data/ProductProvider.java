package com.ratajczykdev.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ratajczykdev.inventoryapp.data.ProductContract.ProductEntry;

//  TODO: fill methods
public class ProductProvider extends ContentProvider
{
    /**
     * URI matcher code for the content URI for a single product in the products table
     */
    public static final int PRODUCT_CODE = 1;

    /**
     * URI matcher code for the content URI for the products table
     */
    public static final int PRODUCTS_CODE = 2;

    /**
     * Matches a content URI to a corresponding code
     */
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    /**
     * Database helper
     */
    private ProductDbHelper productDbHelper;

    static
    {
        //  "#" wildcard can be substituted for an integer
        uriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PATH_PRODUCTS + "/#", PRODUCT_CODE);
        uriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PATH_PRODUCTS, PRODUCTS_CODE);
    }

    @Override
    public boolean onCreate()
    {
        productDbHelper = new ProductDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder)
    {
        SQLiteDatabase sqLiteDatabase = productDbHelper.getReadableDatabase();
        Cursor cursor;

        int match = uriMatcher.match(uri);
        if (match == PRODUCTS_CODE)
        {
            cursor = sqLiteDatabase.query(ProductEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
        } else if (match == PRODUCT_CODE)
        {
            // one "?" is for one String
            selection = ProductEntry._ID + "=?";
            selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

            cursor = sqLiteDatabase.query(ProductEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
        } else
        {
            throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        //  Set notification URI on the cursor
        //  If the data at this URI changes, there will be need to update the Cursor
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri)
    {
        final int match = uriMatcher.match(uri);
        if (match == PRODUCTS_CODE)
        {
            return ProductEntry.CONTENT_LIST_TYPE;
        } else if (match == PRODUCT_CODE)
        {
            return ProductEntry.CONTENT_ITEM_TYPE;
        } else
        {
            throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues)
    {
        final int match = uriMatcher.match(uri);
        if (match == PRODUCTS_CODE)
        {
            return insertProduct(uri, contentValues);
        } else
        {
            throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a product into the database
     *
     * @param contentValues values for a product
     * @return a new product URI for row in the database
     */
    private Uri insertProduct(Uri uri, ContentValues contentValues)
    {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings)
    {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings)
    {
        return 0;
    }
}
