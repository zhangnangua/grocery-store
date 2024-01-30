package com.pumpkin.webCache.util;

import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import okhttp3.Response;

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
        final String extension = getFileExtensionFromUrl(url);
        //from system
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        if (TextUtils.isEmpty(mimeType)) {
            //from internal
            mimeType = CompatibleMimeType.getMimeTypeByExtension(extension);
        }
        return mimeType;
    }

    public static String getMimeTypeFromUrl(String url, Response response) {
        String mimeType = getMimeTypeFromUrl(url);
        if (!TextUtils.isEmpty(mimeType)) {
            return mimeType;
        }
        //from server
        final String contentType = response.header("Content-Type");
        if (contentType == null || TextUtils.isEmpty(contentType)) {
            //无法正确的获取到mime type 会导致未知错误 故不使用该资源
            return null;
        }
        final String[] content = contentType.split(";");
        if (content.length > 0) {
            mimeType = content[0];
        }
        return mimeType;
    }

    public static String getMimeTypeFromExtension(String extension) {
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }
}
