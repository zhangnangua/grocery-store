package com.pumpkin.parse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * ParsingEntry
 */
public class ParseEntrance {

    public Document entrance(String url) throws IOException {
        return Jsoup.connect(url).get();
    }
}
