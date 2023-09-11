package com.pumpkin.pac_core.cache;

import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.pumpkin.pac_core.BuildConfig;

/**
 * Created by yale on 2018/1/9.
 */

public class MimeTypeMapUtils {
    private static final String TAG = "MimeTypeMapUtils";

    /**
     * url = "https://example.com/images/image.jpg"
     * .jpg
     */
    public static String getFileExtensionFromUrl(String url) {
        url = url.toLowerCase();
        if (!TextUtils.isEmpty(url)) {
            int fragment = url.lastIndexOf('#');
            if (fragment > 0) {
                url = url.substring(0, fragment);
            }

            int query = url.lastIndexOf('?');
            if (query > 0) {
                url = url.substring(0, query);
            }

            int filenamePos = url.lastIndexOf('/');
            String filename =
                    0 <= filenamePos ? url.substring(filenamePos + 1) : url;

            // if the filename contains special characters, we don't
            // consider it valid for our matching purposes:
            if (!filename.isEmpty()) {
                int dotPos = filename.lastIndexOf('.');
                if (0 <= dotPos) {
                    return filename.substring(dotPos + 1);
                }
            }
        }

        return "";
    }

    public static String getMimeTypeFromUrl(String url) {
        String extension = getFileExtensionFromUrl(url);
        if (TextUtils.isEmpty(extension)) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "getMimeTypeFromUrl: parsing error " + url);
            }
            extension = "html";
        }
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    public static String getMimeTypeFromExtension(String extension) {
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }
}
