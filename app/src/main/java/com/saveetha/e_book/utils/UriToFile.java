package com.saveetha.e_book.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class UriToFile {


    public static File uriToFile(Uri uri, Context context) throws IOException {
        // Attempt to get the original file name from the URI
        String originalFileName = getFileNameFromUri(uri, context);

        // If the file name couldn't be determined, use a default name
        if (originalFileName == null) {
            originalFileName = "tempfile.pdf";
        }

        // Create the File object in the cache directory with the derived name
        File file = new File(context.getCacheDir(), originalFileName);

        try (InputStream inputStream = context.getContentResolver().openInputStream(uri);
             OutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[4 * 1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();
        }

        return file;
    }

    private static String getFileNameFromUri(Uri uri, Context context) {
        String fileName = null;

        // Try to get the file name from the ContentResolver (for content URIs)
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    fileName = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // If that didn't work, fallback to extracting from the URI path (for file URIs)
        if (fileName == null) {
            String path = uri.getPath();
            if (path != null) {
                int cut = path.lastIndexOf('/');
                if (cut != -1) {
                    fileName = path.substring(cut + 1);
                }
            }
        }

        return fileName;
    }


}
