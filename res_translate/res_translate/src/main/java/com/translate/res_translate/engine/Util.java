package com.translate.res_translate.engine;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用工具
 */
public class Util {

    public static final String REGEX = "<string .*?>(.*?)</string>";

    /**
     *
     * 格式校验
     *
     * <string name="daily_gift_title">Daily free gift pack</string>
     * <string name="total_package_value">Total package value</string>
     * <string name="record">Record</string>
     * <string name="pick_up_steps">Pick up steps</string>
     * <string name="tomorrow_package">Tomorrow Package</string>
     * <string name="event_reminder">Event Reminder</string>
     * <string name="daily_sold_out">Today\'s package is sold out </string>
     * <string name="receive_gift_tomorrow">Come and receive gift packs tomorrow</string>
     */
    public static boolean checkContentFormat(@Nullable String content) {
        if (isEmpty(content)) {
            return false;
        }
        return matcher(content).find();
    }

    public static Matcher matcher(@Nonnull String content) {
        Pattern p = Pattern.compile(REGEX);
        return p.matcher(content);
    }

    public static boolean isEmpty(@Nullable CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(@Nullable CharSequence str) {
        return !isEmpty(str);
    }
}
