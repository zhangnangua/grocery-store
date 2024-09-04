package com.pumpkin.webCache.interceptors;

import android.content.res.Resources;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtil {

    public static boolean assetsToUnzip(Resources resources, String zipFileName, String outPath) {
        try (InputStream inputStream = resources.getAssets().open(zipFileName);
             ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {

            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {

                String name = zipEntry.getName();
                if (zipEntry.isDirectory()) {
                    File folder = new File(outPath, name);
                    folder.mkdirs();
                } else {
                    boolean writeFile = writeFile(zipInputStream, outPath + File.separator + name);
                    if (!writeFile) {
                        return false;
                    }
                }
            }

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static File getFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file;
        }
        return null;
    }

    public static void deleteFile(String filePath) {
        File file = getFile(filePath);
        if (file != null) {
            file.delete();
        }
    }

    public static boolean unzip(String zipFileName, String outPath) {

        try {
            return unzipThrowException(zipFileName, outPath);
        } catch (IOException ignored) {
            return false;
        }

    }

    public static boolean unzipThrowException(String zipFileName, String outPath) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(zipFileName);
             ZipInputStream zipInputStream = new ZipInputStream(fileInputStream)) {

            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {

                String name = zipEntry.getName();

                // Fixed Zip path traversal vulnerability (including ../ paths)
                File onlyOutPathFile = new File(outPath);
                String outCanonicalPath = onlyOutPathFile.getCanonicalPath();

                File f = new File(outPath, name);
                String canonicalPath = f.getCanonicalPath();
                if (!canonicalPath.startsWith(outCanonicalPath)) {
                    throw new SecurityException("zip file path is not safe"
                            + ", zipFileNamee: " + zipFileName
                            + ", name: " + name
                            + ", canonicalPath:" + canonicalPath);
                }

                if (zipEntry.isDirectory()) {

                    File folder = new File(outPath, name);
                    boolean mkdirs = folder.mkdirs();
                } else {

                    boolean writeFile = writeFile(zipInputStream, outPath + File.separator + name);
                    if (!writeFile) {
                        return false;
                    }

                }
            }

        }
        return true;
    }

    public static boolean writeFile(InputStream inputStream, String outputFile) throws IOException {

        File file = new File(outputFile);
        File parentFile = file.getParentFile();
        if (parentFile != null && !parentFile.exists()) {
            boolean mkdirs = parentFile.mkdirs();
        }
        if (file.exists() && file.isDirectory()) {
            boolean delete = file.delete();
            if (!delete) {
                return false;
            }
        }
        if (!file.exists()) {
            try {
                boolean createSuccess = file.createNewFile();
                if (!createSuccess) {
                    return false;
                }
            } catch (IOException ignore) {
                return false;
            }
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            int length;
            byte[] buffer = new byte[1024];
            while ((length = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, length);
            }
        } catch (IOException ignore) {
            throw ignore;
        }
        return true;
    }
}
