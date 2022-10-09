package com.translate.res_translate.engine;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * 翻译引擎
 */
public class Engine {
    private final String originContent;

    private final List<String> listContent = new ArrayList<>();

    @Nonnull
    private final StateListener listener;

    public Engine(String originContent, @NotNull StateListener listener) {
        this.originContent = originContent;
        this.listener = listener;
    }

    public void start() {
        if (Util.checkContentFormat(originContent)) {

            //提取要翻译的内容
            Matcher matcher = Util.matcher(originContent);
            listContent.clear();
            while (matcher.find()) {
                listContent.add(matcher.group(1));
            }

            //翻译
            if (listContent.size() > 0) {

            }

        } else {
            listener.listener(State.FORMAT_FAIL);
        }
    }

    public interface StateListener {
        void listener(State state);
    }
}
