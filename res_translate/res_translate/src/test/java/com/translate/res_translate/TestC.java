package com.translate.res_translate;

import com.intellij.openapi.util.Pair;
import com.translate.res_translate.engine.translate.impl.google.GoogleToken;
import com.translate.res_translate.engine.translate.impl.google.GoogleTranslator;
import com.translate.res_translate.engine.translate.lang.Languages;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestC {

    @Test
    public void getToken() {
        long a = 202905874L;
        long b = 544157181L;
        long c = 419689L;
        Pair<Long, Long> tkk = Pair.create(c, a + b);
        Assertions.assertEquals("34939.454418", GoogleToken.getToken("Translate", tkk));
        Assertions.assertEquals("671407.809414", GoogleToken.getToken("Google translate", tkk));
    }

    @Test
    public void googleTranslate() {
        GoogleTranslator googleTranslator = new GoogleTranslator();
        String hello = googleTranslator.doTranslate(Languages.AUTO, Languages.ENGLISH, "你好");
        Assertions.assertEquals("Hello", hello);
    }

    @Test
    public void testListTranslate() {
        final List<String> stringList = new ArrayList<>();
        final List<String> translateValueList = new ArrayList<>();

        String formatTest = "    <string name=\"daily_gift_title\">Daily free gift pack</string>\n" +
                "    <string name=\"total_package_value\">Total package value</string>\n" +
                "    <string name=\"record\">Record</string>\n" +
                "    <string name=\"pick_up_steps\">Pick up steps</string>\n" +
                "    <string name=\"tomorrow_package\">Tomorrow Package</string>\n" +
                "    <string name=\"event_reminder\">Event Reminder</string>\n" +
                "    <string name=\"daily_sold_out\">Today\\'s package is sold out </string>\n" +
                "    <string name=\"receive_gift_tomorrow\">Come and receive gift packs tomorrow</string>\n" +
                "    <string name=\"gift_hint_1\">1.Copy redemption code.</string>\n" +
                "    <string name=\"gift_hint_2_1\">2.Install the game.</string>\n" +
                "    <string name=\"gift_hint_2_2\">2.Open the game.</string>\n" +
                "    <string name=\"gift_hint_2_3\">2.Update the game.</string>\n" +
                "    <string name=\"gift_hint_3\">3.Go to in-game setting page to redeem.</string>\n" +
                "    <string name=\"subscribe_to_daily\">Subscribe to daily gift packs.</string>\n" +
                "    <string name=\"copy_successfully\">Copy successfully</string>\n" +
                "    <string name=\"copy_failed\">Copy failed</string>\n" +
                "    <string name=\"subscription_successfully\">Subscription successfully</string>\n" +
                "    <string name=\"validity_period\">validity period: </string>\n" +
                "    <string name=\"my_pick_up_record\">My pick up record</string>";

        Pattern p = Pattern.compile("<string .*?>(.*?)</string>");
        Matcher m = p.matcher(formatTest); // 获取 matcher 对象
        while (m.find()) {
            stringList.add(m.group(1));
            System.out.println(m.group(1));
        }
        stringList.forEach(s -> {
            GoogleTranslator googleTranslator = new GoogleTranslator();
            translateValueList.add(googleTranslator.doTranslate(Languages.AUTO, Languages.FRENCH, s));
        });
        System.out.println("--------------------翻译后--------------------");
        translateValueList.forEach(System.out::println);
    }
}
