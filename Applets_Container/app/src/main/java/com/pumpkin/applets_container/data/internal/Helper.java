package com.pumpkin.applets_container.data.internal;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pumpkin.data.AppUtil;
import com.pumpkin.pac.bean.GameEntity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Helper {

    public static final String CONFIG_SAVE_DATA = "save_life_data.json";

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

    @NonNull
    public static List<GameEntity> strToListGame(@NonNull String info, @NonNull Gson gson) {
        final List<GameEntity> result = new ArrayList<>();
        if (TextUtils.isEmpty(info)) {
            return result;
        }
        try {
            List<GameEntity> snapResult = gson.fromJson(info, new TypeToken<List<GameEntity>>() {
            }.getType());
            result.addAll(snapResult);
        } catch (Exception ignored) {
        }

        return result;
    }

}
