package com.pumpkin.pac.internal;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pumpkin.data.AppUtil;
import com.pumpkin.pac.bean.GameResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Helper {

    public static String readJson(@NonNull String assertPath) {
        try {
            final InputStreamReader isr = new InputStreamReader(AppUtil.INSTANCE.getApplication().getAssets().open(assertPath), StandardCharsets.UTF_8);
            final BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            br.close();
            isr.close();
            return builder.toString();
        } catch (Exception ignored) {
        }
        return "";
    }

    public static Set<GameResponse> strToListGame(@NonNull String info, @NonNull Gson gson) {
        final Set<GameResponse> result = new HashSet<>();
        if (TextUtils.isEmpty(info)) {
            return result;
        }
//        try {
            Set<GameResponse> snapResult = gson.fromJson(info, new TypeToken<Set<GameResponse>>() {
            }.getType());
            result.addAll(snapResult);
//        } catch (Exception ignored) {
//
//        }

        return result;
    }

    public static <T> List<T> strToListT(@NonNull String info, @NonNull Gson gson, Type type) {
        final List<T> result = new ArrayList<>();
        if (TextUtils.isEmpty(info)) {
            return result;
        }
        try {
            List<T> snapResult = gson.fromJson(info, type);
            result.addAll(snapResult);
        } catch (Exception ignored) {
        }

        return result;
    }

}
