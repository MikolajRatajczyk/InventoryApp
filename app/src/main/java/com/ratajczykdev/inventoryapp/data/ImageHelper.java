package com.ratajczykdev.inventoryapp.data;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.IOException;

/**
 * Helper class that handles retrieving images from memory
 *
 * @author Mikołaj Ratajczyk <mikolaj.ratajczyk@gmail.com>
 */
public final class ImageHelper
{
    private static final String LOG_TAG = ImageHelper.class.getSimpleName();

    /**
     * Empty private constructor to prevent from instantiating the class
     */
    private ImageHelper()
    {
    }

    public static Uri getUriFromCursor(Cursor cursor, int uriColumnIndex)
    {
        String uriString = getStringFromCursor(cursor, uriColumnIndex);
        return Uri.parse(uriString);
    }

    public static String getStringFromCursor(Cursor cursor, int columnWithStringIndex)
    {
        String retrievedString = null;
        if (!(cursor.isNull(columnWithStringIndex)))
        {
            retrievedString = cursor.getString(columnWithStringIndex);
        }
        return retrievedString;
    }

    public static Bitmap getBitmapFromUri(Uri imageUri, Context context)
    {
        Bitmap retrievedBitmap = null;
        if (imageUri != null)
        {
            try
            {
                retrievedBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                Log.e(LOG_TAG, "Problem with getting Bitmap from uri: " + imageUri.toString());
            }
        }
        return retrievedBitmap;
    }


}
