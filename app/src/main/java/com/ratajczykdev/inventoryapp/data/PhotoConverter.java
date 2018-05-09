package com.ratajczykdev.inventoryapp.data;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

/**
 * Helper class that handles photo conversion
 *
 * @author Mikołaj Ratajczyk
 */
public final class PhotoConverter
{
    /**
     * Default quality for image conversion
     */
    private final static int DEFAULT_QUALITY = 20;
    /**
     * Default compress format for image conversion
     */
    private final static Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.WEBP;
    /**
     * Default photo's max size length
     */
    private final static int DEFAULT_MAX_PHOTO_SIDE_LENGTH = 200;

    /**
     * Private constructor
     */
    private PhotoConverter()
    {
    }

    /**
     * Converts given Bitmap to byte array
     *
     * @param bitmap bitmap to convert
     * @return byte array with image
     */
    static public byte[] bitmapToByteArray(Bitmap bitmap)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(DEFAULT_COMPRESS_FORMAT, DEFAULT_QUALITY, stream);
        return stream.toByteArray();
    }

    /**
     * Recreates Bitmap with reduced max side length
     *
     * @param image image to resize
     * @return resized image
     */
    public static Bitmap getResizedBitmap(Bitmap image)
    {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1)
        {
            width = DEFAULT_MAX_PHOTO_SIDE_LENGTH;
            height = (int) (width / bitmapRatio);
        } else
        {
            height = DEFAULT_MAX_PHOTO_SIDE_LENGTH;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}
