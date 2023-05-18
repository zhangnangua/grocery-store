package com.pumpkin.leetcodejava;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class XiaomiSpider {
    private static final String BASE_URL = "https://www.mi.com/global/";
    private static final String PHONE_COMPARISON_URL = "https://www.mi.com/global/product-list/redmi";
    private static final String PHONE_COMPARISON_URL_XIAO_MI = "https://www.mi.com/global/product-list/xiaomi";

    public static void main(String[] args) {
        try {
            List<String> phoneModels = getPhoneModels();
            for (String phoneModel : phoneModels) {
                System.out.println(phoneModel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> getPhoneModels() throws IOException {
        List<String> phoneModels = new ArrayList<>();
        Document doc = Jsoup.connect(PHONE_COMPARISON_URL).get();
//        Elements phoneElements = doc.select("div.phone-compare-item span.phone-name");
        Elements phoneElements = doc.select("bdi");
        for (Element phoneElement : phoneElements) {
            String phoneModel = phoneElement.text();
            phoneModels.add(phoneModel);
        }

        Document doc1 = Jsoup.connect(PHONE_COMPARISON_URL_XIAO_MI).get();
//        Elements phoneElements = doc.select("div.phone-compare-item span.phone-name");
        Elements phoneElements1 = doc1.select("bdi");
        for (Element phoneElement : phoneElements1) {
            String phoneModel = phoneElement.text();
            phoneModels.add(phoneModel);
        }

        return phoneModels;
    }
}

