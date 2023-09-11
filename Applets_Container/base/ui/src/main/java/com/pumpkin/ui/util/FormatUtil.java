package com.pumpkin.ui.util;

import android.text.TextUtils;
import android.util.Base64;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class FormatUtil {

    private static final float GB = 1024 * 1024 * 1024f;
    private static final float MB = 1024 * 1024f;
    private static final float KB = 1024f;

    @NonNull
    public static String formatMemorySize(long size) {

        if (size <= 0) {
            return "0B";
        }

        int bitCount = Long.bitCount(size);
        long highestOneBit = Long.highestOneBit(size);
        if (bitCount > 1) {
            highestOneBit = highestOneBit << 1;
        }

        int i = 0;
        while (highestOneBit >= 1024 && i < 4) {
            highestOneBit = highestOneBit >> 10;
            i++;
        }

        switch (i) {
            case 0:
                return highestOneBit + "B";
            case 1:
                return highestOneBit + "KB";
            case 2:
                return highestOneBit + "MB";
            case 3:
                return highestOneBit + "GB";
            default:
                return highestOneBit + "TB";
        }

    }

    public static String formatSize(long size) {
        char[] chars = new char[]{
                'B', 'K', 'M', 'G', 'T'
        };
        int length = chars.length;
        int i = 0;
        double doubleSize = size;
        while (i < length - 1 && doubleSize >= 1024) {
            doubleSize /= 1024;
            i++;
        }
        long v = (long) doubleSize;
        if (v == doubleSize) {
            return v + "" + chars[i];
        } else {
            v = (long) (doubleSize * 100);
            return "" + (v / (double) 100) + chars[i];
        }
    }

    public static String formatSize2(long bytes) {
        DecimalFormat format = new DecimalFormat("###.##");
        if (bytes / GB >= 1) {
            return format.format(bytes / GB) + "GB";
        } else if (bytes / MB >= 1) {
            return format.format(bytes / MB) + "MB";
        } else if (bytes / KB >= 1) {
            return format.format(bytes / KB) + "KB";
        } else {
            return bytes + "B";
        }
    }

    public static String upperFirstChar(String s) {

        if (s == null || s.length() == 0) {
            return s;
        }
        char c = s.charAt(0);
        if (c >= 'a' && c <= 'z') {
            char[] toCharArray = s.toCharArray();
            toCharArray[0] -= 32;
            return new String(toCharArray);
        }
        return s;
    }

    public static String getUId(String token) {
        if (TextUtils.isEmpty(token)) {
            return null;
        }
        String[] strings = token.split("\\.");
        if (strings.length != 3) {
            return null;
        }
        byte[] decode = Base64.decode(strings[1], Base64.URL_SAFE);
        String json = new String(decode);
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonObject != null) {
            return jsonObject.optString("id");
        } else {
            return null;
        }
    }

    public static String formatCount(long count) {
        return formatCount(count, true);
    }

    public static String formatCount(long count, boolean whitewash) {

        if (count < 1000) {
            if (whitewash) {
                //当安装次数小于1000次时显示 "1000次以下“
                return "1000+";
            } else {
                return String.valueOf(count);
            }
        } else if (count < 10000) {
            //当安装次数在1000到1万之前时，显示具体安装次数
            return String.valueOf(count);
        } else if (count < 100 * 10000) {
            //当安装次数在1万以上时，以万为单位显示，并且只保留小数点后1位，只入不舍
            String language = Locale.getDefault().getLanguage();
            BigDecimal bigDecimal = new BigDecimal(count);
            if (language.startsWith("zh")) {
                bigDecimal = bigDecimal.divide(new BigDecimal(10000), 1, RoundingMode.UP);
            } else {
                bigDecimal = bigDecimal.divide(new BigDecimal(1000), 1, RoundingMode.UP);
            }
            return bigDecimal.toString() + "K+";
        } else if (count < 10000 * 10000) {
            //当安装次数为百万时，显示"XX百万次安装”
            count = count / (100 * 10000);
            return count + "M+";
        } else {
            //当安装次数超过亿时，显示“XX亿次安装"
            float fCount = 1.0f * count / (10000 * 10000 * 10);
            DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
            String p = decimalFormat.format(fCount);//format 返回的是字符串
            return p + "B+";
        }

    }

    //首字母大写
    public static String captureName(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

    /*
     *时间戳转换为日期
     */
    public static String timestampToDate(long time) {
        if (time < 10000000000L) {
            time = time * 1000;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT0"));
        return simpleDateFormat.format(new Date(time));
    }

    public static boolean isSameDay(long first, long second) {
        final String firstDate = timestampToDate(first);
        final String secondDate = timestampToDate(second);
        return Objects.equals(firstDate, secondDate);
    }
}
