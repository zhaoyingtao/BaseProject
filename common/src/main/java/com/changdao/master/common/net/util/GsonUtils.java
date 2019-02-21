package com.changdao.master.common.net.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zyt on 2018/3/26.
 * gson解析分装类
 */

public class GsonUtils {
    private static GsonUtils gsonUtils;
    private static Gson gson;

    public static GsonUtils init() {
        if (gsonUtils == null) {
            synchronized (GsonUtils.class) {
                if (gsonUtils == null) {
                    gsonUtils = new GsonUtils();
                    gson = new Gson();
                }
            }
        }
        return gsonUtils;
    }

    public String obJtoJson(Object object) {
        return gson.toJson(object);
    }

    /**
     * 解析成类
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T fromJsonObject(String jsonStr, Class<T> clazz) {
        return gson.fromJson(jsonStr, clazz);
    }

    /**
     * 解析成list
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> fromJsonArray(String jsonStr, Class<T> clazz) {
        ArrayList<T> mList = new ArrayList<>();
        JsonArray array = new JsonParser().parse(jsonStr).getAsJsonArray();
        for (final JsonElement elem : array) {
            mList.add(gson.fromJson(elem, clazz));
        }
        return mList;
    }
}
