package com.changdao.master.common.net.net;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by zyt on 2017/11/22.
 * 从服务器请求的初始数据
 */

public class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type type;

    public GsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {

        String response = value.string();
        //先将返回的json数据解析到Response中，如果code==200，则解析到我们的实体基类中，否则抛异常
//        Log.e("Gson", "-------------------" + response);

//        StrUtil.saveStrToFile(response);
//        try {
//            org.json.JSONObject jsonObject = new org.json.JSONObject(response);
//            String data = jsonObject.optString("data");
//            if ("false".equals(data)) {
////                return null;
//                jsonObject.remove("data");
//                org.json.JSONObject dataJson = new org.json.JSONObject();
//
//                jsonObject.put("data", dataJson);
//                return gson.fromJson(jsonObject.toString(), type);
//            }
//            return gson.fromJson(response, type);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        return gson.fromJson(response, type);
    }
}
