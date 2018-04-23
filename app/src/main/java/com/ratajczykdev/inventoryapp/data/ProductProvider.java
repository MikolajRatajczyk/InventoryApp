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
import android.util.Log;

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
     * Tag for log messages
     */
    private static final String LOG_TAG = ProductProvider.class.getSimpleName();

    /**
     * Matches a content URI to a corresponding code
     */
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static
    {
        //  "#" wildcard can be substituted for an integer
        uriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PATH_PRODUCTS + "/#", PRODUCT_CODE);
        uriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PATH_PRODUCTS, PRODUCTS_CODE);
    }

    /**
     * Database helper
     */
    private ProductDbHelper productDbHelper;

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
        //  TODO: do sanity check for photo (?)
        String name = contentValues.getAsString(ProductEntry.COLUMN_PRODUCT_NAME);
        if (name == null || name.isEmpty())
        {
            throw new IllegalArgumentException("Product requires a name");
        }

        //  can be null, because the database will automatically set it to default 0
        Integer price = contentValues.getAsInteger(ProductEntry.COLUMN_PRODUCT_PRICE);
        if (price != null && price < 0)
        {
            throw new IllegalArgumentException("Product needs valid price");
        }

        //  can be null, because the database will automatically set it to default 0
        Integer quantity = contentValues.getAsInteger(ProductEntry.COLUMN_PRODUCT_QUANTITY);
        if (quantity != null && quantity < 0)
        {
            throw new IllegalArgumentException("Product needs valid quantity");
        }

        SQLiteDatabase sqLiteDatabase = productDbHelper.getWritableDatabase();

        long id = sqLiteDatabase.insert(ProductEntry.TABLE_NAME, null, contentValues);
        if (id == -1)
        {
            Log.e(LOG_TAG, "Insertion to the database failed for  " + uri);
            return null;
        }

        //  Notify all listeners that the data has changed for the product content URI
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs)
    {
        SQLiteDatabase sqLiteDatabase = productDbHelper.getWritableDatabase();

        int deletedRowsNumber;

        final int match = uriMatcher.match(uri);
        if (match == PRODUCTS_CODE)
        {
            deletedRowsNumber = sqLiteDatabase.delete(ProductEntry.TABLE_NAME, selection, selectionArgs);
        } else if (match == PRODUCT_CODE)
        {
            selection = ProductEntry._ID + "=?";
            selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
            deletedRowsNumber = sqLiteDatabase.delete(ProductEntry.TABLE_NAME, selection, selectionArgs);
        } else
        {
            throw new IllegalArgumentException("Deletion from database is not supported for " + uri);
        }

        if (deletedRowsNumber != 0)
        {
            //  Notify all listeners that the data has changed for the product content URI
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deletedRowsNumber;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs)
    {
        final int match = uriMatcher.match(uri);
        if (match == PRODUCTS_CODE)
        {
            return updateProduct(uri, contentValues, selection, selectionArgs);
        }
        if (match == PRODUCT_CODE)
        {
            selection = ProductEntry._ID + "=?";
            selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
            return updateProduct(uri, contentValues, selection, selectionArgs);
        } else
        {
            throw new IllegalArgumentException("Product update in database is not supported for " + uri);
        }
    }

    /**
     * Update one product or more in the database
     *
     * @return number of rows successfully updated in the database
     */
    private int updateProduct(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs)
    {
        //  TODO: do sanity check for photo (?)
        //  TODO: verify

        if (contentValues.containsKey(ProductEntry.COLUMN_PRODUCT_NAME))
        {
            String name = contentValues.getAsString(ProductEntry.COLUMN_PRODUCT_NAME);
            if (name == null || name.isEmpty())
            {
                throw new IllegalArgumentException("Product requires a name");
            }

        }

        //  can be null, because the database will automatically set it to default 0
        if (contentValues.containsKey(ProductEntry.COLUMN_PRODUCT_PRICE))
        {
            Integer price = contentValues.getAsInteger(ProductEntry.COLUMN_PRODUCT_PRICE);
            if (price != null && price < 0)
            {
                throw new IllegalArgumentException("Product needs valid price");
            }
        }


        //  can be null, because the database will automatically set it to default 0
        if (contentValues.containsKey(ProductEntry.COLUMN_PRODUCT_QUANTITY))
        {
            Integer quantity = contentValues.getAsInteger(ProductEntry.COLUMN_PRODUCT_QUANTITY);
            if (quantity != null && quantity < 0)
            {
                throw new IllegalArgumentException("Product needs valid quantity");
            }
        }

        // If no values to update, there is no need to update the database
        if (contentValues.size() == 0)
        {
            return 0;
        }

        SQLiteDatabase sqLiteDatabase = productDbHelper.getWritableDatabase();

        int updatedRowsNumber = sqLiteDatabase.update(ProductEntry.TABLE_NAME, contentValues, selection, selectionArgs);

        if (updatedRowsNumber != 0)
        {
            //  Notify all listeners that the data has changed for the product content URI
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return updatedRowsNumber;
    }

}
