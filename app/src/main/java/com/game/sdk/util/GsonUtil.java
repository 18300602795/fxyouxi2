package com.game.sdk.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by liu hong liang on 2016/11/11.
 */
public class GsonUtil {
    public static Gson getGson(){
        return new GsonBuilder().serializeNulls().create();
    }
}
