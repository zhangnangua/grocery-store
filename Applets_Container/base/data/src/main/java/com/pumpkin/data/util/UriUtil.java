package com.pumpkin.data.util;

import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class UriUtil {
    public static Uri urlToUri(String url) {
        if (url == null) {
            return null;
        }
        Uri uri;
        try {
            uri = Uri.parse(url);
        } catch (Exception e) {
            uri = null;
        }
        return uri;
    }

    public static Uri getLegalUriSafely(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        Uri uri;
        try {
            uri = Uri.parse(url);
            uri.getScheme();
            uri.getHost();
            uri.getEncodedPath();
        } catch (Exception ignore) {
            uri = null;
        }
        return uri;
    }

    /**
     * 替换uri的参数值
     *
     * @return 返回新的uri，
     */
    public static Uri replaceUriParameter(@NonNull Uri uri, @NonNull String key, @NonNull String newValue) {
        Set<String> params;
        try {
            params = uri.getQueryParameterNames();
        } catch (Exception e) {
            e.printStackTrace();
            params = new HashSet<>();
        }
        final Uri.Builder newUri = uri.buildUpon().clearQuery();
        for (String param : params) {
            newUri.appendQueryParameter(param, param.equals(key) ? newValue : uri.getQueryParameter(param));
        }

        return newUri.build();
    }

    /**
     * 移除uri的参数值
     *
     * @return 返回新的uri，
     */
    public static Uri removeUriParameter(@NonNull Uri uri, @NonNull String key) {
        Set<String> params;
        try {
            params = uri.getQueryParameterNames();
        } catch (Exception e) {
            e.printStackTrace();
            params = new HashSet<>();
        }
        final Uri.Builder newUri = uri.buildUpon().clearQuery();
        for (String param : params) {
            if (TextUtils.equals(param, key)) {
                continue;
            }
            newUri.appendQueryParameter(param, uri.getQueryParameter(param));
        }

        return newUri.build();
    }

    public static String removeQueryAndFragment(String url) {
        final Uri uri = urlToUri(url);
        if (uri == null) {
            return null;
        }
        final Uri newUri = removeQueryAndFragment(uri);
        return newUri == null ? "" : newUri.toString();
    }

    public static Uri removeQueryAndFragment(Uri originalUri) {
        Uri.Builder builder = new Uri.Builder()
                .scheme(originalUri.getScheme())
                .authority(originalUri.getHost());
        List<String> pathSegments = originalUri.getPathSegments();
        int pathSize = pathSegments == null ? 0 : pathSegments.size();
        if (pathSize > 0) {
            for (String pathSegment : pathSegments) {
                if (!TextUtils.isEmpty(pathSegment)) {
                    builder.appendPath(pathSegment);
                }
            }
        } else {
            builder.appendPath("");
        }
        return builder.build();
    }

    public static boolean uriWithoutQueryFragmentEquals(Uri sourceUri, Uri targetUri) {
        if (sourceUri == null) {
            return targetUri == null;
        } else if (targetUri == null) {
            return false;
        }
        if (sourceUri.equals(targetUri)) {
            return true;
        }
        String sourceScheme = null;
        String targetScheme = null;
        try {
            sourceScheme = sourceUri.getScheme();
        } catch (Exception ignore) {
        }
        try {
            targetScheme = targetUri.getScheme();
        } catch (Exception ignore) {
        }
        if (!Objects.equals(sourceScheme, targetScheme)) {
            return false;
        }
        String sourceHost = null;
        String targetHost = null;
        try {
            sourceHost = sourceUri.getHost();
        } catch (Exception ignore) {
        }
        try {
            targetHost = targetUri.getHost();
        } catch (Exception ignore) {
        }
        if (!Objects.equals(sourceHost, targetHost)) {
            return false;
        }
        List<String> sourcePathSegments = null;
        try {
            sourcePathSegments = sourceUri.getPathSegments();
        } catch (Exception ignore) {
        }
        int sourceSize = sourcePathSegments == null ? 0 : sourcePathSegments.size();

        List<String> targetPathSegments = null;
        try {
            targetPathSegments = targetUri.getPathSegments();
        } catch (Exception ignore) {
        }
        int targetSize = targetPathSegments == null ? 0 : targetPathSegments.size();

        int[] sourceIndex = new int[]{0, sourceSize};
        int[] targetIndex = new int[]{0, targetSize};
        while (true) {
            String sourceText = tryGetNotNullElement(sourcePathSegments, sourceIndex);
            String targetText = tryGetNotNullElement(targetPathSegments, targetIndex);
            if (TextUtils.isEmpty(sourceText)) {
                return TextUtils.isEmpty(targetText);
            } else if (TextUtils.isEmpty(targetText)) {
                return false;
            } else if (!TextUtils.equals(sourceText, targetText)) {
                return false;
            }
        }
    }

    private static String tryGetNotNullElement(List<String> pathSegments, int[] fromAndSize) {
        int index;
        String text = null;
        while ((index = fromAndSize[0]) < fromAndSize[1]) {
            text = pathSegments.get(index);
            fromAndSize[0]++;
            if (!TextUtils.isEmpty(text)) {
                break;
            }
        }
        return text;
    }

}
