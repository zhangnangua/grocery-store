package com.pumpkin.webCache.util;

import android.util.ArrayMap;

/**
 * 兼容低版本通过getMimeTypeFromExtension 获取 mimeType 获取不对的问题
 */
public class CompatibleMimeType {
    private static final ArrayMap<String, String> MIME_TYPE_MAP = new ArrayMap<>();

    static {
        MIME_TYPE_MAP.put("png", "image/png");
        MIME_TYPE_MAP.put("gif", "image/gif");
        MIME_TYPE_MAP.put("jpg", "image/jpeg");
        MIME_TYPE_MAP.put("jpeg", "image/jpeg");
        MIME_TYPE_MAP.put("bmp", "image/bmp");
        MIME_TYPE_MAP.put("mp2", "audio/x-mpeg");
        MIME_TYPE_MAP.put("mp3", "audio/mp3");
        MIME_TYPE_MAP.put("wav", "audio/wav");
        MIME_TYPE_MAP.put("ogg", "audio/x-ogg");
        MIME_TYPE_MAP.put("mid", "audio/mid");
        MIME_TYPE_MAP.put("midi", "audio/midi");
        MIME_TYPE_MAP.put("m3u", "audio/x-mpegurl");
        MIME_TYPE_MAP.put("m4a", "audio/mp4a-latm");
        MIME_TYPE_MAP.put("m4b", "audio/mp4a-latm");
        MIME_TYPE_MAP.put("m4p", "audio/mp4a-latm");
        MIME_TYPE_MAP.put("mpga", "audio/mpeg");
        MIME_TYPE_MAP.put("wma", "audio/x-ms-wma");
        MIME_TYPE_MAP.put("mpe", "video/mpeg");
        MIME_TYPE_MAP.put("mpg", "video/mpeg");
        MIME_TYPE_MAP.put("mpeg", "video/mpeg");
        MIME_TYPE_MAP.put("3gp", "video/3gpp");
        MIME_TYPE_MAP.put("asf", "video/x-ms-asf");
        MIME_TYPE_MAP.put("avi", "video/x-msvideo");
        MIME_TYPE_MAP.put("m4u", "video/vnd.mpegurl");
        MIME_TYPE_MAP.put("m4v", "video/x-m4v");
        MIME_TYPE_MAP.put("mov", "video/quicktime");
        MIME_TYPE_MAP.put("mp4", "video/mp4");
        MIME_TYPE_MAP.put("rmvb", "video/*");
        MIME_TYPE_MAP.put("wmv", "video/*");
        MIME_TYPE_MAP.put("vob", "video/*");
        MIME_TYPE_MAP.put("mkv", "video/*");
        MIME_TYPE_MAP.put("jar", "application/java-archive");
        MIME_TYPE_MAP.put("zip", "application/zip");
        MIME_TYPE_MAP.put("rar", "application/x-rar-compressed");
        MIME_TYPE_MAP.put("gz", "application/gzip");
        MIME_TYPE_MAP.put("gtar", "application/x-gtar");
        MIME_TYPE_MAP.put("tar", "application/x-tar");
        MIME_TYPE_MAP.put("tgz", "application/x-compressed");
        MIME_TYPE_MAP.put("z", "application/x-compressed");
        MIME_TYPE_MAP.put("htm", "text/html");
        MIME_TYPE_MAP.put("html", "text/html");
        MIME_TYPE_MAP.put("php", "text/php ");
        MIME_TYPE_MAP.put("txt", "text/plain");
        MIME_TYPE_MAP.put("c", "text/plain");
        MIME_TYPE_MAP.put("conf", "text/plain");
        MIME_TYPE_MAP.put("cpp", "text/plain");
        MIME_TYPE_MAP.put("h", "text/plain");
        MIME_TYPE_MAP.put("java", "text/plain");
        MIME_TYPE_MAP.put("log", "text/plain");
        MIME_TYPE_MAP.put("prop", "text/plain");
        MIME_TYPE_MAP.put("rc", "text/plain");
        MIME_TYPE_MAP.put("sh", "text/plain");
        MIME_TYPE_MAP.put("csv", "text/csv");
        MIME_TYPE_MAP.put("xml", "text/xml");
        MIME_TYPE_MAP.put("apk", "application/vnd.android.package-archive");
        MIME_TYPE_MAP.put("bin", "application/octet-stream");
        MIME_TYPE_MAP.put("class", "application/octet-stream");
        MIME_TYPE_MAP.put("exe", "application/octet-stream");
        MIME_TYPE_MAP.put("mpc", "application/vnd.mpohun.certificate");
        MIME_TYPE_MAP.put("msg", "application/vnd.ms-outlook");
        MIME_TYPE_MAP.put("doc", "application/msword");
        MIME_TYPE_MAP.put("docx", "application/msword");
        MIME_TYPE_MAP.put("js", "application/x-javascript");
        MIME_TYPE_MAP.put("pdf", "application/pdf");
        MIME_TYPE_MAP.put("pps", "application/vnd.ms-powerpoint");
        MIME_TYPE_MAP.put("ppt", "application/vnd.ms-powerpoint");
        MIME_TYPE_MAP.put("pptx", "application/vnd.ms-powerpoint");
        MIME_TYPE_MAP.put("wps", "application/vnd.ms-works");
        MIME_TYPE_MAP.put("rtf", "application/rtf");
        MIME_TYPE_MAP.put("xls", "application/vnd.ms-excel");
        MIME_TYPE_MAP.put("xlsx", "application/vnd.ms-excel");
    }

    public static String getMimeTypeByExtension(String extension) {
        if (extension == null) {
            return null;
        }
        return MIME_TYPE_MAP.get(extension);
    }

}
