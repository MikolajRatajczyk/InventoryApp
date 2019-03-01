package dev.ratajczyk.inventoryapp.tools;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Helper class that handles retrieving/saving images from/to memory
 *
 * @author Mikołaj Ratajczyk <mikolaj.ratajczyk(AT)gmail.com>
 */
public final class ImageHelper {
    private static final String LOG_TAG = ImageHelper.class.getSimpleName();

    /**
     * Empty private constructor to prevent from instantiating the class
     */
    private ImageHelper() {
    }

    public static Uri getUriFromCursor(Cursor cursor, int uriColumnIndex) {
        String uriString = getStringFromCursor(cursor, uriColumnIndex);
        return Uri.parse(uriString);
    }

    public static String getStringFromCursor(Cursor cursor, int columnWithStringIndex) {
        String retrievedString = null;
        if (!(cursor.isNull(columnWithStringIndex))) {
            retrievedString = cursor.getString(columnWithStringIndex);
        }
        return retrievedString;
    }

    public static Bitmap getBitmapFromUri(Uri imageUri, Context context) {
        Bitmap retrievedBitmap = null;
        if (imageUri != null) {
            try {
                retrievedBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(LOG_TAG, "Problem with getting Bitmap from uri: " + imageUri.toString());
            }
        }
        return retrievedBitmap;
    }

    public static Uri getUriForResourceId(int resourceId, Context context) {
        Resources appResources = context.getResources();
        //  for example: "android.resource://your.package/drawable/image_name"
        Uri resourceUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + appResources.getResourcePackageName(resourceId)
                + "/" + appResources.getResourceTypeName(resourceId)
                + "/" + appResources.getResourceEntryName(resourceId));
        return resourceUri;
    }

    public static Uri saveImageAndGetUri(Uri directImageUri, Context context) {
        //  TODO: save image to different path
        final int COMPRESSION_QUALITY_IN_PER = 75;
        Bitmap directImageBitmap = getBitmapFromUri(directImageUri, context);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        directImageBitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESSION_QUALITY_IN_PER, byteArrayOutputStream);

        final String randomUuid = getRandomUuid();
        String savedImageStringPath = MediaStore.Images.Media.insertImage(context.getContentResolver(), directImageBitmap, randomUuid, null);

        return Uri.parse(savedImageStringPath);
    }

    public static String getRandomUuid() {
        return UUID.randomUUID().toString();
    }


}
