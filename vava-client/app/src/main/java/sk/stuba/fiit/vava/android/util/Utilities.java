package sk.stuba.fiit.vava.android.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import sk.stuba.fiit.vava.android.VavaApplication;
import sk.stuba.fiit.vava.android.db.TemporaryUrlProvider;

import static android.os.Environment.getExternalStorageDirectory;

/**
 * Application utilities
 */
public class Utilities {

    /**
     * Get preference from {@link android.content.SharedPreferences}
     * @param keyId ID of preference's String resource
     * @return Preference's value
     */
    public static String getSharedPreference(int keyId) {
        return PreferenceManager.getDefaultSharedPreferences(VavaApplication.getContext())
                .getString(VavaApplication.getContext().getString(keyId), "");
    }

    /**
     * Add JPG specific extension at the end of file name
     * @param nameWithoutExtension String file name
     * @return file name ending .jpg
     */
    public static String buildJpgFileName(@NonNull String nameWithoutExtension) {
        return nameWithoutExtension + ".jpg";
    }

    /**
     * Decode Bitmap from file
     * @param context Context trying to access Bitmap
     * @param fileUri Uri locator of image file
     * @return Decoded Bitmap on success, null otherwise
     */
    public static Bitmap loadBitmapFromFile(@NonNull Context context, @NonNull Uri fileUri) {
        Bitmap bitmap = null;
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    context.getContentResolver().openFileDescriptor(fileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
        } catch (IOException e) {
            Log.e(Utilities.class.getName(), "IOException during Bitmap loading", e);
        } catch (NullPointerException e) {
            Log.e(Utilities.class.getName(), "NullPointerException during Bitmap loading", e);
        }
        return bitmap;
    }

    /**
     * Resize Bitmap
     * @param bitmap Bitmap to be resized
     * @param newWidth New width
     * @param newHeight New height
     * @return Resized copy of Bitmap
     */
    public static Bitmap resizeBitmap(@NonNull Bitmap bitmap, int newWidth, int newHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(
                bitmap, 0, 0, width, height, matrix, false);
        // Recycle old Bitmap
        bitmap.recycle();
        // Return new Bitmap
        return resizedBitmap;
    }

    /**
     * Code Bitmap to file and store it externally
     * @param bitmap Bitmap to be saved
     * @param fileName Name of file to be created for Bitmap
     * @return Created file
     */
    public static File saveBitmapToFile(@NonNull Bitmap bitmap, @NonNull String fileName) {
        File file = new File(getExternalStorageDirectory(), fileName);
        File filePath = new File(file.getParent());
        try {
            // Check existence of directory
            if (file.getParent() != null && !filePath.isDirectory()) {
                // Create new one
                filePath.mkdirs();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            // Set compression
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            Log.e(Utilities.class.getName(), "FileNotFoundException during Bitmap saving", e);
        } catch (IOException e) {
            Log.e(Utilities.class.getName(), "IOException during Bitmap saving", e);
        }
        return file;
    }

    /**
     * {@link TemporaryUrlProvider} instance access method
     * @return Database provider
     */
    public static @NonNull TemporaryUrlProvider getTemporaryUrlProvider() {
        return VavaApplication.getInstance().getTemporaryUrlProvider();
    }
}
